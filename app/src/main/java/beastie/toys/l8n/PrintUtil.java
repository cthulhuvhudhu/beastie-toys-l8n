package beastie.toys.l8n;

public class PrintUtil {

    public static void print(String s) {
        System.out.print(s);
    }

    public static void println(String s) {
        System.out.println(s);
    }

    public static void print(String f, Object...s) {
        System.out.printf(f, s);
    }

    public static void println(String f, Object...s) {
        System.out.printf(f, s);
        print();
    }

    public static void print(Object o){
        System.out.println(o);
    }
}
