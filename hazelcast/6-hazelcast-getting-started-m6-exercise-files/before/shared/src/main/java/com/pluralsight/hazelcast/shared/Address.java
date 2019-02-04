package com.pluralsight.hazelcast.shared;

import java.io.Serializable;

/**
 * Created by Grant Little (grant@grantlittle.me)
 */
public class Address implements Serializable {

    private Long addressId;
    private String street;
    private String postcode;
    private String country;
    private Long customerId;

    public Address() {
    }

    public Address(Long addressId, Long customerId, String street, String postcode, String country) {
        this.addressId = addressId;
        this.customerId = customerId;
        this.street = street;
        this.postcode = postcode;
        this.country = country;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
