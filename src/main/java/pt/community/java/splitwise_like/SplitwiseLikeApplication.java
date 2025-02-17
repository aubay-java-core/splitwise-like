package pt.community.java.splitwise_like;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class})
public class SplitwiseLikeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SplitwiseLikeApplication.class, args);
	}

}
