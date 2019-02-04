package com.pluralsight.hazelcast.storage;

import com.pluralsight.hazelcast.shared.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by pluralsight on 24/10/2015.
 */
@Repository
public interface CustomerDao extends CrudRepository<Customer, Long> {

}
