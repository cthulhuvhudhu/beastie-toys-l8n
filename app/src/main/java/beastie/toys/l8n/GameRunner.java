package beastie.toys.l8n;

import beastie.toys.l8n.tree.Node;

import java.util.Stack;
import java.util.stream.Collectors;

import static beastie.toys.l8n.App.fm;
import static beastie.toys.l8n.App.localeUtil;
import static beastie.toys.l8n.PrintUtil.print;
import static beastie.toys.l8n.PrintUtil.println;

public class GameRunner {

    private Node root;

    public void run() {
        InputUtil.greet();
        loadTree();

        var cont = true;

        while (cont) {
            println(localeUtil.get("menu"));

            var opt = InputUtil.get();
            if (opt.isEmpty()) {
                continue;
            }
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
        print(localeUtil.get("animal.request"));
        var animal = Subject.of(InputUtil.get());
        var firstFact = root.find(animal.getNoun());
        var lastData = animal.toString();
        if (firstFact == null) {
            print(localeUtil.get("animal.fact.header.none"), animal.getNoun());
        } else {
            print(localeUtil.get("animal.fact.header"), animal.getNoun());
            var facts = new Stack<String>();
            while (firstFact != null) {
                var fact = Sentence.fromQuestion(firstFact.getData());
                if (fact != null) {
                    var truth = firstFact.getYes().getData().contains(lastData);
                    var it = LangUtil.cap(localeUtil.get("lang.article.object"));
                    var negate = localeUtil.getOp("lang.fun.verb.negate").apply(fact.verb());
                    var result = String.format(" - %s %s %s.", it, truth ? fact.verb() : negate, fact.fact());
                    facts.push(result);
                }
                lastData = firstFact.getData();
                firstFact = firstFact.getParent();
            }
            while (!facts.isEmpty()) {
                println(facts.pop());
            }
        }
    }

    private void printAnimals() {
        print(localeUtil.get("animal.list.header"));
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
        var it = localeUtil.get("lang.article.object");
        var parsed = Sentence.fromQuestion(root.getData());
        var str = root.isLeaf() ? root.getData() :
                String.format("%s %s %s", it, parsed.verb(), parsed.fact());
        println(localeUtil.get("stats.format"),
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

    private void loadTree() {
        if (fm.exists()) {
            root = fm.read(Node.class);
        } else {
            populate();
        }
        print(localeUtil.get("welcome.ready"));
    }

    private void populate() {
        print(localeUtil.get("welcome.new"));
        print(localeUtil.get("animal.prompt"));
        var faveAnimal = Subject.of(InputUtil.get());
        root = new Node(faveAnimal.toString());
    }

    public void startGame() {
        boolean letsPlay = true;

        print(localeUtil.get("play.start"));
        while (letsPlay) {
            print(localeUtil.get("instructions"));
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

            print(localeUtil.get("guess.format"), currNode.getData());
            var confirm = InputUtil.translateBoolean();
            if (confirm) {
                print(localeUtil.get("guess.correct"));
            } else {
                var newNode = learn(currNode);
                updateTree(newNode, pNode, lastAnswer);
            }
            println(localeUtil.get("play.again"));
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
        println(localeUtil.get("animal.fact.request.format"), animal1.toString(), animal2.toString());
        return InputUtil.parseSentence();
    }

    private Node learn(Node currNode) {
        print(localeUtil.get("guess.incorrect.prompt"));
        var animal1 = Subject.of(currNode.getData());
        var animal2 = Subject.of(InputUtil.get());
        var fact = getFact(animal1, animal2);

        print(localeUtil.get("learn.confirm"), animal2);
        var isAnimal2 = InputUtil.translateBoolean();

        var animal1Fact = fact.toStatement(animal1.getNoun(), !isAnimal2);
        var animal2Fact = fact.toStatement(animal2.getNoun(), isAnimal2);
        print(localeUtil.get("learn.summary.format"), LangUtil.cap(animal1Fact), LangUtil.cap(animal2Fact));

        var otherAnimal = new Node(animal2.toString());
        var yesChild = isAnimal2 ? otherAnimal : currNode;
        var noChild = isAnimal2 ? currNode : otherAnimal;

        print(localeUtil.get("learn.questions.format"), fact.toQuestion());
        print(localeUtil.get("learn.done"));
        return new Node(yesChild, noChild, fact.toQuestion());
    }
}
