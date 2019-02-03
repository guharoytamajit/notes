package com.pluralsight.hazelcast.client;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Created by Grant Little grant@grantlittle.me
 */
@Configuration
@SpringBootApplication
public class ClientApplication {

    public static void main(String... args) {
        SpringApplication.run(ClientApplication.class, args);
    }

    @Bean(name = "ClientConfig")
    public ClientConfig clientConfig() {
        ClientConfig clientConfig = new ClientConfig();
        ClientNetworkConfig networkConfig = clientConfig.getNetworkConfig();
        networkConfig.setConnectionAttemptLimit(0);
        return clientConfig;
    }

    @Bean(name = "ClientInstance", destroyMethod = "shutdown")
    public HazelcastInstance clientInstance(ClientConfig clientConfig) throws Exception {
        return HazelcastClient.newHazelcastClient(clientConfig);
    }



}
