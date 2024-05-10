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
        loadTree();

        var cont = true;

        while (cont) {
            print(Content.menu);

            var opt = InputUtil.get();
            switch (Integer.parseInt(opt)) {
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
                    break;
                default:
                    fm.write(root);
                    InputUtil.close();
                    InputUtil.farewell();
                    cont = false;
            }
        }
    }

    private void printFactsByAnimal() {
        print(Content.requestAnimal);
        var animal = Subject.of(InputUtil.get());
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
                        .map(Subject::of)
                        .map(Subject::getNoun)
                        .sorted()
                        .collect(Collectors.joining("\n - "))
        );
    }

    private void printStats() {
        var str = root.isLeaf() ? root.getData() : Sentence.parseQuestion(root.getData()).toQuestion;
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

    private void loadTree() {
        if (fm.exists()) {
            root = fm.read(Node.class);
        } else {
            populate();
        }
        print(Content.welcomeExpert);
    }

    private void populate() {
        print(Content.welcomeBeginner);
        print(Content.animalPrompt);
        var faveAnimal = Subject.of(InputUtil.get());
        root = new Node(faveAnimal.toString());
    }

    public void startGame() {
        boolean letsPlay = true;

        print(Content.startGame);
        while (letsPlay) {
            print(Content.instructions);
            InputUtil.get();

            var currNode = root;
            Node pNode = null;
            boolean lastAnswer = false;
            while (!currNode.isLeaf()) {
                println(currNode.getData());
                lastAnswer = InputUtil.translateBoolean();
                pNode = currNode;
                currNode = lastAnswer ? currNode.getYes() : currNode.getNo();
            }

            print(Content.guessFormat, currNode.getData());
            var confirm = InputUtil.translateBoolean();
            if (confirm) {
                print(Content.guessCorrect);
            } else {
                var newNode = learn(currNode);
                updateTree(newNode, pNode, lastAnswer);
            }
            println(Content.playAgain);
            letsPlay = InputUtil.translateBoolean(false);
        }
    }

    private void updateTree(Node newNode, Node pNode, boolean lastAnswer) {
        if (pNode == null) {
            root = newNode;
        } else {
            if (lastAnswer) {
                pNode.setYes(newNode);
            } else {
                pNode.setNo(newNode);
            }
        }
    }

    private Sentence getFact(Subject animal1, Subject animal2) {
        print(Content.requestFactFormat, animal1.toString(), animal2.toString());
        return InputUtil.parseSentence();
    }

    private Node learn(Node currNode) {
        print(Content.guessIncorrectThenPrompt);
        var animal1 = Subject.of(currNode.getData());
        var animal2 = Subject.of(InputUtil.get());
        var fact = getFact(animal1, animal2);


        print(Content.learningConfirm, animal2);
        var isAnimal2 = InputUtil.translateBoolean();
        var animal1Fact = fact.toStatementF(animal1.getNoun(), !isAnimal2);
        var animal2Fact = fact.toStatementF(animal2.getNoun(), isAnimal2);
        print(Content.learningSummaryF, animal1Fact, animal2Fact);

        var otherAnimal = new Node(animal2.toString());
        var yesChild = isAnimal2 ? otherAnimal : currNode;
        var noChild = isAnimal2 ? currNode : otherAnimal;

        print(Content.learningQuestionF, fact.toQuestion);
        print(Content.learningDone);
        return new Node(yesChild, noChild, fact.toQuestion);
    }
}
