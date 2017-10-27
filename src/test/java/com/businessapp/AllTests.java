package com.businessapp;

import com.businessapp.model.IndividualCustomerTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
        AppTest.class,
        IndividualCustomerTest.class })

public class AllTests {

}