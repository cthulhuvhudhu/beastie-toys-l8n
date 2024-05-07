/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package beastie.toys.l8n;

import java.util.Arrays;

public class App {

    public static FileManager fm;

    public static void main(String[] args) {

//        public static LocalizationUtil localeUtil;
//        public static VerbUtil verbUtil;

        fm = FileManager.getInstance(
                Arrays.stream(args)
                        .dropWhile(a -> !"-type".equals(a))
                        .limit(2)
                        .skip(1)
                        .findFirst()
                        .orElse("json")
        );


//            localeUtil = LocalizationUtil.getInstance(ResourceBundle.getBundle("main.resource.App"));
//            verbUtil = VerbUtil.getInstance();
        new GameRunner().run();
        }
    }
}
