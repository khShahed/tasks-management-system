package com.shahed.taskmanagementsystem;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.TimeZone;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@AllArgsConstructor
@SpringBootApplication
@EnableScheduling
@OpenAPIDefinition(info = @Info(
	title = "Task Management System",
	version = "1.0",
	description = "Task Management System API Documentation"
))
public class TaskManagementSystemApplication {

	private final Environment env;

	public static void main(String[] args) {
		SpringApplication.run(TaskManagementSystemApplication.class, args);
	}

	@PostConstruct
	public void init() {
		log.info("Task Management System Initialisation");

		String appTimeZone = env.getProperty("app.default.timezone", "UTC");
		log.info("Setting Application Default Time Zone to: " + appTimeZone);
		TimeZone.setDefault(TimeZone.getTimeZone(appTimeZone));

		String appCharSet = env.getProperty("app.default.charset", StandardCharsets.UTF_8.toString());
		log.info("Setting Application Default File Encoding to: " + appCharSet);
		System.setProperty("file.encoding", appCharSet);
	}
}
