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

    public String toPtrStatement(boolean isTrue) {
        return String.format("%s %s%s.",
                LangUtil.cap(Subject.POINTER),
                isTrue ? verb.conjugate : verb.negation,
                fact);
    }

    public String toStatementF(String subject, boolean isTrue) {
        return String.format("%s %s %s %s.",
                LangUtil.cap(Subject.INDEFINITE_ARTICLE),
                subject,
                isTrue ? verb.conjugate : verb.negation,
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
            var expected = String.format("%s %s ", Subject.POINTER.toLowerCase(), verb.conjugate.toLowerCase());
            if (answer.startsWith(expected)) {
                answer = answer.replace(expected, "");
                return new Sentence(verb, answer);
            }
        }
        throw new IllegalArgumentException("Unknown verb in: " + answer);
    }
}
