/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boxalino.test;

import com.boxalino.examples.SearchAutocompleteItems;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pc
 */
public class SearchAutoCompleteItemsTest {

    private String account = "boxalino_automated_tests";
    private String password = "boxalino_automated_tests";

    public SearchAutoCompleteItemsTest() {
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
    public void testFrontendSearchAutocompleteItems() {
        SearchAutocompleteItems _searchAutocompleteItems = new SearchAutocompleteItems();
        try {
            _searchAutocompleteItems.account = this.account;
            _searchAutocompleteItems.password = this.password;
            _searchAutocompleteItems.print = false;

            List<String> textualSuggestions =new ArrayList<String>();
           textualSuggestions.add("ida workout parachute pant");
           textualSuggestions.add("jade yoga jacket");
           textualSuggestions.add("push it messenger bag");
           
            _searchAutocompleteItems.SearchAutocompleteItems();
            List<String> fieldNames = java.util.Arrays.asList("title");
            List<Object> itemSuggestions = Arrays.asList(_searchAutocompleteItems.bxAutocompleteResponse.getBxSearchResponse("").getHitFieldValues(fieldNames, "", true, 0, 10).values().toArray());
            assertEquals(itemSuggestions.size(), 5);
            assertEquals(_searchAutocompleteItems.bxAutocompleteResponse.getTextualSuggestions(), textualSuggestions);

        } catch (Exception ex) {
            Assert.fail("Expected no exception, but got: " + ex.getMessage());
        }
    }
}
