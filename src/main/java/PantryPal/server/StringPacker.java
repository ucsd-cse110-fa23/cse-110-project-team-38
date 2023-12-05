package PantryPal.server;

import java.util.Arrays;

public class StringPacker {

    /*
     * given string
     * return string in form [12-24-38-41] representing bytes
     */
    public static String encrypt(String input){
        return Arrays.toString(input.getBytes()).replace(", ","-");
    }

    /*
     * given many strings
     * return in packaged form []/[]/[]/...
     */
    public static String encryptMany(String[] strArr){
        String combo = "";
        for(String str : strArr){
            combo += encrypt(str) + "/";
        }
        return combo.substring(0,combo.length()-1);
    }

    /*
     * given string in form [12-24-38-41] representing bytes
     * return its original string form
     */
    public static String decrypt(String input){
        input = input.replace("[", "").replace("]", "");
        String[] strArr = input.split("-");
        // now have [12,24,38,41]
        byte[] byteArr = new byte[strArr.length];
        
        for (int j = 0; j < byteArr.length; j++) {
            try {
                byteArr[j] = Byte.parseByte(strArr[j]);
            }
            catch (Exception err) {
                continue;
            }
        }
        return new String(byteArr);
    }
    
}
