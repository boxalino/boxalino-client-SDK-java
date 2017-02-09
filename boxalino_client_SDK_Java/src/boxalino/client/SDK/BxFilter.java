/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boxalino.client.SDK;

import com.boxalino.p13n.api.thrift.Filter;
import java.util.ArrayList;

/**
 *
 * @author HASHIR
 */
public class BxFilter {

    protected String fieldName;
    protected ArrayList<String> values;
    protected boolean negative;
    protected String hierarchyId = Helper.Common.EMPTY_STRING;
    protected ArrayList<String> hierarchy = new ArrayList<String>();
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
}
