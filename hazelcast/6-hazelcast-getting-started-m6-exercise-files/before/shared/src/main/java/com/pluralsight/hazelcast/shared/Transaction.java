package com.pluralsight.hazelcast.shared;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Grant Little (grant@grantlittle.me)
 */
public class Transaction implements Serializable {

    private Long transactionId;
    private Long customerId;
    private Date transactionDateTime;
    private BigDecimal amount;

    public Transaction() {
    }

    public Transaction(Long transactionId, Long customerId, Date transactionDateTime, BigDecimal amount) {
        this.transactionId = transactionId;
        this.customerId = customerId;
        this.transactionDateTime = transactionDateTime;
        this.amount = amount;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Date getTransactionDateTime() {
        return transactionDateTime;
    }

    public void setTransactionDateTime(Date transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;

        if (transactionId != null ? !transactionId.equals(that.transactionId) : that.transactionId != null)
            return false;
        if (customerId != null ? !customerId.equals(that.customerId) : that.customerId != null) return false;
        if (transactionDateTime != null ? !transactionDateTime.equals(that.transactionDateTime) : that.transactionDateTime != null)
            return false;
        return !(amount != null ? !amount.equals(that.amount) : that.amount != null);

    }

    @Override
    public int hashCode() {
        int result = transactionId != null ? transactionId.hashCode() : 0;
        result = 31 * result + (customerId != null ? customerId.hashCode() : 0);
        result = 31 * result + (transactionDateTime != null ? transactionDateTime.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", customerId=" + customerId +
                ", transactionDateTime=" + transactionDateTime +
                ", amount=" + amount +
                '}';
    }

}
