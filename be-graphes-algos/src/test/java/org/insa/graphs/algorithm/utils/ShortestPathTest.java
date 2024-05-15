package org.insa.graphs.algorithm.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.shortestpath.AStarAlgorithm;
import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.algorithm.shortestpath.ShortestPathSolution;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.BinaryPathReader;
import org.insa.graphs.model.io.GraphReader;

@RunWith(Parameterized.class)
public class ShortestPathTest {
    // public ShortestPathData createShortestPathData();

    @Parameters
    public static List<Object> data() throws IOException {
        final String mapName = "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/belgium.mapgr";
        final GraphReader reader = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
        final Graph graph = reader.read();
        Node source = graph.get(766701);
        Node destination = graph.get(905615);
        ShortestPathData data = new ShortestPathData(graph, source, destination,
                ArcInspectorFactory.getAllFilters().get(0));
        reader.close();
        return Arrays.asList(data);
    }

    private ShortestPathSolution solution;

    @Parameter
    public ShortestPathData inputData;

    @Before
    public void init() {
        ShortestPathAlgorithm aStarAlgorithm = new AStarAlgorithm(inputData);
        this.solution = aStarAlgorithm.run();
    }

    @Test
    public void originIsOrigin() {
        assertEquals(inputData.getOrigin(), solution.getPath().getOrigin());
    }

    @Test
    public void isValidPath() {
        assertTrue(solution.getPath().isValid());
    }
    @Test
    public void runsFaster(){

        long startTime = System.nanoTime();
        ShortestPathAlgorithm aStarAlgorithm = new AStarAlgorithm(inputData);
        ShortestPathSolution solution = aStarAlgorithm.run();

        long endTime = System.nanoTime();
        long duration = (endTime - startTime);

        startTime = System.nanoTime();
        ShortestPathAlgorithm Dijkstra = new DijkstraAlgorithm(inputData);
        ShortestPathSolution dijkstraSolution = Dijkstra.run();
        endTime = System.nanoTime();
        long duration2 = (endTime - startTime);
        System.out.println((duration2 - duration));
    }

    @Test
    public void DijkstraEqualsAStar() {
        ShortestPathAlgorithm Dijkstra = new DijkstraAlgorithm(inputData);
        ShortestPathSolution dijkstraSolution = Dijkstra.run();
        ArrayList<Integer> diverging = new ArrayList<Integer>();
        boolean affichage = false;

        assertTrue((dijkstraSolution.getPath().getLength() - solution.getPath().getLength()) < solution.getPath().getLength()/100.0);
        for (int i = 0; i < solution.getPath().getArcs().size(); i++) {
            System.out.println(solution.getPath().getArcs().get(i).getDestination().getId());
            if (solution.getPath().getArcs().get(i).getDestination().getId() == 51885) {
                affichage = true;
            }
            if (solution.getPath().getArcs().get(i).getDestination().getId() == 54921) {
                affichage = false;
            }
            if (affichage){
                System.out.println("arc : "+dijkstraSolution.getPath().getArcs().get(i).getDestination().getId());
                diverging.add(dijkstraSolution.getPath().getArcs().get(i).getDestination().getId());
            }
            //assertEquals(solution.getPath().getArcs().get(i),dijkstraSolution.getPath().getArcs().get(i));
            
            
        }
        assertEquals(diverging,new ArrayList<Integer>());
    }
}