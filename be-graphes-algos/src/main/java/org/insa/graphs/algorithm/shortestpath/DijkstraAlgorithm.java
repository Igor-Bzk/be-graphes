package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.Collections;

import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.algorithm.utils.ElementNotFoundException;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Path;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    protected Label[] setUpLabels(Graph graph, ShortestPathData data){
        final int nbNodes = graph.size();

        Label[] labels = new Label[nbNodes];
        graph.getNodes().forEach(node -> labels[node.getId()] = new Label(node, Double.POSITIVE_INFINITY, null));
        labels[data.getOrigin().getId()].setCost(0);
        return labels;
    }

    protected BinaryHeap<Label> setUpHeap(Label current){
        BinaryHeap<Label> sommets = new BinaryHeap<Label>();
        sommets.insert(current);

        return sommets;
    }

    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;

        // Retrieve the graph.
        Graph graph = data.getGraph();

        // Initialize array of distances.
        Label[] labels = setUpLabels(graph, data);

        // Notify observers about the first event (origin processed).
        notifyOriginProcessed(data.getOrigin());


        Label current = new Label(data.getOrigin(), 0, null);

        BinaryHeap<Label> sommets = setUpHeap(current);

        // boolean found = false;

        while (current.getNode() != data.getDestination() && !sommets.isEmpty()) {
            current = sommets.deleteMin();
            // System.out.println(current.getNode().toString());
            if (current.getNode().hasSuccessors()) {
                for (Arc arc : current.getNode().getSuccessors()) {
                    // System.out.println(arc.toString());
                    if (!labels[arc.getDestination().getId()].isMarked() && data.isAllowed(arc)) {
                        double w = data.getCost(arc);
                        Label next_label = labels[arc.getDestination().getId()];
                        double oldDistance = next_label.getCurrentCost();
                        double newDistance = labels[current.getNode().getId()].getCurrentCost() + w;

                        if (Double.isInfinite(oldDistance) && Double.isFinite(newDistance)) {
                            notifyNodeReached(arc.getDestination());
                        }

                        if (newDistance < oldDistance) {
                            // found = false;
                            next_label.setCost(newDistance);
                            next_label.setPere(arc);
                            if (Double.isFinite(oldDistance)) {
                                try {
                                    sommets.remove(next_label);
                                } catch (ElementNotFoundException e) {
                                }
                            }
                            sommets.insert(next_label);
                        }
                    }
                }
                labels[current.getNode().getId()].mark();
                notifyNodeMarked(current.getNode());

            }
        }

        // Destination has no predecessor, the solution is infeasible...
        if (labels[data.getDestination().getId()].getPere() == null) {
            solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        } else {

            // The destination has been found, notify the observers.
            notifyDestinationReached(data.getDestination());

            // Create the path from the array of predecessors...
            ArrayList<Arc> arcs = new ArrayList<>();
            Arc arc = labels[data.getDestination().getId()].getPere();
            while (arc != null) {
                arcs.add(arc);
                arc = labels[arc.getOrigin().getId()].getPere();
            }

            // Reverse the path...
            Collections.reverse(arcs);

            // Create the final solution.
            solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, arcs));
        }

        return solution;

    }

}
