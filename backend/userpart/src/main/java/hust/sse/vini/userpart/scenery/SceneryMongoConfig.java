/**
 * Credits to Leo, our leader.
 * Many thanks to Leo, who spent hours of efforts in developing this
 * bean to enable our connection to MongoDB.
 */
package hust.sse.vini.userpart.scenery;

import com.mongodb.client.MongoClient;

import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class SceneryMongoConfig {
    public @Bean
    MongoClient mongoClient() {
        return MongoClients.create("mongodb://vini_backend_user:v1n1_back3nd_us3r@218.244.151.221:27017");
    }

    public @Bean
    MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), "mydb");
    }
}
