package resource;

import java.util.Arrays;

public enum VerbDict_en {
    IDENTITY("is", "isn't", "Is it", "it is"),
    POSSESSION("has", "doesn't have", "Does it have", "it has"),
    ABILITY("can", "can't", "Can it", "it can");

    final String conjugate;
    final String negation;
    final String ptrQuestion;
    final String statement;

    VerbDict_en(String conjugate, String negation, String ptrQuestion, String statement) {
        this.conjugate = conjugate;
        this.negation = negation;
        this.ptrQuestion = ptrQuestion;
        this.statement = statement;
    }

    public static VerbDict_en getVerbDict(String verb) {
        return Arrays.stream(VerbDict_en.values())
                .filter(v -> v.conjugate.equals(verb))
                .findFirst()
                .orElse(null);
    }

    public static VerbDict_en matcher(String sentence) {
        return Arrays.stream(VerbDict_en.values())
                .filter(v -> sentence.contains(v.conjugate))
                .findFirst()
                .orElse(null);
    }
}
