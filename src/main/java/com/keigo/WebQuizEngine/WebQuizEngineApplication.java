package com.keigo.WebQuizEngine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = UserRepository.class)

public class WebQuizEngineApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebQuizEngineApplication.class, args);
	}

/* TODO
        Clean up exception handler
            ADD error message for authorization 401
            create email already exists exception
        ADD COMMENTS
        add QuizService to simplify controller
        ADD UNIT TESTS
        REFACTOR CLASS STRUCTURE

*/
}
