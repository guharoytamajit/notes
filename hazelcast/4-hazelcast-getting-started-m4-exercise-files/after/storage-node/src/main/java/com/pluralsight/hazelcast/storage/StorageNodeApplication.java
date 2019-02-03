package com.pluralsight.hazelcast.storage;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.pluralsight.hazelcast.shared.MapNames;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@SpringBootApplication
@ComponentScan
@EnableJpaRepositories
public class StorageNodeApplication implements MapNames {

    @Bean(destroyMethod = "shutdown")
    public HazelcastInstance createStorageNode(
            @Qualifier("StorageNodeConfig")
            Config config
    ) throws Exception {
        return Hazelcast.newHazelcastInstance(config);
    }



    @Bean(name="StorageNodeConfig")
    public Config config(CustomersMapStore customersMapStore,
                         EmailQueueStore emailQueueStore) throws Exception {
        Config config = new Config();


        MultiMapConfig multiMapConfig = new MultiMapConfig("account-to-customers");
        multiMapConfig.setValueCollectionType(MultiMapConfig.ValueCollectionType.LIST);
        config.addMultiMapConfig(multiMapConfig);


        //Email Queue Configuration
        QueueConfig emailQueueConfig = new QueueConfig();
        emailQueueConfig.setName("email-queue");
        QueueStoreConfig emailQueueStoreConfig = new QueueStoreConfig();
        emailQueueStoreConfig.setStoreImplementation(emailQueueStore);

        emailQueueConfig.setQueueStoreConfig(emailQueueStoreConfig);
        config.addQueueConfig(emailQueueConfig);



        //Create a new map configuration for the customers map
        MapConfig customerMapConfig = new MapConfig();

        //Create a map store config for the customer information
        MapStoreConfig customerMapStoreConfig = new MapStoreConfig();
        customerMapStoreConfig.setImplementation(customersMapStore);
        customerMapStoreConfig.setWriteDelaySeconds(3);

        //Update the customers map configuration to use the
        //customers map store config we just created
        customerMapConfig.setMapStoreConfig(customerMapStoreConfig);
        customerMapConfig.setName("customers");

        MapIndexConfig dobFieldIndex = new MapIndexConfig("dob", true);
        customerMapConfig.addMapIndexConfig(dobFieldIndex);


        //Add the customers map config to our storage node config
        config.addMapConfig(customerMapConfig);

        return config;
    }







    public static void main(String[] args) {
        SpringApplication.run(StorageNodeApplication.class, args);
    }


}
