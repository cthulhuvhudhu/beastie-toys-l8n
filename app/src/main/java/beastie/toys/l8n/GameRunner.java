package beastie.toys.l8n;

import beastie.toys.l8n.lang.LangUtil;
import beastie.toys.l8n.lang.Sentence;
import beastie.toys.l8n.lang.Subject;
import beastie.toys.l8n.tree.Node;
import beastie.toys.l8n.util.InputUtil;
import beastie.toys.l8n.util.PrintUtil;

import java.util.Map;
import java.util.stream.Collectors;

import static beastie.toys.l8n.App.localeUtil;
import static beastie.toys.l8n.App.fileManager;
import static beastie.toys.l8n.util.PrintUtil.print;
import static beastie.toys.l8n.util.PrintUtil.println;

public class GameRunner {
    protected Node root;
    private final Map<Integer, Runnable> menuOptions = Map.of(
        0, this::exit,
        1, this::startGame,
        2, this::printAnimals,
        3, this::printFactsByAnimal,
        4, this::printStats,
        5, () -> print(root)
    );

    public void run() {
        InputUtil.greet();
        loadTree();

        do {
            println(localeUtil.get("menu"));
            var opt = InputUtil.get();
            if (opt.isEmpty()) {
                continue;
            } try {
                var option = Integer.parseInt(opt);
                if (menuOptions.containsKey(option)) {
                    menuOptions.get(option).run();
                }
            } catch (NumberFormatException e) {
                // Simply reprint menu and start over
            }
        } while (true);
    }

    private void exit() {
        fileManager.write(root);
        InputUtil.close();
        InputUtil.farewell();
        System.exit(0);
    }

    private void printFactsByAnimal() {
        print(localeUtil.get("animal.request"));
        var animal = Subject.of(InputUtil.get());
        var facts = root.findFacts(animal);
        if (facts.isEmpty()) {
            print(localeUtil.get("animal.fact.header.none"), animal.getNoun());
        } else {
            print(localeUtil.get("animal.fact.header"), animal.getNoun());
            facts.forEach(PrintUtil::println);
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
        if (fileManager.exists()) {
            root = fileManager.read(Node.class);
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
        boolean letsPlay;

        print(localeUtil.get("play.start"));
        do {
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
        } while (letsPlay);
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
