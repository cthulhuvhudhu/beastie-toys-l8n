package beastie.toys.l8n;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

import static beastie.toys.l8n.PrintUtil.print;
import static beastie.toys.l8n.PrintUtil.println;

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

            if (Arrays.asList(Content.affirmativeOptions).contains(input)) {
                if (print) { print(Content.confirmYes); }
                return true;
            }
            if (Arrays.asList(Content.negativeOptions).contains(input)) {
                if (print) { print(Content.confirmNo); }
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
            print(Content.greetingMorning);
        } else if (now.isAfter(LocalTime.NOON) && now.isBefore(LocalTime.of(6, 0))) {
            print(Content.greetingAfternoon);
        } else {
            print(Content.greetingEvening);
        }
        print();
    }

    public static void farewell() {
        println(Content.farewellOptions[rand.nextInt(Content.farewellOptions.length - 1)]);
    }

    public static void clarifyBoolean() {
        print(Content.clarifyOptions[rand.nextInt(Content.clarifyOptions.length - 1)]);
    }
}
