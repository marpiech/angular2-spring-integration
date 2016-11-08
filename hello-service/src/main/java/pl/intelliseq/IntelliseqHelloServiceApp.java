package pl.intelliseq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class IntelliseqHelloServiceApp {

	public static void main(String[] args) {
		SpringApplication.run(IntelliseqHelloServiceApp.class, args);
	}
	
	@RequestMapping("/")
    public String hello() {
        return "{\"message\": \"hello\"}";
    }
	
}
