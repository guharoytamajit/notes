package com.pluralsight.hazelcast.client.services.reporting;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.pluralsight.hazelcast.client.HazelcastClientTestConfiguration;
import com.pluralsight.hazelcast.client.helper.StorageNodeFactory;
import com.pluralsight.hazelcast.shared.Transaction;
import com.pluralsight.hazelcast.storage.StorageNodeApplication;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by Grant Little (grant@grantlittle.me)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(
        classes = {
                HazelcastClientTestConfiguration.class,
                StorageNodeApplication.class
        }
)
public class ReportingServiceTest {

    @Autowired
    ReportingService reportingService;

    @Autowired
    @Qualifier("ClientInstance")
    private HazelcastInstance hazelcastInstance;

    @Test
    public void testGetIncome() throws Exception {

        generateTransactions(100);

        Calendar cal = getCurrentDate();
        Date end = cal.getTime();
        cal.add(Calendar.DATE, -5);
        Date start = cal.getTime();

        BigDecimal result = reportingService.getIncome(start, end);
        assertTrue(result.compareTo(new BigDecimal("5"))==0);
    }


    public void generateTransactions(int count) {
        IMap<Long, Transaction> transactionsMap = hazelcastInstance.getMap("transactions");
        Calendar current = getCurrentDate();
        Long customerId = 1L;
        for (long x=count; x>0; x--) {
            Transaction transaction = new Transaction(x, customerId, current.getTime(), new BigDecimal("1"));
            transactionsMap.put(x, transaction);
            current.add(Calendar.DATE,-1);
        }

    }








    public Calendar getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        DateUtils.truncate(cal, Calendar.DATE);
        return cal;
    }


    @Bean(name = "ClientInstance")
    public HazelcastInstance clientInstance(StorageNodeFactory storageNodeFactory, ClientConfig config) throws Exception {
        //Ensure there is at least 1 running instance();
        storageNodeFactory.ensureClusterSize(1);
        return HazelcastClient.newHazelcastClient(config);
    }


}