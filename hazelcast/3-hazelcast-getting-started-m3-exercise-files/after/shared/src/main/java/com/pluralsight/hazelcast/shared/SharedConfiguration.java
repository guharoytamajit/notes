package com.pluralsight.hazelcast.shared;

import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Grant Little grant@grantlittle.me
 */
@Configuration
@ComponentScan
@EnableJpaRepositories
@EntityScan
public class SharedConfiguration {
}
