package S5_T2.IT_ACADEMY;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ItAcademyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItAcademyApplication.class, args);
	}
}
