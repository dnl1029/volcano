package hobby.volcano;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class VolcanoApplication {

	public static void main(String[] args) {
		SpringApplication.run(VolcanoApplication.class, args);
	}

}
