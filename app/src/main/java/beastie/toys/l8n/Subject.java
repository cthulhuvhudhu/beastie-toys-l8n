package beastie.toys.l8n;

import com.google.common.base.Objects;

import java.util.Arrays;

import static beastie.toys.l8n.App.localeUtil;

public class Subject {
    private final String article;
    private String noun;
    public static final String DEFAULT_ARTICLE = localeUtil.get("lang.article.default");
    public static final String[] ARTICLES = localeUtil.getArr("lang.article.all");

    private Subject(String s) {
        var arg = Arrays.stream(s.split(" ")).toList();
        this.noun = s;

        if (arg.size() > 1) {
            if (DEFAULT_ARTICLE.equals(arg.getFirst())) {
                this.article = DEFAULT_ARTICLE;
                this.noun = String.join(" ", arg.stream().skip(1).toList());
                return;
            } else if (Arrays.asList(ARTICLES).contains(arg.getFirst())) {
                this.noun = String.join(" ", arg.stream().skip(1).toList());
            }
        }
        this.article = resolveArticle(this.noun);
    }

    public static Subject of(String s) {
        return new Subject(s);
    }

    public String getNoun() {
        return noun;
    }

    private String resolveArticle(String s) {
        return localeUtil.getOp("lang.fun.article.identifier").apply(s);
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