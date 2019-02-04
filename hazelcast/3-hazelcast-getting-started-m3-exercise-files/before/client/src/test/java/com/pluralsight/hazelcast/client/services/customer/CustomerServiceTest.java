package com.pluralsight.hazelcast.client.services.customer;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.pluralsight.hazelcast.storage.StorageNodeApplication;
import com.pluralsight.hazelcast.client.HazelcastClientTestConfiguration;
import com.pluralsight.hazelcast.client.helper.StorageNodeFactory;
import com.pluralsight.hazelcast.shared.Customer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * Grant Little grant@grantlittle.me
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(
        classes = {
                HazelcastClientTestConfiguration.class,
                StorageNodeApplication.class
        }
)
public class CustomerServiceTest {


    @Autowired
    CustomerService customerService;


    @Autowired
    @Qualifier("ClientInstance")
    HazelcastInstance hazelcastInstance;

    @Autowired
    StorageNodeFactory storageNodeFactory;


    @Before
    public void tearDown() {
        hazelcastInstance.getMap(CustomerService.CUSTOMERS_MAP).clear();
    }

    @Test
    public void testAddCustomer() {
        Customer customer = new Customer(1L, "Grant Little", new Date(), "grant@grantlittle.me");
        customerService.addCustomer(customer);

        IMap<Long, Customer> customersMap = hazelcastInstance.getMap(CustomerService.CUSTOMERS_MAP);
        assertEquals(1, customersMap.size());
        assertEquals(customer, customersMap.get(1L));
    }


    @Test
    public void testAddCustomers() {
        Customer customer1 = new Customer(1L, "Grant Little", new Date(), "grant@grantlittle.me");
        Customer customer2 = new Customer(2L, "Simon Smith", new Date(), "simon@fake.com");
        Customer customer3 = new Customer(3L, "Joanne Michaels", new Date(), "joanne@fake.com");

        List<Customer> customers = Arrays.asList(customer1, customer2, customer3);

        customerService.addCustomers(customers);

        IMap<Long, Customer> customersMap = hazelcastInstance.getMap(CustomerService.CUSTOMERS_MAP);
        assertEquals(3, customersMap.size());
        assertEquals(customer1, customersMap.get(1L));
        assertEquals(customer2, customersMap.get(2L));
        assertEquals(customer3, customersMap.get(3L));
    }


    @Test
    public void testNoDataLossWithOnlyOneNode() throws Exception {
        storageNodeFactory.ensureClusterSize(4);

        int maxCustomers = 1000;
        List<Customer> customers = generateCustomers(maxCustomers);
        customerService.addCustomers(customers);

        IMap<Long, Customer> customersMap = hazelcastInstance.getMap(CustomerService.CUSTOMERS_MAP);
        assertEquals(maxCustomers, customersMap.size());
        storageNodeFactory.ensureClusterSize(1);
        assertEquals(maxCustomers, customersMap.size());
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
                    , "Customer " + x //Customer name eg "Customer 1"
                    , c.getTime() // DOB based on calendar above
                    , "customer" + x + "@pluralsight.com") //Email
            );
            //Add 1 year to the calendar for DOBs
            c.add(Calendar.YEAR, 1);
        }
        return customers;
    }












    @Test
    public void testNoDataLossAfterClusterShutdown() throws Exception {

        storageNodeFactory.ensureClusterSize(4);

        int maxCustomers = 1000;

        List<Customer> customers = generateCustomers(maxCustomers);

        customerService.addCustomers(customers);

        IMap<Long, Customer> customersMap = hazelcastInstance.getMap(CustomerService.CUSTOMERS_MAP);
        assertEquals(maxCustomers, customersMap.size());

        storageNodeFactory.ensureClusterSize(0); // Shutdown all storage nodes

        storageNodeFactory.ensureClusterSize(3); //Start another 3 storage nodes

        assertEquals(maxCustomers, customersMap.size());

    }










    @Test
    @SuppressWarnings("unchecked")
    public void testSearchForCustomersWithDob() throws Exception {
        customerService.addCustomers(generateCustomers(10));


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date startDate = sdf.parse("1980/01/01");
        Date endDate = sdf.parse("1981/01/01");

        Collection<Customer> customers = customerService.findCustomer(startDate, endDate);
        assertEquals(1, customers.size());
    }













    @Test
    public void testFindCustomersByEmail() throws Exception {
        customerService.addCustomers(generateCustomers(10));

        Collection<Customer> customers = customerService.findCustomersByEmail("%@pluralsight.com");
        assertEquals(10, customers.size());

    }
















    @Bean(name = "ClientInstance")
    public HazelcastInstance clientInstance(StorageNodeFactory storageNodeFactory, ClientConfig config) throws Exception {
        //Ensure there is at least 1 running instance();
        storageNodeFactory.ensureClusterSize(1);
        return HazelcastClient.newHazelcastClient(config);
    }


}