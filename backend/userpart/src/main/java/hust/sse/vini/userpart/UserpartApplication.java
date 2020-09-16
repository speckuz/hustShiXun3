package hust.sse.vini.userpart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

@SpringBootApplication(exclude = MongoAutoConfiguration.class)
public class UserpartApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserpartApplication.class, args);
    }

}
