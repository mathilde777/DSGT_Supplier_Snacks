package be.kuleuven.caffeinerestservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@EnableTransactionManagement
public class CaffeineRestServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CaffeineRestServiceApplication.class, args);
	}

}
