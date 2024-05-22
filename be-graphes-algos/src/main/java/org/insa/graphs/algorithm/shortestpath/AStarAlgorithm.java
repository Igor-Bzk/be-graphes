package org.insa.graphs.algorithm.shortestpath;


import org.insa.graphs.algorithm.AbstractInputData.Mode;
import org.insa.graphs.algorithm.utils.BinaryHeap;

import org.insa.graphs.model.Graph;


public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    //LabelStar : Label (noeud + distance parcourue jusqu'a ce noeud + arc par lequel on est arrive a ce noeud) + cout estime
    //retourne un tableau de LabelStar initialises comme tel :
    //noeud -> de meme que dans Label ;
    //distance parcourue jusqu'a ce noeud -> de meme que dans Label ;
    //arc par lequel on est arrive a ce noeud -> de meme que dans Label ;
    //cout estime -> distance a vol d'oiseau (donnee qui est soit en kilometres, soit en secondes. Si elle est en secondes, division par la vitesse maximale du graphe) jusqu'a la destination.
    protected LabelStar[] setUpLabels(Graph graph, ShortestPathData data){
        final int nbNodes = graph.size();
        LabelStar[] labels = new LabelStar[nbNodes];
        if (data.getMode() == Mode.LENGTH){
            graph.getNodes().forEach(node -> labels[node.getId()] = new LabelStar(node, Double.POSITIVE_INFINITY, null, node.getPoint().distanceTo(data.getDestination().getPoint())));
        } else {
            graph.getNodes().forEach(node -> labels[node.getId()] = new LabelStar(node, Double.POSITIVE_INFINITY, null, node.getPoint().distanceTo(data.getDestination().getPoint())*3.6/graph.getGraphInformation().getMaximumSpeed()));
        }
        labels[data.getOrigin().getId()].setCost(0);
        return labels;
    }

    @Override
    //meme chose que BinaryHeap avec des Label dans DijkstraAlgorithm.java, mais avec des LabelStar a la place des Label
    protected BinaryHeap<Label> setUpHeap(Label current){
        BinaryHeap<Label> sommets = new BinaryHeap<Label>();

        LabelStar new_current = new LabelStar(current, 0);

        sommets.insert(new_current);

        return sommets;
    }

}
