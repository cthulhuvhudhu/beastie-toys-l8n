package beastie.toys.l8n;

public enum VerbDict {
    IDENTITY("is", "isn't", "Is it"),
    POSSESSION("has", "doesn't have", "Does it have"),
    ABILITY("can", "can't", "Can it");

    final String conjugate;
    final String negation;
    final String ptrQuestion;

    VerbDict(String conjugate, String negation, String ptrQuestion) {
        this.conjugate = conjugate;
        this.negation = negation;
        this.ptrQuestion = ptrQuestion;
    }
}
