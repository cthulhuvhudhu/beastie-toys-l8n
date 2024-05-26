package beastie.toys.l8n.util;

import beastie.toys.l8n.lang.Sentence;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

import static beastie.toys.l8n.App.localeUtil;
import static beastie.toys.l8n.util.PrintUtil.print;
import static beastie.toys.l8n.util.PrintUtil.println;

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

            if (Arrays.asList(localeUtil.getArr("affirmatives")).contains(input)) {
                if (print) { print(localeUtil.get("answer.confirm.yes")); }
                return true;
            }
            if (Arrays.asList(localeUtil.getArr("negatives")).contains(input)) {
                if (print) { print(localeUtil.get("answer.confirm.no")); }
                return false;
            }
            clarifyBoolean();
        }
    }

    public static Sentence parseSentence() {
        while (true) {
            var input = InputUtil.get();
            try {
                return Sentence.fromAnswer(input);
            } catch (IllegalArgumentException e) {
                print(localeUtil.get("animal.request.error"));
            }
        }
    }

    public static void greet() {
        var now = LocalTime.now();
        if (now.isBefore(LocalTime.NOON) && now.isAfter(MORNING_TIME)) {
            println(localeUtil.get("greeting.morning"));
        } else if (now.isAfter(LocalTime.NOON) && now.isBefore(EVENING_TIME)) {
            println(localeUtil.get("greeting.afternoon"));
        } else {
            println(localeUtil.get("greeting.evening"));
        }
    }

    public static void farewell() {
        var farewells = localeUtil.getArr("farewells");
        println(farewells[rand.nextInt(farewells.length - 1)]);
    }

    public static void clarifyBoolean() {
        var clarifiers = localeUtil.getArr("answer.clarifications");
        print(clarifiers[rand.nextInt(clarifiers.length - 1)]);
    }

    public static void close() {
        scanIn.close();
    }
}
