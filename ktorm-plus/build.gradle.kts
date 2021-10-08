dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.ktorm:ktorm-core:3.4.1")
    implementation("org.ktorm:ktorm-jackson:3.4.1")
    compileOnly("org.postgresql:postgresql:42.2.5")
    implementation("org.springframework:spring-tx")

    implementation("com.github.zeshaoaaa:OkReflect:master-SNAPSHOT")
}
