package vn.edu.hcmus.fit.learningpath;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AcademicAdvisorBackendApplication {

    public static void main(String[] args) {

        System.out.println("Hello World");
        SpringApplication.run(AcademicAdvisorBackendApplication.class, args);
        System.out.println("Hello World 2");
    }
}
