package com.pluralsight.hazelcast.client.services.reporting;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.aggregation.Aggregation;
import com.hazelcast.mapreduce.aggregation.Aggregations;
import com.hazelcast.mapreduce.aggregation.Supplier;
import com.pluralsight.hazelcast.shared.TransactionAmountSupplier;
import com.pluralsight.hazelcast.shared.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Grant Little (grant@grantlittle.me)
 */
@Service
public class ReportingService {

    private HazelcastInstance hazelcastInstance;
    private IMap<Long, Transaction> transactionsMap;

    @Autowired
    public ReportingService(@Qualifier("ClientInstance")HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    @PostConstruct
    public void init() throws Exception {
        transactionsMap = hazelcastInstance.getMap("transactions");
    }

    @SuppressWarnings("unchecked")
    public BigDecimal getIncome(Date start, Date end) {
        TransactionAmountSupplier supplier = new TransactionAmountSupplier(start, end);
        Aggregation<Long, BigDecimal, BigDecimal> aggregation = Aggregations.bigDecimalSum();
        return transactionsMap.aggregate(supplier, aggregation);
    }
}
