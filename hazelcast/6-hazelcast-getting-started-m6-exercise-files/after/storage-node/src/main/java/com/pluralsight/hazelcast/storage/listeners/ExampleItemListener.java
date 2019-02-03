package com.pluralsight.hazelcast.storage.listeners;

import com.hazelcast.core.*;
import com.pluralsight.hazelcast.shared.Email;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by Grant Little (grant@grantlittle.me)
 */
@Service
public class ExampleItemListener
        implements com.hazelcast.core.ItemListener<Email> {

    private HazelcastInstance hazelcastInstance;
    private String listenerId;

    private static Log LOG = LogFactory.getLog("AUDIT");

    @Autowired
    public ExampleItemListener(@Qualifier("StorageNodeInstance")HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    @Override
    public void itemAdded(ItemEvent<Email> item) {
        LOG.info("Email scheduled to be processed " + item.getItem());
    }

    @Override
    public void itemRemoved(ItemEvent<Email> item) {
        LOG.info("Email is being processed " + item.getItem());
    }

    @PostConstruct
    public void init() {
        IQueue<Email> emailQueue = hazelcastInstance.getQueue("email-queue");
        this.listenerId = emailQueue.addItemListener(this, true);
    }

    @PreDestroy
    public void stop() {
        IQueue<Email> emailQueue = hazelcastInstance.getQueue("email-queue");
        emailQueue.removeItemListener(this.listenerId);
    }

}
