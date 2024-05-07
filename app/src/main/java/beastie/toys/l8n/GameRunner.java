package beastie.toys.l8n;

import beastie.toys.l8n.tree.Node;

import java.util.Stack;
import java.util.stream.Collectors;

import static beastie.toys.l8n.PrintUtil.print;

public class GameRunner {

    private Node root;

    public void run() {
        InputUtil.greet();
        populate();

        var cont = true;

        while (cont) {
            print(localeUtil.get(MENU.name()));

            switch (Integer.parseInt(InputUtil.get())) {
                case 1:
                    startGame();
                    break;
                case 2:
                    printAnimals();
                    break;
                case 3:
                    printFactsByAnimal();
                    break;
                case 4:
                    printStats();
                    break;
                case 5:
                    print(root);
                default:
                    fm.write(root);
                    farewell();
                    cont = false;
            }
        }
    }

    private void printFactsByAnimal() {
        print(localeUtil.get(REQUEST_ANIMAL.name()));
        var animal = new Subject(InputService.get());
        var firstFact = root.find(animal.getNoun());
        var lastData = animal.toString();
        if (firstFact == null) {
            print(localeUtil.get(NO_FACTS.name()), animal.getNoun());
        } else {
            print(localeUtil.get(FACTS.name()), animal.getNoun());
            var facts = new Stack<String>();
            while (firstFact != null) {
                var fact = Fact.fromQuestion(firstFact.getData());
                if (fact != null) {
                    var truth = firstFact.getYes().getData().contains(lastData);
                    var it = localeUtil.get(LocalizationLang.IT.name());
                    var negate = localeUtil.getOp(LocalizationVerbs.VERB_TO_NEGATE.name()).apply(fact.verb());
                    var result = String.format(" - %s %s %s.", it, truth ? fact.verb() : negate, fact.fact());
                    facts.push(result);
                }
                lastData = firstFact.getData();
                firstFact = firstFact.getParent();
            }
            while (!facts.isEmpty()) {
                print(facts.pop());
                print();
            }
        }
        print();
    }

    private void printAnimals() {
        print(localeUtil.get(ANIMAL_LIST.name()));
        print(" - %s\n",
                root.leaves().stream()
                        .map(Node::getData)
                        .map(Subject::new)
                        .map(Subject::getNoun)
                        .sorted()
                        .collect(Collectors.joining("\n - "))
        );
        print();
    }

    private void printStats() {
        var it = localeUtil.get(LocalizationLang.IT.name());
        var str = root.isLeaf() ? root.getData() : String.format("%s %s %s", it, Fact.fromQuestion(root.getData()).verb(), Fact.fromQuestion(root.getData()).fact());
        print(localeUtil.get(KNOWLEDGE_TREE_STATS.name()),
                str,
                root.count(),
                (long) root.leaves().size(),
                root.count() - root.leaves().size(),
                root.maxDepth(),
                root.leaves().stream().mapToInt(Node::getDepth).min().orElse(0),
                root.leaves().stream().mapToInt(Node::getDepth).average().orElse(0)
        );
        print();
    }

    private void populate() {
        if (fm.exists()) {
            root = fm.read(Node.class);
        } else {
            print(localeUtil.get(NO_KNOWLEDGE.name()));
            print(localeUtil.get(FAVE_ANIMAL.name()));
            var faveAnimal = new Subject(InputService.get());
            root = new Node(faveAnimal.toString());
        }
        print(localeUtil.get(HAVE_KNOWLEDGE.name()));
    }

    public void startGame() {
        boolean letsPlay = true;

//        print(localeUtil.get(START_GAME_SESSION.name()));
        while (letsPlay) {
            print(localeUtil.get(INSTRUCTIONS.name()));
            InputService.get();

            // Traverse existing knowledge tree
            var currNode = root;
            Node pNode = null;
            boolean lastAnswer = false;
            while (!currNode.isLeaf()) {
                print(currNode.getData());
                lastAnswer = TranslationService.translateBoolean();
                pNode = currNode;
                currNode = lastAnswer ? currNode.getYes() : currNode.getNo();
            }

            print(localeUtil.get(GUESS_F.name()), currNode.getData());
            var confirm = TranslationService.translateBoolean();
            if (confirm) {
                print(localeUtil.get(CORRECT.name()));
            } else {
                print(localeUtil.get(INCORRECT_PROMPT.name()));
                var animalActual = new Subject(InputService.get());
                var faveAnimal = new Subject(currNode.getData());
                var fact = TranslationService.getFact(faveAnimal, animalActual);
                print(localeUtil.get(CONFIRM_FACT_F.name()), animalActual);
                var isAnimal2 = TranslationService.translateBoolean();
                var negate = localeUtil.getOp(LocalizationVerbs.VERB_TO_NEGATE.name()).apply(fact.verb());
                print(localeUtil.get(LEARNING_F.name()),
                        faveAnimal.getNoun(),
                        isAnimal2 ? negate : fact.verb(),
                        fact.fact(),
                        animalActual.getNoun(),
                        isAnimal2 ? fact.verb() : negate,
                        fact.fact());

                var otherAnimal = new Node(animalActual.toString());
                var yesChild = isAnimal2 ? otherAnimal : currNode;
                var noChild = isAnimal2 ? currNode : otherAnimal;
                var questionVerb = localeUtil.getOp(LocalizationVerbs.VERB_TO_QUESTION.name()).apply(fact.verb());
                var question = String.format("%s %s?", questionVerb, fact.fact());
                var newNode = new Node(yesChild, noChild, question, pNode == null ? 0 : pNode.getDepth() + 1);
                if (pNode == null) {
                    root = newNode;
                } else {
                    if (lastAnswer) {
                        pNode.setYes(newNode);
                    } else {
                        pNode.setNo(newNode);
                    }
                }

                print(localeUtil.get(MADE_QUESTION.name()), question);
                print(localeUtil.get(LEARNED.name()));
            }
            print(localeUtil.get(PLAY_AGAIN.name()));
            letsPlay = TranslationService.translateBoolean(false);
        }
    }
}
