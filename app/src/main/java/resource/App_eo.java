package resource;

import beastie.toys.l8n.Sentence;

import java.util.Arrays;
import java.util.ListResourceBundle;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class App_eo extends ListResourceBundle {
    @Override
    protected Object[][] getContents() {
        return new Object[][]{
                {"affirmatives", new String[]{"j", "jes", "ja", "jesa", "vi ricevis ĝin",
                        "vi vetas", "ĝusta", "ĝuste", "certe", "prave", "vi diris ĝin"}},
                {"animal.fact.header", "Faktoj pri la %s\n"},
                {"animal.fact.header.none", "Neniuj faktoj pri la %s\n"},
                {"animal.list.header", "Jen la bestoj, kiujn mi konas:\n"},
                {"animal.prompt", "Kiun beston vi plej ŝatas?\n"},
                {"animal.request", "Enigu la besto\n"},
                {"animal.request.error", """
                    Ekzemploj de deklaro:
                      - Ĝi povas flugi
                      - Ĝi havas kornon
                      - Ĝi estas mamulo
                    """.trim()},
                {"animal.fact.request.format", """
                    Indiku fakton, kiu distingas %s de %s.
                    La frazo devus esti de la formato: 'Ĝi povas/havas/estas...'
                    """.trim()},
                {"answer.clarifications", new String[]{
                        "Mi ne certas, ke mi kaptis vin: ĉu jes aŭ ne?\n",
                        "Amuza, mi ankoraŭ ne komprenas, ĉu jes aŭ ne?\n",
                        "Ho, ĝi estas tro komplika por mi: nur diru al mi jes aŭ ne.\n",
                        "Ĉu vi povus simple diri jes aŭ ne?\n",
                        "Ho, ne, ne provu konfuzi min: diru jes aŭ ne.\n"
                }},
                {"answer.confirm.no", "Vi respondis: Ne\n"},
                {"answer.confirm.yes", "Vi respondis: Jes\n"},
                {"farewells", new String[]{
                        "Ĝis poste\n", "Ĝis baldaŭ\n",
                        "Ĝis!\n", "Ĝis revido!\n", "Estis agrable vidi vin!\n"
                }},
                {"greeting.afternoon", "Bonan posttagmezon\n"},
                {"greeting.evening", "Bonan vesperon\n"},
                {"greeting.morning", "Bonan matenon\n"},
                {"guess.correct", "Mi ĝuste divenis! mi gajnas.\n"},
                {"guess.format", "Ĉu ĝi estas %s?\n"},
                {"guess.incorrect.prompt", "Mi rezignas. Kiun beston vi havas en la kapo?\n"},
                {"instructions", "Vi pensu pri besto, kaj mi divenos ĝin.\nPremu enen kiam vi pretas.\n"},
                {"lang.article.definite", "la"},
                {"lang.article.default", ""},
                {"lang.article.all", new String[]{"la"}},
                {"lang.article.object", "ĝi"},
                {"lang.fun.article.identifier", (UnaryOperator<String>) noun -> ""},
                {"lang.fun.fact.question.parse", (Function<String, Sentence>) s -> {
                    s = s.toLowerCase();
                    var arr = Arrays.stream(s.split(" ")).toList();
                    if (arr.size() < 3 || !s.startsWith("ĉu ĝi")) {
                        return null;
                    }
                    return new Sentence(arr.stream().skip(3).collect(Collectors.joining(" ")), arr.get(2));
                }},
                {"lang.fun.fact.answer.parse", (Function<String, Sentence>) s -> {
                    s = s.toLowerCase();
                    var arr = Arrays.stream(s.split(" ")).toList();
                    if (arr.size() < 2 || !"ĝi".equalsIgnoreCase(arr.get(0))) {
                        return null;
                    }
                    return new Sentence(arr.stream().skip(2).collect(Collectors.joining(" ")), arr.get(1));
                }},
                {"lang.fun.verb.question", (UnaryOperator<String>) verb -> String.format("Ĉu ĝi %s", verb) },
                {"lang.fun.verb.negate", (UnaryOperator<String>) verb -> String.format("ne %s", verb) },
                {"learn.confirm", "Ĉu la aserto ĝustas por la %s?\n"},
                {"learn.done", "Bela! Mi multe lernis pri bestoj!\n"},
                {"learn.questions.format", "Mi povas distingi ĉi tiujn bestojn farante la demandon:\n - %s\n"},
                {"learn.summary.format", "Mi lernis la jenajn faktojn pri bestoj:\n - La %s %s %s.\n - La %s %s %s.\n"},
                {"menu", """
                    Kion vi volas fari:
    
                    1. Ludi la divenludon
                    2. Listo de ĉiuj bestoj
                    3. Serĉi beston
                    4. Kalkuli statistikon
                    5. Presu la Scion-Arbon
                    0. Eliri
                    """.trim()},
                {"negatives", new String[]{"n", "ne", "neniel", "negativo",
                        "Mi ne pensas tiel", "jes ne"}},
                {"play.again", "Ĉu vi ŝatus ludi denove?"},
                {"play.start", "Ni ludu ludon!\n"},
                {"stats.format", """
                    La Statistiko de la Scio-Arbo
                     - radika nodo                   %s
                     - tuta nombro de nodoj          %d
                     - tuta nombro de bestoj         %d
                     - totala nombro de deklaroj     %d
                     - alteco de la arbo             %d
                     - minimuma profundo             %d
                     - averaĝa profundo              %.1f
                    """.trim()},
                {"welcome.new", "Mi volas lerni pri bestoj.\n"},
                {"welcome.ready", "Bonvenon al la sperta sistemo de la besto!\n"}
        };
    }
}
