package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Node;
import org.insa.graphs.model.Arc;

public class Label implements Comparable<Label> {
    private Node current_node;
    private Boolean marked;
    protected double current_cost;
    private Arc pere;

    public Label(Node n, double cost, Arc pere) {
        current_node = n;
        marked = false;
        current_cost = cost;
        this.pere = pere;
    }

    public double getCost() {
        return current_cost;
    }

    public double getCurrentCost(){
        return current_cost;
    }

    public Node getNode() {
        return this.current_node;
    }

    public Boolean isMarked() {
        return marked;
    }

    public Arc getPere() {
        return pere;
    }

    public void setCost(double cost) {
        current_cost = cost;
    }

    public void setPere(Arc arc) {
        pere = arc;
    }

    public int compareTo(Label l) {
        if (current_cost == l.current_cost) {
            return 0;
        } else if (current_cost < l.current_cost) {
            return -1;
        } else {
            return 1;
        }
    }

    public void mark() {
        marked = true;
    }
}
