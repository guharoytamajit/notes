package com.pluralsight.hazelcast.client.services.rest;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.pluralsight.hazelcast.client.services.customer.CustomerService;
import com.pluralsight.hazelcast.shared.Customer;
import com.pluralsight.hazelcast.shared.EchoCustomerEntryProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Grant Little (grant@grantlittle.me)
 */
@RestController
@RequestMapping
public class HazelcastRestController {


    private CustomerService customerService;
    private HazelcastInstance hazelcastInstance;

    @Autowired
    public HazelcastRestController(
            CustomerService customerService,
            @Qualifier("ClientInstance") HazelcastInstance hazelcastInstance) {
        this.customerService = customerService;
        this.hazelcastInstance = hazelcastInstance;
    }

    @RequestMapping(value = "/api/generate-customers")
    public String generateCustomers() throws Exception {
        customerService.addCustomers(generateCustomers(10));
        return "Done";
    }

    @RequestMapping(value = "/api/echo-customers")
    public String echoCustomers() throws Exception {
        IMap<String, Customer> customersMap = hazelcastInstance.getMap("customers");
        customersMap.executeOnEntries(new EchoCustomerEntryProcessor());
        return "Done";
    }
















    private List<Customer> generateCustomers(int maxCustomers) throws Exception {
        List<Customer> customers = new ArrayList<>(maxCustomers);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date d = sdf.parse("1980/01/01");
        Calendar c = Calendar.getInstance();
        c.setTime(d);

        for (long x=0; x<maxCustomers; x++) {
            customers.add(new Customer(
                    x //Customer ID
                    , "Dr"
                    , "Customer " + x //Customer name eg "Customer 1"
                    , c.getTime() // DOB based on calendar above
                    , "customer" + x + "@pluralsight.com") //Email
            );
            //Add 1 year to the calendar for DOBs
            c.add(Calendar.YEAR, 1);
        }
        return customers;
    }








}
