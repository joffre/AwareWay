package com.pji.de.awareway.utilitaires;

/**
 * Created by Geoffrey on 13/05/2016.
 */
public class AAStringUtils {

    public static String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

    public static String capitalizeFully(final String line) {
        String result = "";
        String[] elements = line.split(" ");
        for(int i = 0; i < elements.length; i++){
            result += capitalize(elements[i]) + ((i != elements.length-1)? " " : "");
        }
        return result;
    }
}
