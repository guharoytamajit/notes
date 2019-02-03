package com.pluralsight.hazelcast.client.listeners;

import com.hazelcast.core.HazelcastInstance;
import com.pluralsight.hazelcast.client.HazelcastClientTestConfiguration;
import com.pluralsight.hazelcast.client.helper.StorageNodeFactory;
import com.pluralsight.hazelcast.shared.Email;
import com.pluralsight.hazelcast.storage.StorageNodeApplication;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * Created by Grant Little (grant@grantlittle.me)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration( classes = { HazelcastClientTestConfiguration.class, StorageNodeApplication.class})
@Configuration
@EntityScan(basePackages = {"com.pluralsight.hazelcast.storage"})
@EnableJpaRepositories
public class ExampleItemListenerTest {

    @Autowired
    StorageNodeFactory storageNodeFactory;

    @Autowired
    @Qualifier("ClientInstance")
    HazelcastInstance hazelcastInstance;

    @After
    public void tearDown() throws Exception {
        hazelcastInstance.getQueue("email-queue").clear();
    }

    @Test
    public void testLog() throws Exception {
        storageNodeFactory.ensureClusterSize(5);
        hazelcastInstance.getQueue("email-queue")
                .add(new Email("1234", "grant@grantlittle.me", "subject", "body"));
    }


}