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
                var fact = Sentence.parseQuestion(firstFact.getData());
                var truth = firstFact.getYes().getData().contains(lastData);
                facts.push(fact.toPtrStatement(truth));
                lastData = firstFact.getData();
                firstFact = firstFact.getParent();
            }
            while (!facts.isEmpty()) {
                println(facts.pop());
            }
        }
    }

    private void printAnimals() {
        print(Content.animalList);
        println(" - %s\n",
                root.leaves().stream()
                        .map(Node::getData)
                        .map(Subject::new)
                        .map(Subject::getNoun)
                        .sorted()
                        .collect(Collectors.joining("\n - "))
        );
    }

    private void printStats() {
        var str = root.isLeaf() ? root.getData() : Sentence.parseQuestion(root.getData()).toPtrStatement();
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
                var animal2 = new Subject(InputUtil.get());
                var animal1 = new Subject(currNode.getData());

                print(Content.requestFactFormat, animal1.toString(), animal2.toString());
                var fact = InputUtil.parseSentence();
                print(Content.learningConfirm, animal2);
                var isAnimal2 = InputUtil.translateBoolean();
                var animal1Fact = fact.toStatementF(animal1.getNoun(), !isAnimal2);
                var animal2Fact = fact.toStatementF(animal2.getNoun(), isAnimal2);
                print(Content.learningSummaryF, animal1Fact, animal2Fact);

                var otherAnimal = new Node(animal2.toString());
                var yesChild = isAnimal2 ? otherAnimal : currNode;
                var noChild = isAnimal2 ? currNode : otherAnimal;
                var newNode = new Node(yesChild, noChild, fact.toQuestion);
                if (pNode == null) {
                    root = newNode;
                } else {
                    if (lastAnswer) {
                        pNode.setYes(newNode);
                    } else {
                        pNode.setNo(newNode);
                    }
                }

                print(Content.learningQuestionF, fact.toQuestion);
                print(Content.learningDone);
            }
            print(Content.playAgain);
            letsPlay = InputUtil.translateBoolean(false);
        }
    }
}
