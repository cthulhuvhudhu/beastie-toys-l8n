package beastie.toys.l8n;

import java.util.EnumSet;

public class Sentence {

    private final VerbDict verb;
    private final String fact;
    public final String toQuestion;

    private Sentence(VerbDict verb, String fact) {
        this.verb = verb;
        this.fact = fact;
        toQuestion = String.format("%s %s?", verb.ptrQuestion, this.fact);
    }

    public String toPtrStatement() { return toPtrStatement(false); }

    public String toPtrStatement(boolean negate) {
        return String.format("%s %s %s.",
                LangUtil.cap(Subject.INDEFINITE_ARTICLE),
                negate ? verb.negation : verb.conjugate,
                fact);
    }

    public String toStatementF(String subject, boolean negate) {
        return String.format("%s %s %s %s.",
                LangUtil.cap(Subject.INDEFINITE_ARTICLE),
                subject,
                negate ? verb.negation : verb.conjugate,
                fact);
    }

    public static Sentence parseQuestion(String question) {
        for (VerbDict verb : EnumSet.allOf(VerbDict.class)) {
            question = question.toLowerCase();
            if (question.toLowerCase().startsWith(verb.ptrQuestion.toLowerCase())) {
                question = question
                        .replace(verb.ptrQuestion.toLowerCase(), "")
                        .replace("?", "");
                return new Sentence(verb, question);
            }
        }
        throw new IllegalArgumentException("Unknown verb in: " + question);
    }

    public static Sentence parseAnswer(String answer) {
        answer = answer.toLowerCase();
        for (VerbDict verb : EnumSet.allOf(VerbDict.class)) {
            if (answer.startsWith(Subject.POINTER + verb.conjugate.toLowerCase())) {
                answer = answer.replace(Subject.POINTER + verb.conjugate.toLowerCase(), "");
                return new Sentence(verb, answer);
            }
        }
        throw new IllegalArgumentException("Unknown verb in: " + answer);
    }
}
