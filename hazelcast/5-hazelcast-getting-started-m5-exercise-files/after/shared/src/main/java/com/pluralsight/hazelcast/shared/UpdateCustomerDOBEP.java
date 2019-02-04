package com.pluralsight.hazelcast.shared;

import com.hazelcast.map.EntryBackupProcessor;
import com.hazelcast.map.EntryProcessor;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * Created by Grant Little (grant@grantlittle.me)
 */
public class UpdateCustomerDOBEP implements Serializable
        , EntryProcessor<Long, Customer>
        , EntryBackupProcessor<Long, Customer> {

    private Date newDOB;

    public UpdateCustomerDOBEP(Date newDOB) {
        this.newDOB = newDOB;
    }

    @Override
    public void processBackup(Map.Entry<Long, Customer> entry) {
        process(entry);
    }

    @Override
    public Object process(Map.Entry<Long, Customer> entry) {
        Customer customer = entry.getValue();
        customer.setDob(newDOB);
        entry.setValue(customer);
        return true;
    }

    @Override
    public EntryBackupProcessor<Long, Customer> getBackupProcessor() {
        return this;
    }

}
