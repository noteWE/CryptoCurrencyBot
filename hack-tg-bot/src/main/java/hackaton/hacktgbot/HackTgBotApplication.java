package hackaton.hacktgbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HackTgBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(HackTgBotApplication.class, args);
	}

}
