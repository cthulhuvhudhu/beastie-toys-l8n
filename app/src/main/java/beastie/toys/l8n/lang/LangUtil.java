package beastie.toys.l8n.lang;

public class LangUtil {
    public static String cap(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        } else {
            return Character.toUpperCase(str.charAt(0)) + str.substring(1).toLowerCase();
        }
    }
}
