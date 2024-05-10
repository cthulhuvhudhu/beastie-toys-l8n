package beastie.toys.l8n.tree;

import beastie.toys.l8n.Subject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Node {
     private Node yes = null;
    private Node no = null;
    @JsonIgnore
    private Node parent = null;
    private String data;

    public Node(String data) {
        this.data = data;
    }

    public Node() {}

    public Node(Node yes, Node no, String data) {
        this.yes = yes;
        this.no = no;
        this.data = data;
    }

    public Node getYes() {
        return yes;
    }

    public Node getNo() {
        return no;
    }

    public String getData() {
        return data;
    }

    @JsonIgnore
    public int getDepth() {
        var temp = parent;
        var depth = 0;
        while (temp != null) {
            depth++;
            temp = temp.parent;
        }
        return depth;
    }

    public Node getParent() {
        return parent;
    }

    public void setYes(Node yes) {
        yes.parent = this;
        this.yes = yes;
    }

    public void setNo(Node no) {
        no.parent = this;
        this.no = no;
    }

    @JsonIgnore
    public boolean isLeaf() {
        return no == null && yes == null;
    }

    @JsonIgnore
    public Node find(String data) {
        var toVisit = new ArrayDeque<Node>();
        toVisit.add(this);
        while (!toVisit.isEmpty()) {
            var curr = toVisit.pop();
            Pattern pattern = Pattern.compile(String.format("\\b%s\\b", data));
            Matcher matcher = pattern.matcher(curr.data);
            if (matcher.find()) {
                return curr.parent;
            }
            if (curr.no != null) {
                toVisit.push(curr.no);
            }
            if (curr.yes != null) {
                toVisit.push(curr.yes);
            }
        }
        return null;
    }

    @JsonIgnore
    public List<Node> leaves() {
        if (this.isLeaf()) { return List.of(this); }
        return Stream.concat(yes.leaves().stream(), no.leaves().stream()).toList();
    }

    @JsonIgnore
    public int count() {
        if (this.isLeaf()) return 1;
        return no.count() + yes.count() + 1;
    }

    @JsonIgnore
    public int maxDepth() {
        return leaves().stream().mapToInt(Node::getDepth).max().orElse(0);
    }

    @JsonIgnore
    public String toString() {
        var yesBullet = "├ ";
        var noBullet = "└ ";
        var join = "│";
        var root = " └ ";
        var notRoot = "  ";
        var nodes = new ArrayDeque<Node>();
        var nodePrint = new HashMap<Node, String>();
        var sb = new StringBuilder();
        nodes.add(this);
        nodePrint.put(this, root);
        while (!nodes.isEmpty()) {
            var currNode = nodes.pop();
            var nodePrefix = nodePrint.remove(currNode);
            sb.append(String.format("%s%s\n", nodePrefix, currNode.data));
            // TODO Depth prefix is incorrect in case of imbalanced tree. check out other solutions
            var depthPrefix = "";
            var currDepth = currNode.getDepth();
            if (currNode.parent != null && currNode == currNode.parent.yes) {
                depthPrefix = join.repeat(currDepth);
            } else if (currDepth > 0) {
                depthPrefix = join.repeat(currDepth - 1) + " ";
            }

            if (currNode.no != null) {
                nodePrint.put(currNode.no, String.format("%s%s%s", notRoot, depthPrefix, noBullet));
                nodes.push(currNode.no);
            }
            if (currNode.yes != null) {
                nodePrint.put(currNode.yes, String.format("%s%s%s", notRoot, depthPrefix, yesBullet));
                nodes.push(currNode.yes);
            }
        }
        return sb.toString();
    }
}
