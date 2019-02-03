package com.pluralsight.hazelcast.client;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.pluralsight.hazelcast.client.helper.StorageNodeFactory;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Grant Little grant@grantlittle.me
 */
@Configuration
@ComponentScan(basePackages = {
        "com.pluralsight.hazelcast.shared",
        "com.pluralsight.hazelcast.client",
        "com.pluralsight.hazelcast.storage",
})
public class HazelcastClientTestConfiguration {

    @Bean(name = "ClientInstance")
    public HazelcastInstance clientInstance(StorageNodeFactory storageNodeFactory, ClientConfig config) throws Exception {
        //Ensure there is at least 1 running instance();
        storageNodeFactory.ensureClusterSize(1);
        return HazelcastClient.newHazelcastClient(config);
    }

}
