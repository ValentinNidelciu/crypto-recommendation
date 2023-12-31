package ro.assignment.cryptorec;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CryptorecApplication {

	public static void main(String[] args) {
		SpringApplication.run(CryptorecApplication.class, args);
	}

}
