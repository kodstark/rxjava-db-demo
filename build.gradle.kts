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

val mockitoAgent = configurations.create("mockitoAgent")

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-actuator")

	implementation("io.vertx:vertx-web")
	implementation("io.vertx:vertx-web-client")
	implementation("io.vertx:vertx-pg-client")
	implementation("io.vertx:vertx-rx-java3:${vertxVersion}")

	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

	implementation("org.flywaydb:flyway-core")
	implementation("org.flywaydb:flyway-database-postgresql")
	runtimeOnly("org.postgresql:postgresql")

	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.vertx:vertx-junit5")
	testImplementation("io.vertx:vertx-junit5-rx-java3")

	testCompileOnly("org.projectlombok:lombok")
	testAnnotationProcessor("org.projectlombok:lombok")

	mockitoAgent("org.mockito:mockito-core") { isTransitive = false }
}

tasks.withType<JavaCompile> {
	options.compilerArgs.addAll(arrayOf("-Xlint:deprecation"))
}

tasks.withType<Test> {
	useJUnitPlatform()
	jvmArgs = listOf(
		"-javaagent:${mockitoAgent.asPath}"
	)
}

spotless {
	java {
		removeUnusedImports()
		googleJavaFormat().skipJavadocFormatting()
	}
}