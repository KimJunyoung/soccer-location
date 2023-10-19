package com.soccerlocation;

import com.soccerlocation.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(AppConfig.class)
@SpringBootApplication
public class SoccerlocationApplication {

	public static void main(String[] args) {
		SpringApplication.run(SoccerlocationApplication.class, args);
	}

}
