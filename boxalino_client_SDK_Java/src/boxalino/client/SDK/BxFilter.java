/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boxalino.client.SDK;

import com.boxalino.p13n.api.thrift.Filter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author HASHIR
 */
public class BxFilter {

    protected String fieldName;
    protected ArrayList<String> values;
    protected boolean negative;
    protected String hierarchyId = Helper.Common.EMPTY_STRING;
    protected ArrayList<String> hierarchy = new ArrayList<>();
    protected String rangeFrom = Helper.Common.EMPTY_STRING;
    protected String rangeTo = Helper.Common.EMPTY_STRING;

    public BxFilter(String fieldName, ArrayList<String> values, boolean negative) {
        //default start
        negative = false;
        //default end

        this.fieldName = fieldName;
        this.values = values;
        this.negative = negative;
    }

    public Filter getThriftFilter() {
        Filter filter = new Filter();
        filter.fieldName = this.fieldName;
        filter.negative = this.negative;
        filter.stringValues = this.values;
        if (this.hierarchyId != null) {
            filter.hierarchyId = this.hierarchyId;
        }
        if (this.hierarchy != null) {
            filter.hierarchy = this.hierarchy;
        }
        if (this.rangeFrom != null) {
            filter.rangeFrom = this.rangeFrom;
        }
        if (this.rangeTo != null) {
            filter.rangeTo = this.rangeTo;
        }
        return filter;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public List<String> getValues() {
        return this.values;
    }

    public boolean isNegative() {
        return this.negative;
    }

    public String getHierarchyId() {
        return this.hierarchyId;
    }

    public void setHierarchyId(String hierarchyId) {
        this.hierarchyId = hierarchyId;
    }

    public List<String> getHierarchy() {
        return this.hierarchy;
    }

    public void setHierarchy(ArrayList<String> hierarchy) {
        this.hierarchy = hierarchy;
    }

    public String getRangeFrom() {
        return this.rangeFrom;
    }

    public void setRangeFrom(String rangeFrom) {
        this.rangeFrom = rangeFrom;
    }

    public String getRangeTo() {
        return this.rangeTo;
    }

    public void setRangeTo(String rangeTo) {
        this.rangeTo = rangeTo;
    }
    
    

}
