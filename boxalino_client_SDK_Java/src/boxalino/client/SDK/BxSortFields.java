/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boxalino.client.SDK;

import com.boxalino.p13n.api.thrift.SortField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author HASHIR
 */
public class BxSortFields {

    private Map<String, Boolean> sorts;

    public BxSortFields() {
        sorts = new HashMap<>();
    }

    void push(String field, boolean reverse) {
        this.sorts.put(field, reverse);
    }

    public BxSortFields(String field, boolean reverse) {
        //default value start
        sorts = new HashMap<>();

        if (field == null || field.isEmpty()) {
            field = null;
        }
        //default value end

        if (field != null) {
            this.push(field, reverse);
        }
    }

    public Map<String, Boolean> getSortFields() {
        return this.sorts;
    }

    public boolean isFieldReverse(String field) {
        return this.sorts != null && this.sorts.get(field) != null;
    }

    public ArrayList<SortField> getThriftSortFields() {
        ArrayList<SortField> sortFields = new ArrayList<>();
        this.getSortFields().entrySet().forEach((field) -> {
            SortField objSortFields = new SortField();
            objSortFields.fieldName = field.getKey();
            objSortFields.reverse = this.isFieldReverse(field.getKey());
        });
        return sortFields;
    }
}
