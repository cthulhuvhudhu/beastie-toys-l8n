package resource;

import beastie.toys.l8n.lang.Sentence;

import java.util.Arrays;
import java.util.ListResourceBundle;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import static resource.VerbDict_en.getVerbDict;
import static resource.VerbDict_en.matcher;

public class App extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {
        return new Object[][] {
            {"affirmatives", new String[] {"y", "yes", "ya", "affirmative", "you got it",
                "indeed", "you bet", "correct", "exactly", "yeah", "yep", "sure", "right", "you said it"}},
            {"animal.fact.header", "Facts about the %s\n"},
            {"animal.fact.header.none", "No facts about the %s\n"},
            {"animal.list.header", "Here are the animals I know:\n"},
            {"animal.prompt", "Which animal do you like most?\n"},
            {"animal.request", "Enter the animal:\n"},
            {"animal.request.error", """
                The examples of a statement:
                 - It can fly
                 - It has horn
                 - It is a mammal
                """.trim()},
            {"animal.fact.request.format", """
                Specify a fact that distinguishes %s from %s.
                The sentence should be of the format: 'It can/has/is ...'
                """.trim()},
            {"answer.clarifications", new String[] {
                    "I'm not sure I caught you: was it yes or no?\n",
                    "Funny, I still don't understand, is it yes or no?\n",
                    "Oh, it's too complicated for me: just tell me yes or no.\n",
                    "Could you please simply say yes or no?\n",
                    "Oh, no, don't try to confuse me: say yes or no.\n"
            }},
            {"answer.confirm.no", "You answered: No\n"},
            {"answer.confirm.yes", "You answered: Yes\n"},
            {"farewells", new String[]{"Bye!\n", "See you later\n", "See you soon\n", "Take it easy\n"}},
            {"greeting.afternoon", "Good afternoon\n"},
            {"greeting.evening", "Good evening\n"},
            {"greeting.morning", "Good morning\n"},
            {"guess.correct", "I guessed correctly! I win.\n"},
            {"guess.format", "Is it %s?\n"},
            {"guess.incorrect.prompt", "I give up. What animal do you have in mind?\n"},
            {"instructions", "You think of an animal, and I guess it.\nPress enter when you're ready.\n"},
            {"lang.article.definite", "the"},
            {"lang.article.default", "a"},
            {"lang.article.all", new String[]{"an", "the", "a"}},
            {"lang.article.object", "it"},
            {"lang.fun.article.identifier", (UnaryOperator<String>) s -> {
                var qualifyingNoun = Arrays.stream(s.split(" ")).reduce((a, b) -> b).orElse("");
                return ("aeioux".indexOf(qualifyingNoun.charAt(0)) < 0) ? "a" : "an";
            }},
            {"lang.fun.fact.question.parse", (Function<String, Sentence>) s -> {
                if (s.startsWith("Can it")) {
                    return new Sentence(s.replace("Can it ", "").replace("?", ""), "can");
                } else if (s.startsWith("Does it have")) {
                    return new Sentence(s.replace("Does it have ", "").replace("?", ""), "has");
                } else if (s.startsWith("Is it")) {
                    return new Sentence(s.replace("Is it ", "").replace("?", ""), "is");
                } else {
                    throw new IllegalArgumentException("Unknown question type: " + s);
                }
            }},
            {"lang.fun.fact.answer.parse", (Function<String, Sentence>) s -> {
                s = s.toLowerCase();
                var verb = matcher(s);
                if (verb == null) {
                    throw new IllegalArgumentException("Unknown verb: " + s);
                }
                return new Sentence(s.replace("it " + verb.conjugate + " ", ""), verb.conjugate);
            }},
            {"lang.fun.verb.question", (UnaryOperator<String>) verb ->
                Optional.ofNullable(getVerbDict(verb)).map(v -> v.ptrQuestion).orElse(null)
            },
            {"lang.fun.verb.negate", (UnaryOperator<String>) verb ->
                Optional.ofNullable(getVerbDict(verb)).map(v -> v.negation).orElse(null)
            },
            {"learn.confirm", "Is the statement correct for %s?\n"},
            {"learn.done", "Nice! I've learned so much about animals!\n"},
            {"learn.questions.format", "I can distinguish these animals by asking the question:\n - %s\n"},
            {"learn.summary.format", "I have learned the following facts about animals:\n - %s\n - %s\n"},
            {"menu", """
                What do you want to do:
    
                1. Play the guessing game
                2. List of all animals
                3. Search for an animal
                4. Calculate statistics
                5. Print the Knowledge Tree
                0. Exit
                """.trim()},
            {"negatives", new String[]{"n", "no", "no way", "nah", "nope", "negative",
                "i don't think so", "yeah no"}},
            {"play.again","Would you like to play again?"},
            {"play.start", "Let's play a game!\n"},
            {"stats.format", """
                The Knowledge Tree stats
                - root node                    %s
                - total number of nodes        %d
                - total number of animals      %d
                - total number of statements   %d
                - height of the tree           %d
                - minimum animal's depth       %d
                - average animal's depth       %.1f
                """.trim()},
            {"welcome.new", "I want to learn about animals.\n"},
            {"welcome.ready", "Welcome to the animal expert system!\n"},
        };
    }
}
