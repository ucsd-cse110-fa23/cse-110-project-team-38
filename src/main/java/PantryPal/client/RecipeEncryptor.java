package PantryPal.client;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/* 
 * ALL FORMATS ARE ASSUMED TO BE IN FORM
 * [1-2-3-4-5]/[6-7-8-9-10-11-12]
 * Info sent and recieved are in string form only, so no need to use RecipeItems
 */
public class RecipeEncryptor {

    /*
     * private because static class
     */
    private RecipeEncryptor() {

    }

    /*
     * given string in form [1-2-3]/[4-5-6] splits into [[1-2-3], [4-5-6]]
     */
    public static String[] comboDivider(String comboString) {
        return new String[] { comboString.substring(0, comboString.indexOf("/")),
                comboString.substring(comboString.indexOf("/") + 1) };
    }

    /*
     * given string "[1-2-3-4-5]"" splits into string[] = [1,2,3,4,5]
     */
    public static String[] stringSplitter(String str) {
        return str.replace("[", "").replace("]", "").replace(", ", "-").split("-");
    }

    /*
     * returns encrypted recipe title and description as one string
     */
    public static String encryptRecipeInfo(String recipeTitle, String recipeDescription) {
        return encryptSingle(recipeTitle) + "/" + encryptSingle(recipeDescription);
    }

    /*
     * Given a string "[1-2-3]/[4-5-6]" representing bytes of a string
     * returns a string[2] each string is translated from the bytes
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

    /*
     * encrypts a single string as [1-2-3]
     */
    public static String encryptSingle(String single) {
        // given string array of form [1, 2, 3, 4]
        // return string in form [1-2-3-4]
        return Arrays.toString(single.getBytes()).replace(", ", "-");
    }

    /*
     * decrypts a single string from the bytes the string represents
     */
    public static String decryptSingle(String single) {

        String[] stringArray = stringSplitter(single);
        byte[] byteArray = new byte[stringArray.length];

        for (int j = 0; j < byteArray.length; j++) {
            byteArray[j] = Byte.parseByte(stringArray[j]);
        }
        return new String(byteArray);
    }
}
