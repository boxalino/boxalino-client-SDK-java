/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boxalino.test;

import com.boxalino.examples.SearchAutocompleteItemsBundled;
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
public class SearchAutocompleteItemsBundledTest {

    private String account = "boxalino_automated_tests";
    private String password = "boxalino_automated_tests";

    public SearchAutocompleteItemsBundledTest() {
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
    public void testFrontendSearchAutocompleteItemsBundled() {
        SearchAutocompleteItemsBundled _searchAutocompleteItemsBundled = new SearchAutocompleteItemsBundled();
        try {
            _searchAutocompleteItemsBundled.account = this.account;
            _searchAutocompleteItemsBundled.password = this.password;
            _searchAutocompleteItemsBundled.print = false;

            List<String> firstTextualSuggestions = Arrays.asList("ida workout parachute pant", "jade yoga jacket", "push it messenger bag");
            List<String> secondTextualSuggestions = Arrays.asList("argus all weather tank", "jupiter all weather trainer", "livingston all purpose tight");

            _searchAutocompleteItemsBundled.searchAutocompleteItemsBundled();
            assertEquals(_searchAutocompleteItemsBundled.bxAutocompleteResponses.size(), 2);

            //first response
            assertEquals(_searchAutocompleteItemsBundled.bxAutocompleteResponses.get(0).getTextualSuggestions(), firstTextualSuggestions);

            List<String> fieldNames = java.util.Arrays.asList("title");

            List<String> globalIds1 = new ArrayList<String>();
            globalIds1.add("115");
            globalIds1.add("131");
            globalIds1.add("227");
            globalIds1.add("355");
            globalIds1.add("611");

            List<String> globalIds2 = new ArrayList<String>();
            globalIds2.add("1545");

            //global ids
            assertEquals(Arrays.asList(_searchAutocompleteItemsBundled.bxAutocompleteResponses.get(0).getBxSearchResponse("").getHitFieldValues(fieldNames, "", true, 0, 10).keySet().toArray(new String[0])), globalIds1);

            //second response
            assertEquals(_searchAutocompleteItemsBundled.bxAutocompleteResponses.get(1).getTextualSuggestions(), secondTextualSuggestions);

            //global ids
            assertEquals(Arrays.asList(_searchAutocompleteItemsBundled.bxAutocompleteResponses.get(1).getBxSearchResponse("").getHitFieldValues(fieldNames, "", true, 0, 10).keySet().toArray(new String[0])), globalIds2);
        } catch (Exception ex) {
            Assert.fail("Expected no exception, but got: " + ex.getMessage());
        }
    }
}
