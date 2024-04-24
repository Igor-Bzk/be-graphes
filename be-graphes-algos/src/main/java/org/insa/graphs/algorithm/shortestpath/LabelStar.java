package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;

public class LabelStar extends Label{

    private double coutEstime;

    public LabelStar(Node n, double cost, Arc pere,double coutEstime) {
        super(n, cost, pere); 
        this.coutEstime = coutEstime;
    }

    public LabelStar(Label l, double coutEstime){
        super(l.getNode(), l.getCost(), l.getPere());
        this.coutEstime = coutEstime;
    }
    
    public double getCost() {
        return coutEstime + this.current_cost;
    }

    public int compareTo(LabelStar l) {
        if (this.getCost() == l.getCost()) {
            return 0;
        } else if (this.getCost() < l.getCost()) {
            return -1;
        } else {
            return 1;
        }
    }
}
