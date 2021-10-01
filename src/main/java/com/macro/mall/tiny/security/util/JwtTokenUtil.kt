package com.macro.mall.security.util

import cn.hutool.core.date.DateUtil
import com.macro.mall.security.util.JwtTokenUtil
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

/**
 * JwtToken生成的工具类
 * JWT token的格式：header.payload.signature
 * header的格式（算法、token的类型）：
 * {"alg": "HS512","typ": "JWT"}
 * payload的格式（用户名、创建时间、生成时间）：
 * {"sub":"wang","created":1489079981393,"exp":1489684781}
 * signature的生成算法：
 * HMACSHA512(base64UrlEncode(header) + "." +base64UrlEncode(payload),secret)
 * Created by macro on 2018/4/26.
 */
class JwtTokenUtil {
    @Value("\${jwt.secret}")
    lateinit var secret: String

    @Value("\${jwt.expiration}")
    var expiration: Long = 24* 60 *60

    @Value("\${jwt.tokenHead}")
    lateinit var tokenHead: String

    /**
     * 根据负责生成JWT的token
     */
    private fun generateToken(claims: Map<String, Any>): String {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact()
    }

    /**
     * 从token中获取JWT中的负载
     */
    private val String.claims: Claims?
        get() {
            return try {
                Jwts.parser()
                        .setSigningKey(secret)
                        .parseClaimsJws(this).body
            } catch (e: Exception) {
                LOGGER.info("JWT格式验证失败:{}", this)
                null
            }
        }

    /**
     * 生成token的过期时间
     */
    private fun generateExpirationDate(): Date {
        return Date(System.currentTimeMillis() + expiration * 1000)
    }

    /**
     * 从token中获取登录用户名
     */
    fun getUserNameFromToken(token: String): String? {
        return token.claims?.subject
    }

    /**
     * 验证token是否还有效
     *
     * @param token       客户端传入的token
     * @param userDetails 从数据库中查询出来的用户信息
     */
    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val claims = token.claims?: return false
        val username = claims.subject
        return username == userDetails.username && !claims.isExpired
    }

    /**
     * 判断token是否已经失效
     */
    private val Claims.isExpired: Boolean get() = expiration.before(Date())

    /**
     * 根据用户信息生成token
     */
    fun generateToken(userDetails: UserDetails): String {
        val claims = mutableMapOf<String, Any>()
        claims[CLAIM_KEY_USERNAME] = userDetails.username
        claims[CLAIM_KEY_CREATED] = Date()
        return generateToken(claims)
    }

    /**
     * 当原来的token没过期时是可以刷新的
     *
     * @param oldToken 带tokenHead的token
     */
    fun refreshHeadToken(oldToken: String): String? {
        if (oldToken.isBlank()) {
            return null
        }
        val token = oldToken.substring(tokenHead.length)
        if (token.isBlank()) {
            return null
        }
        //token校验不通过
        val claims = token.claims ?: return null
        //如果token已经过期，不支持刷新
        if (claims.isExpired) {
            return null
        }

        //如果token在30分钟之内刚刷新过，返回原token
        return if (claims.isJustRefresh(30 * 60)) {
            token
        } else {
            claims[CLAIM_KEY_CREATED] = Date()
            generateToken(claims)
        }
    }

    /**
     * 判断token在指定时间内是否刚刚刷新过
     * @param token 原token
     * @param time 指定时间（秒）
     */
    private fun Claims.isJustRefresh(time: Int): Boolean {
        val created = get(CLAIM_KEY_CREATED, Date::class.java)
        val refreshDate = Date()
        //刷新时间在创建时间的指定时间内
        return refreshDate.after(created) && refreshDate.before(DateUtil.offsetSecond(created, time))
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(JwtTokenUtil::class.java)
        private const val CLAIM_KEY_USERNAME = "sub"
        private const val CLAIM_KEY_CREATED = "created"
    }
}