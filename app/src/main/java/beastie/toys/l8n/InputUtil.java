package beastie.toys.l8n;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

import static beastie.toys.l8n.PrintUtil.print;
import static beastie.toys.l8n.PrintUtil.println;

public class InputUtil {

    public static final Random rand = new Random();
    public static final LocalTime MORNING_TIME = LocalTime.of(5, 0);
    public static final LocalTime EVENING_TIME = LocalTime.of(18, 0);

    private static final Scanner scanIn = new Scanner(System.in);

    public static String get() {
        var input = scanIn.nextLine();
        return sanitize(input);
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

    public static Sentence parseSentence() {
        while (true) {
            var input = InputUtil.get();
            try {
                return Sentence.parseAnswer(input);
            } catch (IllegalArgumentException e) {
                print(Content.requestFactError);
            }
        }
    }

    public static void greet() {
        var now = LocalTime.now();
        if (now.isBefore(LocalTime.NOON) && now.isAfter(MORNING_TIME)) {
            println(Content.greetingMorning);
        } else if (now.isAfter(LocalTime.NOON) && now.isBefore(EVENING_TIME)) {
            println(Content.greetingAfternoon);
        } else {
            println(Content.greetingEvening);
        }
    }

    public static void farewell() {
        println(Content.farewellOptions[rand.nextInt(Content.farewellOptions.length - 1)]);
    }

    public static void clarifyBoolean() {
        print(Content.clarifyOptions[rand.nextInt(Content.clarifyOptions.length - 1)]);
    }

    public static void close() {
        scanIn.close();
    }
}
