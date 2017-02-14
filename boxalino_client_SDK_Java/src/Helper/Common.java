/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
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

    public static String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }
    
     public static void unlink(String fileName)
        {
            if ( new File(fileName).exists())
            {
                new File(fileName).delete();
            }
        }
     
     public static String file_get_contents(String path) throws FileNotFoundException{
         return( new Scanner(new File(path)).useDelimiter("\\Z").next());
     }
     
}
