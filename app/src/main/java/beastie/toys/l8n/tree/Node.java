package beastie.toys.l8n.tree;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.*;
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
        StringBuilder sb = new StringBuilder();
        var toVisit = new ArrayDeque<Map.Entry<Node, String>>();
        toVisit.add(new AbstractMap.SimpleEntry<>(this, ""));

        while (!toVisit.isEmpty()) {
            var curr = toVisit.pop();
            if (curr.getKey() == null) {
                continue;
            }
            var node = curr.getKey();
            if (node.parent == null) {
                sb.append(curr.getValue()).append(root).append(node.data).append("\n");
                toVisit.push(new AbstractMap.SimpleEntry<>(curr.getKey().no, notRoot));
                toVisit.push(new AbstractMap.SimpleEntry<>(curr.getKey().yes, notRoot));
            } else {
                String newPrefix;
                if (node.parent.yes == node) {
                    sb.append(String.format("%s%s", curr.getValue(), yesBullet)).append(node.data).append("\n");
                    newPrefix = curr.getValue() + join;
                } else {
                    sb.append(String.format("%s%s", curr.getValue(), noBullet)).append(node.data).append("\n");
                    newPrefix = curr.getValue() + " ";
                }
                if (node.no != null) {
                    toVisit.push(new AbstractMap.SimpleEntry<>(curr.getKey().no, newPrefix));
                }
                if (node.yes != null) {
                    toVisit.push(new AbstractMap.SimpleEntry<>(curr.getKey().yes, newPrefix));
                }
            }
        }
        return sb.toString();
    }
}
