/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helper;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author HASHIR
 */
public  class Shift<T> {    
    public  List<T> array_shift(List<T> coll){        
        List<T> list = new ArrayList<T>();
            list.add(coll.get(0));
            coll.remove(0); 
       return  list;
   }
    
}
