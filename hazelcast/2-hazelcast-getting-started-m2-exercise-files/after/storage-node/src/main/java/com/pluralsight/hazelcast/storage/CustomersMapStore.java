package com.pluralsight.hazelcast.storage;

import com.pluralsight.hazelcast.shared.Customer;
import com.pluralsight.hazelcast.storage.CustomerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by pluralsight on 24/10/2015.
 */
@Service
public class CustomersMapStore implements com.hazelcast.core.MapStore<Long, Customer> {

    private CustomerDao customerDao;

    @Autowired
    public CustomersMapStore(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    @Override
    public void store(Long key, Customer value) {
        customerDao.save(value);
    }

    @Override
    public void storeAll(Map<Long, Customer> map) {
        customerDao.save(map.values());
    }

    @Override
    public void delete(Long key) {
        Customer customer = load(key);
        customerDao.delete(customer);
    }

    @Override
    public void deleteAll(Collection<Long> keys) {
        Iterable<Customer> customers = customerDao.findAll(keys);
        customerDao.delete(customers);
    }

    @Override
    public Customer load(Long key) {
        return customerDao.findOne(key);
    }

    @Override
    public Map<Long, Customer> loadAll(Collection<Long> keys) {
        Iterable<Customer> customers = customerDao.findAll(keys);
        return StreamSupport.stream(customers.spliterator(), false)
                .collect(Collectors.toMap(Customer::getId, Function.identity()));    }

    @Override
    public Iterable<Long> loadAllKeys() {
        Iterable<Customer> customers = customerDao.findAll();
        return StreamSupport.stream(customers.spliterator(), false)
                .map(Customer::getId)
                .collect(Collectors.toList());
    }

}
