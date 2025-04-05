package com.spartanwrath;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@SpringBootApplication//(exclude = DataSourceAutoConfiguration.class)
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class SpartanWrathApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(SpartanWrathApplication.class, args);
	}

}
