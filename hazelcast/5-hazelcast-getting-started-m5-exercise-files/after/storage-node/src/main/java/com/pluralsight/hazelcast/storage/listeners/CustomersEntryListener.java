package com.pluralsight.hazelcast.storage.listeners;

import com.hazelcast.core.*;
import com.pluralsight.hazelcast.shared.Customer;
import com.pluralsight.hazelcast.shared.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.UUID;

/**
 * Created by Grant Little (grant@grantlittle.me)
 */
@Service
public class CustomersEntryListener
        implements
        com.hazelcast.map.listener.EntryAddedListener<Long, Customer>,
        com.hazelcast.map.listener.EntryUpdatedListener<Long, Customer>,
        com.hazelcast.map.listener.EntryRemovedListener<Long, Customer>    {

    private HazelcastInstance hazelcastInstance;
    private String referenceId;

    public CustomersEntryListener() {
    }

    @Autowired
    public CustomersEntryListener(@Qualifier("StorageNodeInstance")HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    @PostConstruct
    public void init() {
        IMap<Long, Customer> customersMap = hazelcastInstance.getMap("customers");
        referenceId = customersMap.addLocalEntryListener(this);
    }

    @PreDestroy
    public void preDestroy() {
        IMap<Long, Customer> customersMap = hazelcastInstance.getMap("customers");
        customersMap.removeEntryListener(referenceId);
    }










    @Override
    public void entryAdded(EntryEvent<Long, Customer> event) {
        Customer customer = event.getValue();
        if (customer.getEmail() != null) {

            String generatedUuid = UUID.randomUUID().toString();
            String emailAddress = customer.getEmail();
            String subject = "Welcome to our Book Store";
            String body = "Hi " + customer.getName()
                    + ", Thanks for joining our book store.";
            Email email = new Email(generatedUuid, emailAddress, subject, body);

            IQueue<Email> emailQueue = hazelcastInstance.getQueue("email-queue");
            emailQueue.add(email);
        }
    }












    @Override
    public void entryUpdated(EntryEvent<Long, Customer> event) {
        Customer customer = event.getValue();
        if (customer.getEmail() != null) {

            String generatedUuid = UUID.randomUUID().toString();
            String emailAddress = customer.getEmail();
            String subject = "Update of customer details";
            String body = "Hi " + customer.getName()
                    + ", We're just letting you know that we have updated your details.";
            Email email = new Email(generatedUuid, emailAddress, subject, body);

            IQueue<Email> emailQueue = hazelcastInstance.getQueue("email-queue");
            emailQueue.add(email);
        }

    }










    @Override
    public void entryRemoved(EntryEvent<Long, Customer> event) {
        Customer customer = event.getOldValue();
        if (customer.getEmail() != null) {

            String generatedUuid = UUID.randomUUID().toString();
            String emailAddress = customer.getEmail();
            String subject = "Sorry to see you go";
            String body = "Hi " + customer.getName()
                    + ", We're sorry to see you leave us.";
            Email email = new Email(generatedUuid, emailAddress, subject, body);

            IQueue<Email> emailQueue = hazelcastInstance.getQueue("email-queue");
            emailQueue.add(email);
        }

    }

}
