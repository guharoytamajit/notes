package com.pluralsight.hazelcast.client;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Created by Grant Little grant@grantlittle.me
 */
@SpringBootApplication
@Configuration
public class ClientApplication {

    public static void main(String... args) {
        SpringApplication.run(ClientApplication.class, args);
    }

    @Bean(destroyMethod = "shutdown")
    public HazelcastInstance clientInstance() throws Exception {
        return HazelcastClient.newHazelcastClient();
    }

}
