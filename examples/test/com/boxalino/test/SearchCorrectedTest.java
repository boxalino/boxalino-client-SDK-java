/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boxalino.test;

import com.boxalino.examples.SearchCorrected;
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
public class SearchCorrectedTest {

    private String account = "boxalino_automated_tests";
    private String password = "boxalino_automated_tests";

    public SearchCorrectedTest() {
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
    public void testFrontendSearchCorrected() {
        SearchCorrected _searchCorrected = new SearchCorrected();
        try {
            _searchCorrected.account = this.account;
            _searchCorrected.password = this.password;
            _searchCorrected.print = false;

            String[] hitIds = {"41", "1940", "1065", "1151", "1241", "1321", "1385", "1401", "1609", "1801"};
            _searchCorrected.searchCorrected();
            assertEquals(_searchCorrected.bxResponse.areResultsCorrected("", 0, 10), true);

            assertEquals(_searchCorrected.bxResponse.getHitIds("", true, 0, 10, "id").values().toArray(new String[0]), hitIds);

        } catch (Exception ex) {
            Assert.fail("Expected no exception, but got: " + ex.getMessage());
        }
    }
}
