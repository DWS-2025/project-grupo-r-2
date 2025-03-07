package com.spartanwrath;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@SpringBootApplication//(exclude = DataSourceAutoConfiguration.class)
public class SpartanWrathApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(SpartanWrathApplication.class, args);
	}

}
