package com.vincenzoracca.webflux.configs;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.vincenzoracca.webflux.domains.User;
import com.vincenzoracca.webflux.repos.UserMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;

@Configuration
@Profile("dev")
@RequiredArgsConstructor
public class MongoConfig extends AbstractReactiveMongoConfiguration {

    private final Environment env;

    @Override
    protected String getDatabaseName() {
        return "users";
    }

    @Override
    @Bean
    @DependsOn("embeddedMongoServer")
    public MongoClient reactiveMongoClient(){
        var port = env.getProperty("local.mongo.port", Integer.class);
        return MongoClients.create(String.format("mongodb://localhost:%d", port));
    }

    @Bean
    public CommandLineRunner insertData(UserMongoRepository userMongoRepository) {
        return args -> {
            userMongoRepository.save(new User("Vincenzo", "Racca")).subscribe();
            userMongoRepository.save(new User("Mario", "Rossi")).subscribe();
            userMongoRepository.save(new User("Gennaro", "Esposito")).subscribe();
            userMongoRepository.save(new User("Diego", "della Lega")).subscribe();
        };
    }
}
