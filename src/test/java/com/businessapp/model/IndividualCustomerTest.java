package com.businessapp.model;

import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
        assertNull(customer.getFirstName());

        customer.setFirstName("");
        assertEquals(customer.getFirstName(), "");
    }

    @Test
    public void setGetId() throws Exception {
        customer.setId("abc");
        assertEquals(customer.getId(), "abc");

        customer.setId(null);
        assertNull(customer.getId());

        customer.setId("");
        assertEquals(customer.getId(), "");
    }

    @Test
    public void setGetName() throws Exception {
        customer.setName("abc");
        assertEquals(customer.getName(), "abc");

        customer.setName(null);
        assertNull(customer.getName());

        customer.setName("");
        assertEquals(customer.getName(), "");
    }

    @Test
    public void setGetCreated() throws Exception {

        DateFormat format = new SimpleDateFormat("dd.mm.yyyy", Locale.GERMAN);
        Date date = format.parse("03.11.2017");

        customer.setCreated(date);
        assertEquals(customer.getCreated(), date);

        customer.setCreated(null);
        assertNull(customer.getCreated());
    }
}