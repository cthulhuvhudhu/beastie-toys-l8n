package beastie.toys.l8n.util;

import beastie.toys.l8n.lang.Sentence;

import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class LocaleUtil {

    private static ResourceBundle resrc;
    private static LocaleUtil instance;

    public static LocaleUtil getInstance(ResourceBundle rb) {
        if (instance == null) {
            instance = new LocaleUtil(rb);
        }
        return instance;
    }

    private LocaleUtil(ResourceBundle rb) {
        resrc = rb;
    }

    public String get(String s) {
        return resrc.getString(s);
    }

    public Object getObj(String s) {
        return resrc.getObject(s);
    }

    public String[] getArr(String s) {
        return resrc.getStringArray(s);
    }

    @SuppressWarnings("unchecked")
    public UnaryOperator<String> getOp(String s) {
        return ((UnaryOperator<String>) resrc.getObject(s));
    }

    @SuppressWarnings("unchecked")
    public Function<String, Sentence> getFunc(String s) {
        return (Function<String, Sentence>) resrc.getObject(s);
    }
}