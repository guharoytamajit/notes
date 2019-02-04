package com.pluralsight.hazelcast.shared;

import com.hazelcast.core.PartitionAware;

import java.io.Serializable;

/**
 * Created by Grant Little (grant@grantlittle.me)
 */
public class AddressKey implements Serializable, PartitionAware<Long> {

    private Long addressId;
    private Long customerId;

    public AddressKey() {
    }

    public AddressKey(Long addressId, Long customerId) {
        this.addressId = addressId;
        this.customerId = customerId;
    }

    @Override
    public Long getPartitionKey() {
        return customerId;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
