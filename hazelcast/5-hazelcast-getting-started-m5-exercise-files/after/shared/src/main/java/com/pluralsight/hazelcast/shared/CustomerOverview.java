package com.pluralsight.hazelcast.shared;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Grant Little (grant@grantlittle.me)
 */
public class CustomerOverview implements Serializable {

    private String customerName;
    private Date dob;
    private List<Address> addresses;

    public CustomerOverview() {
    }

    public CustomerOverview(String customerName, Date dob, List<Address> addresses) {
        this.customerName = customerName;
        this.dob = dob;
        this.addresses = addresses;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }
}
