package beastie.toys.l8n;

import static beastie.toys.l8n.App.localeUtil;

public record Sentence(String fact, String verb) {

    public static Sentence fromAnswer(String s) {
        return localeUtil.getFunc("lang.fun.fact.answer.parse").apply(s);
    }

    public static Sentence fromQuestion(String s) {
        return localeUtil.getFunc("lang.fun.fact.question.parse").apply(s);
    }

    public String toStatement(String subject, boolean isTrue) {
        return String.format("%s %s %s %s.",
            localeUtil.getOp("lang.fun.article.identifier").apply(subject),
            subject,
            isTrue ? verb : localeUtil.getOp("lang.fun.verb.negate").apply(verb),
            fact
        );
    }

    public String toQuestion() {
        var questionVerb = localeUtil.getOp("lang.fun.verb.question").apply(verb());
        return String.format("%s %s?", questionVerb, fact);
    }
}
