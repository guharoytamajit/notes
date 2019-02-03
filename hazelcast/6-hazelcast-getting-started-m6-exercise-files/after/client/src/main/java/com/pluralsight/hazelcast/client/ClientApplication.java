package com.pluralsight.hazelcast.client;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.core.HazelcastInstance;
import com.pluralsight.hazelcast.client.services.customer.CustomerService;
import com.pluralsight.hazelcast.client.services.rest.HazelcastRestController;
import com.pluralsight.hazelcast.shared.serialization.PluralsightSerializationFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


/**
 * Created by Grant Little grant@grantlittle.me
 */
@Configuration
@SpringBootApplication
@ComponentScan(basePackageClasses = {
        PluralsightSerializationFactory.class,
        CustomerService.class,
        HazelcastRestController.class
})
public class ClientApplication {

    public static void main(String... args) {
        SpringApplication.run(ClientApplication.class, args);
    }



    @Bean(name = "ClientConfig")
    public ClientConfig clientConfig(PluralsightSerializationFactory pluralsightSerializationFactory) {
        ClientConfig clientConfig = new ClientConfig();
        ClientNetworkConfig networkConfig = clientConfig.getNetworkConfig();
        networkConfig.setConnectionAttemptLimit(0);


        clientConfig.getSerializationConfig()
                .addPortableFactory(
                        PluralsightSerializationFactory.FACTORY_ID,
                        pluralsightSerializationFactory);


        return clientConfig;
    }

    @Bean(name = "ClientInstance", destroyMethod = "shutdown")
    public HazelcastInstance clientInstance(ClientConfig clientConfig) throws Exception {
        return HazelcastClient.newHazelcastClient(clientConfig);
    }



}
