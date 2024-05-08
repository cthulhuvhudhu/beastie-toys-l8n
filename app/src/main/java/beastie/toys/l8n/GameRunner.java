package beastie.toys.l8n;

import beastie.toys.l8n.tree.Node;

import java.util.Stack;
import java.util.stream.Collectors;

import static beastie.toys.l8n.App.fm;
import static beastie.toys.l8n.PrintUtil.print;
import static beastie.toys.l8n.PrintUtil.println;

public class GameRunner {

    private Node root;

    public void run() {
        InputUtil.greet();
        populate();

        var cont = true;

        while (cont) {
            print(Content.menu);

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
                    InputUtil.farewell();
                    cont = false;
            }
        }
    }

    private void printFactsByAnimal() {
        print(Content.requestAnimal);
        var animal = new Subject(InputUtil.get());
        var firstFact = root.find(animal.getNoun());
        var lastData = animal.toString();
        if (firstFact == null) {
            print(Content.animalNoFacts, animal.getNoun());
        } else {
            print(Content.animalFacts, animal.getNoun());
            var facts = new Stack<String>();
            while (firstFact != null) {
                var fact = Fact.fromQuestion(firstFact.getData());
                if (fact != null) {
                    var truth = firstFact.getYes().getData().contains(lastData);
                    var negate = localeUtil.getOp(LocalizationVerbs.VERB_TO_NEGATE.name()).apply(fact.verb());
                    var result = String.format(" - %s %s %s.", Subject.POINTER, truth ? fact.verb() : negate, fact.fact());
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
        print(Content.animalList);
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
        var str = root.isLeaf() ? root.getData() : String.format("%s %s %s", Subject.POINTER, Fact.fromQuestion(root.getData()).verb(), Fact.fromQuestion(root.getData()).fact());
        println(Content.statsFormat,
                str,
                root.count(),
                (long) root.leaves().size(),
                root.count() - root.leaves().size(),
                root.maxDepth(),
                root.leaves().stream().mapToInt(Node::getDepth).min().orElse(0),
                root.leaves().stream().mapToInt(Node::getDepth).average().orElse(0)
        );
    }

    private void populate() {
        if (fm.exists()) {
            root = fm.read(Node.class);
        } else {
            print(Content.welcomeBeginner);
            print(Content.animalPrompt);
            var faveAnimal = new Subject(InputUtil.get());
            root = new Node(faveAnimal.toString());
        }
        print(Content.welcomeExpert);
    }

    public void startGame() {
        boolean letsPlay = true;

        print(Content.startGame);
        while (letsPlay) {
            print(Content.instructions);
            InputUtil.get();

            // Traverse existing knowledge tree
            var currNode = root;
            Node pNode = null;
            boolean lastAnswer = false;
            while (!currNode.isLeaf()) {
                print(currNode.getData());
                lastAnswer = InputUtil.translateBoolean();
                pNode = currNode;
                currNode = lastAnswer ? currNode.getYes() : currNode.getNo();
            }

            print(Content.guessFormat, currNode.getData());
            var confirm = InputUtil.translateBoolean();
            if (confirm) {
                print(Content.guessCorrect);
            } else {
                print(Content.guessIncorrectThenPrompt);
                var animalActual = new Subject(InputUtil.get());
                var faveAnimal = new Subject(currNode.getData());
                var fact = TranslationService.getFact(faveAnimal, animalActual);
                print(Content.learningConfirm, animalActual);
                var isAnimal2 = InputUtil.translateBoolean();
                var negate = localeUtil.getOp(LocalizationVerbs.VERB_TO_NEGATE.name()).apply(fact.verb());
                print(Content.learningSummary,
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

                print(Content.learningQuestion, question);
                print(Content.learningDone);
            }
            print(Content.playAgain);
            letsPlay = InputUtil.translateBoolean(false);
        }
    }
}
