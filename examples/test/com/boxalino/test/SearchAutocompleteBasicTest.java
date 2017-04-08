/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boxalino.test;

import com.boxalino.examples.SearchAutocompleteBasic;
import com.sun.xml.rpc.processor.modeler.j2ee.xml.string;
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
public class SearchAutocompleteBasicTest {

    private String account = "boxalino_automated_tests";
    private String password = "boxalino_automated_tests";

    public SearchAutocompleteBasicTest() {
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
    public void testFrontendSearchAutocompleteBasic() {
        SearchAutocompleteBasic _searchAutocompleteBasic = new SearchAutocompleteBasic();
        try {
            _searchAutocompleteBasic.account = this.account;
            _searchAutocompleteBasic.password = this.password;
            _searchAutocompleteBasic.print = false;
            String[] textualSuggestions = {"ida workout parachute pant","jade yoga jacket","push it messenger bag"};
            _searchAutocompleteBasic.searchAutocompleteBasic();
            assertEquals(_searchAutocompleteBasic.bxAutocompleteResponse.getTextualSuggestions().toArray(new String[0]), textualSuggestions);
        } catch (Exception ex) {
            Assert.fail("Expected no exception, but got: " + ex.getMessage());
        }
    }
}
