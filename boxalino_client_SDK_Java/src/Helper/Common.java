/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helper;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import static javafx.scene.input.KeyCode.T;

/**
 *
 * @author HASHIR
 */
public class Common {

    public static final String EMPTY_STRING = "";

    public static int strpos(String haystack, String needle, int offset) {
        return haystack.indexOf(needle, offset);
    }
}
