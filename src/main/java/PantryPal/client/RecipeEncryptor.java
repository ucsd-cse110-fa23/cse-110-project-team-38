package PantryPal.client;

import java.util.Arrays;

/* 
 * Two static methods, encrypt info and decrypt info.
 * Info sent and recieved are in string form only, so no need to use RecipeItems
 */
public class RecipeEncryptor {

    private RecipeEncryptor() {

    }

    public static String encryptRecipeInfo(String recipeTitle, String recipeDescription) {
        byte[] titleBytes = recipeTitle.getBytes();
        byte[] descriptionBytes = recipeDescription.getBytes();
        return Arrays.toString(titleBytes) + "|" + Arrays.toString(descriptionBytes);

    }

    /*
     * Given a string representing bytes, returns the string represented by the
     * bytes
     * 
     * [1,2,3,4,5,6] | [1,1,1,1,12,23]
     */
    public static String[] decryptRecipeInfo(String comboString) {
        String[] titleDescCombo = { comboString.substring(0, comboString.indexOf("|")),
                comboString.substring(comboString.indexOf("|") + 1) };
        // comboString.split("|");

        for (int i = 0; i < 2; i++) {
            String[] stringArray = titleDescCombo[i].replace("[", "")
                    .replace("]", "").split(", ");
            byte[] byteArray = new byte[stringArray.length];

            for (int j = 0; j < byteArray.length; j++) {
                byteArray[j] = Byte.parseByte(stringArray[j]);
            }
            titleDescCombo[i] = new String(byteArray);

        }
        return titleDescCombo;
    }

    public static String encryptSingle(String single) {
        return Arrays.toString(single.getBytes());
    }

    public static String decryptSingle(String single) {

        String[] stringArray = single.replace("[", "")
                .replace("]", "").split(", ");
        byte[] byteArray = new byte[stringArray.length];

        for (int j = 0; j < byteArray.length; j++) {
            byteArray[j] = Byte.parseByte(stringArray[j]);
        }
        return new String(byteArray);
    }
}
