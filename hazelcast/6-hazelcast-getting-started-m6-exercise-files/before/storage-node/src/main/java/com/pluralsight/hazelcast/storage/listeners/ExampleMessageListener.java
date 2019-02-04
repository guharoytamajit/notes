package com.pluralsight.hazelcast.storage.listeners;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ITopic;
import com.hazelcast.core.Message;
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
public class ExampleMessageListener implements com.hazelcast.core.MessageListener<String> {

    private static final Log LOG = LogFactory.getLog(ExampleMessageListener.class);

    private HazelcastInstance hazelcastInstance;
    private String listenerId;

    @Autowired
    public ExampleMessageListener(@Qualifier("StorageNodeInstance")HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    @Override
    public void onMessage(Message<String> message) {
        LOG.info(message.getMessageObject());
    }

    @PostConstruct
    public void postConstruct() {
        ITopic<String> topic = hazelcastInstance.getTopic("topic-name");
        listenerId = topic.addMessageListener(this);
    }

    @PreDestroy
    public void preDestroy() {
        ITopic<String> topic = hazelcastInstance.getTopic("topic-name");
        topic.removeMessageListener(listenerId);
    }
}
