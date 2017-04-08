/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boxalino.test;

import com.boxalino.examples.SearchSubPhrases;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author pc
 */
public class SearchSubPhrasesTest {

    private String account = "boxalino_automated_tests";
    private String password = "boxalino_automated_tests";

    public SearchSubPhrasesTest() {
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
    public void hello() {
        SearchSubPhrases _searchSubPhrases = new SearchSubPhrases();
        try {
            _searchSubPhrases.account = this.account;
            _searchSubPhrases.password = this.password;
            _searchSubPhrases.print = false;

            _searchSubPhrases.searchSubPhrases();

            assertTrue(_searchSubPhrases.bxResponse.areThereSubPhrases("", 0));
            assertEquals(_searchSubPhrases.bxResponse.getSubPhrasesQueries("", 0).size(), 2);
        } catch (Exception ex) {
            Assert.fail("Expected no exception, but got: " + ex.getMessage());
        }
    }
}
