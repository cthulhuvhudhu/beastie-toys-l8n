package beastie.toys.l8n;

import com.google.common.base.Objects;

import java.util.Arrays;

public class Subject {
    private final String article;
    private String noun;
    public static final String DEFAULT_ARTICLE = "a";
    public static final String INDEFINITE_ARTICLE = "the";
    public static final String VOWEL_ARTICLE = "an";
    public static final String POINTER = "it";
    public static final String[] ARTICLES = new String[]{VOWEL_ARTICLE, INDEFINITE_ARTICLE, DEFAULT_ARTICLE};

    private Subject(String s) {
        var arg = Arrays.stream(s.split(" ")).toList();
        this.noun = s;

        if (arg.size() > 1) {
            if (DEFAULT_ARTICLE.equals(arg.get(0))) {
                this.article = DEFAULT_ARTICLE;
                this.noun = String.join(" ", arg.stream().skip(1).toList());
                return;
            } else if (Arrays.asList(ARTICLES).contains(arg.get(0))) {
                this.noun = String.join(" ", arg.stream().skip(1).toList());
            }
        }
        this.article = resolveArticle(this.noun);
    }

    public static Subject of(String s) {
        return new Subject(s);
    }

    public Subject(String article, String noun) {
        this.article = article;
        this.noun = noun;
    }

    public String getArticle() {
        return article;
    }

    public String getNoun() {
        return noun;
    }

    private String resolveArticle(String s) {
        var qualifyingNoun =  Arrays.stream(s.split(" ")).reduce((a, b) -> b).orElse("");
        return "aeioux".indexOf(qualifyingNoun.charAt(0)) < 0 ? DEFAULT_ARTICLE : VOWEL_ARTICLE;
    }

    public String toString() {
        if (article == null || article.isEmpty()) {
            return noun;
        }
        return String.format("%s %s", article, noun);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        return Objects.equal(article, subject.article) && Objects.equal(noun, subject.noun);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(article, noun);
    }
}