package beastie.toys.l8n.lang;

import static beastie.toys.l8n.App.localeUtil;

public record Sentence(String fact, String verb) {

    public static Sentence fromAnswer(String s) {
        return localeUtil.getFunc("lang.fun.fact.answer.parse").apply(s);
    }

    public static Sentence fromQuestion(String s) {
        return localeUtil.getFunc("lang.fun.fact.question.parse").apply(s);
    }

    public String toStatement(String subject, boolean isTrue) {
        String article = localeUtil.getOp("lang.fun.article.identifier").apply(subject);
        String verbForm = isTrue ? verb : localeUtil.getOp("lang.fun.verb.negate").apply(verb);
        return article + " " + subject + " " + verbForm + " " + fact + ".";
    }

    public String toQuestion() {
        String questionVerb = localeUtil.getOp("lang.fun.verb.question").apply(verb());
        return questionVerb + " " + fact + "?";
    }
}
