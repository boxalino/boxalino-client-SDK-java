/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boxalino.test;

import com.boxalino.examples.SearchFilterAdvanced;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
public class SearchFilterAdvancedTest {

    private String account = "boxalino_automated_tests";
    private String password = "boxalino_automated_tests";

    public SearchFilterAdvancedTest() {
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
    public void testFrontendSearchFilterAdvanced() {
        SearchFilterAdvanced _searchFilterAdvanced = new SearchFilterAdvanced();
        try {
            _searchFilterAdvanced.account = this.account;
            _searchFilterAdvanced.password = this.password;
            _searchFilterAdvanced.print = false;

            _searchFilterAdvanced.searchFilterAdvanced();
            List<String> fieldNames = new ArrayList<String>() {
                {
                    add("products_color");
                }
            };
           assertEquals(_searchFilterAdvanced.bxResponse.getHitFieldValues(Arrays.copyOf(fieldNames.toArray(), fieldNames.toArray().length, String[].class), "", true, 0, 10).size(),10);            
           

        } catch (Exception ex) {
            Assert.fail("Expected no exception, but got: " + ex.getMessage());
        }
    }
}
