package com.pluralsight.hazelcast.shared;

import com.hazelcast.map.EntryBackupProcessor;
import com.hazelcast.map.EntryProcessor;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Grant Little (grant@grantlittle.me)
 */
public class EchoCustomerEntryProcessor implements EntryProcessor<Long, Customer>, Serializable {

    @Override
    public Object process(Map.Entry<Long, Customer> entry) {
        Customer customer = entry.getValue();
        System.out.println(customer.toString());
        return null;
    }

    @Override
    public EntryBackupProcessor<Long, Customer> getBackupProcessor() {
        return null;
    }
}
