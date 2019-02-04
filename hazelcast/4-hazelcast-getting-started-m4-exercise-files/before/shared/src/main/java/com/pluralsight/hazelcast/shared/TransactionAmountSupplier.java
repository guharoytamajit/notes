package com.pluralsight.hazelcast.shared;

import com.hazelcast.mapreduce.aggregation.Supplier;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * Created by Grant Little (grant@grantlittle.me)
 */
public class TransactionAmountSupplier
        extends Supplier<Long, Transaction, BigDecimal>
        implements Serializable {

    private Date start;
    private Date end;

    public TransactionAmountSupplier(Date start, Date end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public BigDecimal apply(Map.Entry<Long, Transaction> entry) {
        Date transactionDate = entry.getValue().getTransactionDateTime();
        if (transactionDate.compareTo(start) >= 0
                && transactionDate.compareTo(end) < 0) {
            return entry.getValue().getAmount();
        } else {
            return null;
        }
    }
}
