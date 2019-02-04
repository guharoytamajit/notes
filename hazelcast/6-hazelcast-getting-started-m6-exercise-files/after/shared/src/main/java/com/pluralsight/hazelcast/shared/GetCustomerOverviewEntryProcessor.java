package com.pluralsight.hazelcast.shared;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IMap;
import com.hazelcast.map.EntryBackupProcessor;
import com.hazelcast.map.EntryProcessor;
import com.hazelcast.query.SqlPredicate;

import java.beans.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Grant Little (grant@grantlittle.me)
 */
public class GetCustomerOverviewEntryProcessor implements Serializable
        , EntryProcessor<Long, Customer>
        , HazelcastInstanceAware {


    private transient HazelcastInstance hazelcastInstance;

    @Override
    public Object process(Map.Entry<Long, Customer> entry) {
        Long customerKey = entry.getKey();
        Customer customer = entry.getValue();

        IMap<AddressKey, Address> addressMap = hazelcastInstance.getMap("addresses");
        Set<AddressKey> addressKeys = addressMap.localKeySet(new SqlPredicate("customerId = " + customerKey));
        List<Address> addresses = new ArrayList<>(addressKeys.size());
        for (AddressKey addressKey : addressKeys) {
            addresses.add(addressMap.get(addressKey));
        }

        return new CustomerOverview(customer.getName(), customer.getDob(), addresses);
    }

    @Override
    public EntryBackupProcessor<Long, Customer> getBackupProcessor() {
        return null;
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }
}
