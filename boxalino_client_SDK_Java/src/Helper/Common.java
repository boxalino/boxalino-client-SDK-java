/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Scanner;

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
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

    public static void unlink(String fileName) {
        if (new File(fileName).exists()) {
            new File(fileName).delete();
        }
    }

    public static String file_get_contents(String path) throws FileNotFoundException {
        String response = null;
        try {
            response = new Scanner(new URL(path).openStream(), "UTF-8").useDelimiter("\\A").next();
        } catch (MalformedURLException ex) {
        } catch (IOException ex) {
        }
        return response;
    }

    public static <T> Iterable<T> emptyIfNull(Iterable<T> iterable) {
        return iterable == null ? Collections.<T>emptyList() : iterable;
    }

    

}
