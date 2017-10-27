package com.businessapp.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class IndividualCustomerTest {
    private IndividualCustomer customer;
    @Before
    public void setUp() throws Exception {
        customer = new IndividualCustomer();
    }

    @Test
    public void setGetFirstName() throws Exception {
        customer.setFirstName("Meier");
        assertEquals(customer.getFirstName(), "Meier");

        customer.setFirstName(null);
        assertEquals(customer.getFirstName(), null);

        
        customer.setFirstName("");
        assertEquals(customer.getFirstName(), "");
    }
}