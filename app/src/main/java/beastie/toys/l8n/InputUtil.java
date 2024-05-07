package beastie.toys.l8n;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

import static beastie.toys.l8n.PrintUtil.print;

public class InputUtil {

    public static final Random rand = new Random();

    public static String get() {
        try(Scanner scan = new Scanner(System.in)) {
            return sanitize(scan.nextLine());
        }
    }

    private static String sanitize(String s) {
        var result = s.toLowerCase().trim();
        if (!s.isEmpty() && ".!".indexOf(s.charAt(s.length() - 1)) >= 0) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    public static boolean translateBoolean() {
        return translateBoolean(true);
    }

    public static boolean translateBoolean(boolean print) {
        while (true) {
            var input = get();
            var affirmatives = Arrays.asList(localeUtil.getArr(Localization.AFFIRMATIVES.name()));
            var negatives = Arrays.asList(localeUtil.getArr(Localization.NEGATIVES.name()));

            if (affirmatives.contains(input)) {
                if (print) { print(localeUtil.get(Localization.CONFIRM_YES.name())); }
                return true;
            }
            if (negatives.contains(input)) {
                if (print) { print(localeUtil.get(Localization.CONFIRM_NO.name())); }
                return false;
            }
            clarifyBoolean();
        }
    }

    public static Fact getFact(Subject animal1, Subject animal2) {
        while (true) {
            print(localeUtil.get(Localization.REQUEST_FACT_F.name()),
                    animal1.toString(), animal2.toString());

            var input = InputService.get();
            var fact = Fact.fromAnswer(input);
            if (fact != null) { return fact; }
            print(localeUtil.get(Localization.FACT_FORMAT_ERR.name()));
        }
    }

    public static void greet() {
        var now = LocalTime.now();
        if (now.isBefore(LocalTime.NOON) && now.isAfter(LocalTime.of(5, 0))) {
            print(localeUtil.get(Localization.GREETING_MORNING.name()));
        } else if (now.isAfter(LocalTime.NOON) && now.isBefore(LocalTime.of(6, 0))) {
            print(localeUtil.get(Localization.GREETING_AFTERNOON.name()));
        } else {
            print(localeUtil.get(Localization.GREETING_EVENING.name()));
        }
        print();
    }

    public static void farewell() {
        var farewells = localeUtil.getArr(Localization.FAREWELLS.name());
        print(farewells[rand.nextInt(farewells.length - 1)]);
        print();
    }

    public static void clarifyBoolean() {
        var clarify = localeUtil.getArr(Localization.CLARIFY.name());
        print(clarify[rand.nextInt(clarify.length - 1)]);
    }
}
