package PantryPal.client;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/* 
 * ALL FORMATS ARE ASSUMED TO BE IN FORM
 * [1-2-3-4-5]/[6-7-8-9-10-11-12]
 * Info sent and recieved are in string form only, so no need to use RecipeItems
 */
public class RecipeEncryptor {

    private RecipeEncryptor() {

    }

    // splits strings in form a/b into [a,b]
    public static String[] comboDivider(String comboString){
        return new String[]{ comboString.substring(0, comboString.indexOf("/")), comboString.substring(comboString.indexOf("/") + 1) };
    }
    public static String[] stringSplitter(String str){
        return str.replace("[","").replace("]", "").replace(", ", "-").split("-");
    }
    public static String encryptRecipeInfo(String recipeTitle, String recipeDescription) {
        return encryptSingle(recipeTitle) + "/" + encryptSingle(recipeDescription);
    }

    /*
     * Given a string representing bytes, returns the string represented by the
     * bytes
     * http://localhost:8100/?=[66, 97, 99, 111, 110]
     * [1,2,3,4,5,6]/[1,1,1,1,12,23]
     */
    public static String[] decryptRecipeInfo(String comboString) {
        String[] titleDescCombo = comboDivider(comboString);

        for (int i = 0; i < 2; i++) {
            String[] stringArray = stringSplitter(titleDescCombo[i]);
            byte[] byteArray = new byte[stringArray.length];

            for (int j = 0; j < byteArray.length; j++) {
                byteArray[j] = Byte.parseByte(stringArray[j]);
            }
            titleDescCombo[i] = new String(byteArray);
        }
        return titleDescCombo;
    }

    public static String encryptSingle(String single) {
        //given string array of form [1, 2, 3, 4]
        //return string in form [1-2-3-4]
        return Arrays.toString(single.getBytes()).replace(", ", "-");
    }

    public static String decryptSingle(String single) {

        String[] stringArray = stringSplitter(single);
        byte[] byteArray = new byte[stringArray.length];

        for (int j = 0; j < byteArray.length; j++) {
            byteArray[j] = Byte.parseByte(stringArray[j]);
        }
        return new String(byteArray);
    }
}
