/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boxalino.test;

import com.boxalino.examples.SearchSortField;
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
public class SearchSortFieldTest {

    private String account = "boxalino_automated_tests";
    private String password = "boxalino_automated_tests";

    public SearchSortFieldTest() {
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
    public void testFrontendSearchSortField() {
        SearchSortField _searchSortField = new SearchSortField();
        try {
            _searchSortField.account = this.account;
            _searchSortField.password = this.password;
            _searchSortField.print = false;

            _searchSortField.searchSortField();
            String sortField = "title";
            List<String> fieldsToMatch=Arrays.asList("41","1940");
            List<String> HitfieldValues=Arrays.asList(_searchSortField.bxResponse.getHitFieldValues(java.util.Arrays.asList(sortField), "", true, 0, 10).keySet().toArray(new String[0]));
            List<String> fieldsSelected = new ArrayList<>(HitfieldValues.subList(0,2));
            assertEquals(fieldsSelected, fieldsToMatch);

        } catch (Exception ex) {
            Assert.fail("Expected no exception, but got: " + ex.getMessage());
        }
    }
}
