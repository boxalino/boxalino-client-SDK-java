/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boxalino.test;

import Helper.HttpContext;
import boxalino.client.SDK.BxClient;
import com.boxalino.examples.SearchDebugRequest;
import com.boxalino.p13n.api.thrift.ChoiceRequest;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author pc
 */
public class SearchDebugRequestTest {

    private String account = "boxalino_automated_tests";
    private String password = "boxalino_automated_tests";

    public SearchDebugRequestTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void testFrontendSearchDebugRequest() {
        SearchDebugRequest _searchDebugRequest = new SearchDebugRequest();
        try {
            _searchDebugRequest.account = this.account;
            _searchDebugRequest.password = this.password;
            _searchDebugRequest.print = false;

            _searchDebugRequest.searchDebugRequest();
            ChoiceRequest choice = new ChoiceRequest();
            HttpContext httpContext = new HttpContext("", "", "", "", "");
            _searchDebugRequest.bxClient = new BxClient(account, password, "", false, null, 0, null, null, null, null, httpContext);
            assertEquals(_searchDebugRequest.bxClient.getThriftChoiceRequest().getClass(), choice.getClass());
        } catch (Exception ex) {
            Assert.fail("Expected no exception, but got: " + ex.getMessage());
        }
    }
}
