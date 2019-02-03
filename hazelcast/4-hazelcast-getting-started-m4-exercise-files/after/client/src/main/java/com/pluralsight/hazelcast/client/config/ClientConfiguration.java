package com.pluralsight.hazelcast.client.config;

import com.hazelcast.client.config.ClientConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Grant Little grant@grantlittle.me
 */
@Configuration
public class ClientConfiguration {

    @Bean(name = "HazelcastClientConfig")
    public ClientConfig clientConfig() {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.getNetworkConfig().setConnectionAttemptLimit(0);
        return clientConfig;
    }


}
