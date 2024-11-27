plugins {
	id("java")
	id("org.springframework.boot") version "3.4.0"
	id("io.spring.dependency-management") version "1.1.6"
	id("com.diffplug.spotless") version "6.25.0"
}

group = "pl.kodstark"
version = "0.0.1-SNAPSHOT"

val vertxVersion = "4.5.11"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencyManagement {
	imports {
		mavenBom("io.vertx:vertx-stack-depchain:${vertxVersion}")
	}
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-actuator")

	implementation("io.vertx:vertx-web")
	implementation("io.vertx:vertx-pg-client")
	implementation("io.vertx:vertx-rx-java3:${vertxVersion}")

	implementation("org.flywaydb:flyway-core")
	implementation("org.flywaydb:flyway-database-postgresql")
	runtimeOnly("org.postgresql:postgresql")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

spotless {
	java {
		removeUnusedImports()
		googleJavaFormat().skipJavadocFormatting()
	}
}