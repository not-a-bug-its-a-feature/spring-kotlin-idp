import org.gradle.internal.impldep.com.amazonaws.retry.v2.SimpleRetryPolicy

plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.5.10"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.ltwrld"
version = "0.0.1-SNAPSHOT"
description = "Demo project for Spring Boot"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
	maven {
		url = uri("https://build.shibboleth.net/nexus/content/repositories/releases")
	}
}

dependencies {
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-security")

//	saml
	implementation("org.opensaml:opensaml-saml-api:4.3.2")
	implementation("org.opensaml:opensaml-saml-impl:4.3.2")
	implementation("com.google.guava:guava:33.4.0-jre")


//	redis
	implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")

//	swagger
	implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.8.5")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

configurations.all {
	resolutionStrategy {
		force("com.google.guava:guava:33.4.0-jre")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
