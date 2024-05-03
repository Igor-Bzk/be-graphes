package org.insa.graphs.algorithm.shortestpath;


import org.insa.graphs.algorithm.AbstractInputData.Mode;
import org.insa.graphs.algorithm.utils.BinaryHeap;

import org.insa.graphs.model.Graph;


public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
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
    protected BinaryHeap<Label> setUpHeap(Label current){
        BinaryHeap<Label> sommets = new BinaryHeap<Label>();

        LabelStar new_current = new LabelStar(current, 0);

        sommets.insert(new_current);

        return sommets;
    }

}
