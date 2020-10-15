package com.spinozanose.springbootrestoo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringbootRestOoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootRestOoApplication.class, args);
	}

//	@Bean
//	public Docket api() {
//		return new Docket(DocumentationType.SWAGGER_2)
//				.select()
//				.apis(RequestHandlerSelectors.any())
//				.paths(PathSelectors.regex("/spanish-greeting.*"))
//				.build();
//	}

}
