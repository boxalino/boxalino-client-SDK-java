/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boxalino.client.SDK;


import com.boxalino.p13n.api.thrift.FacetRequest;
import com.boxalino.p13n.api.thrift.FacetSortOrder;
import com.boxalino.p13n.api.thrift.Filter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author HASHIR
 */
public class BxFacets {

    private HashMap<String, Filter> filters = new HashMap<String, Filter>();
    
    public HashMap<String, Filter> getFilters() {
        return this.filters;
    }
    
    public ArrayList<FacetRequest> getThriftFacets()
        {

            ArrayList<FacetRequest> thriftFacets = new ArrayList<FacetRequest>();
            

            return thriftFacets;
        }
    
}
