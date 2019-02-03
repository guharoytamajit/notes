package com.pluralsight.hazelcast.client.listeners;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.IQueue;
import com.pluralsight.hazelcast.client.HazelcastClientTestConfiguration;
import com.pluralsight.hazelcast.client.helper.StorageNodeFactory;
import com.pluralsight.hazelcast.shared.Customer;
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

import static org.junit.Assert.*;

/**
 * Created by Grant Little (grant@grantlittle.me)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration( classes = { HazelcastClientTestConfiguration.class, StorageNodeApplication.class})
@Configuration
@EntityScan(basePackages = {"com.pluralsight.hazelcast.storage"})
@EnableJpaRepositories
public class CustomersEntryListenerTest {

    @Autowired
    StorageNodeFactory storageNodeFactory;

    @Autowired
    @Qualifier("ClientInstance")
    HazelcastInstance hazelcastInstance;

    @After
    public void tearDown() throws Exception {
        hazelcastInstance.getMap("customers").clear();
        Thread.sleep(300L);
        hazelcastInstance.getQueue("email-queue").clear();
    }


    @Test
    public void testEntryAdded() throws Exception {
        IMap<Long, Customer> customersMap = hazelcastInstance.getMap("customers");
        Customer customer = new Customer(1L, "Grant", null, "grant@grantlittle.me");
        customersMap.put(1L, customer);

        IQueue<Email> emailQueue = hazelcastInstance.getQueue("email-queue");
        Email email = emailQueue.take();
        assertNotNull(email);
        assertEquals("grant@grantlittle.me", email.getToAddress());

        String expectedBody = "Hi " + customer.getName()
                + ", Thanks for joining our book store.";

        assertEquals(expectedBody, email.getBody());
    }






    @Test(timeout = 60000L)
    public void testEntryUpdated() throws Exception {
        IMap<Long, Customer> customersMap = hazelcastInstance.getMap("customers");
        Customer customer = new Customer(1L, "Grant", null, "incorrect@grantlittle.me");
        customersMap.put(1L, customer);

        IQueue<Email> emailQueue = hazelcastInstance.getQueue("email-queue");
        //Take the customer added email off the queue
        emailQueue.take();

        //Update the customer
        customer = new Customer(1L, "Grant", null, "grant@grantlittle.me");
        customersMap.put(1L, customer);

        Email email = emailQueue.take();
        assertNotNull(email);
        assertEquals("grant@grantlittle.me", email.getToAddress());

        String expectedBody = "Hi " + customer.getName()
                + ", We're just letting you know that we have updated your details.";

        assertEquals(expectedBody, email.getBody());
    }

    @Test(timeout = 60000L)
    public void testEntryRemoved() throws Exception {
        IMap<Long, Customer> customersMap = hazelcastInstance.getMap("customers");
        Customer customer = new Customer(1L, "Grant", null, "grant@grantlittle.me");
        customersMap.put(1L, customer);

        IQueue<Email> emailQueue = hazelcastInstance.getQueue("email-queue");
        //Take the customer added email off the queue
        emailQueue.take();

        //Remove the customers
        customersMap.remove(1L);

        Email email = emailQueue.take();
        assertNotNull(email);
        assertEquals("grant@grantlittle.me", email.getToAddress());

        String expectedBody = "Hi " + customer.getName()
                + ", We're sorry to see you leave us.";

        assertEquals(expectedBody, email.getBody());
    }


}