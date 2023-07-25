package com.aspire.lms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = {"com.aspire.lms"})
@EnableJpaRepositories(basePackages = {"com.aspire.lms.repository"})
@EnableJpaAuditing
@EnableCaching
@Slf4j
@EnableAsync
@EntityScan("com.aspire.lms.model")
public class LmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(LmsApplication.class, args);
	}

}
