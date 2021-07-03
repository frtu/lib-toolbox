package com.github.frtu.dot;

import com.github.frtu.dot.model.Graph;
import com.github.frtu.dot.model.GraphEdge;
import com.github.frtu.dot.model.GraphNode;
import com.github.frtu.dot.model.GraphTest;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DotRendererTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(DotRendererTest.class);

    @Test
    public void testRenderGraphDirected() {
        //--------------------------------------
        // 1. Prepare data
        //--------------------------------------
        Graph graph = new Graph("DirectedGraphID");
        GraphTest.buildGraph(graph);

        //--------------------------------------
        // 2. Run tests
        //--------------------------------------
        final DotRenderer dotRenderer = new DotRenderer();
        final String renderGraph = dotRenderer.renderDirectedGraph(graph);

        LOGGER.debug(renderGraph);
        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        Assert.assertTrue(renderGraph.contains("->"));
        Assert.assertTrue(!renderGraph.contains("--"));
        Assert.assertTrue(renderGraph.contains("digraph "));
    }

    @Test
    public void testRenderGraphMoreAttributes() {
        //--------------------------------------
        // 1. Prepare data
        //--------------------------------------
        Graph graph = new Graph("DirectedGraphID");
        graph.setComment("graph trimmed comment");
        GraphTest.buildGraph(graph);

        final GraphEdge graphEdge = graph.addEdge("child2_2", "subchild3_2_1");
        graphEdge.setColor("blue");
        graphEdge.setStyle("dotted");

        final GraphNode id3 = graph.getNode("id3");
        id3.setComment(" node trimmed comment ");

        //--------------------------------------
        // 2. Run tests
        //--------------------------------------
        final DotRenderer dotRenderer = new DotRenderer();
        final String renderGraph = dotRenderer.renderGraph(graph, true);

        LOGGER.debug(renderGraph);
        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        Assert.assertTrue(renderGraph.contains("/* graph trimmed comment */"));
        Assert.assertTrue(renderGraph.contains("/* node trimmed comment */"));
        Assert.assertTrue(renderGraph.contains("[color=blue,style=dotted"));
    }

    @Test
    public void testRenderGraphUnirected() {
        //--------------------------------------
        // 1. Prepare data
        //--------------------------------------
        Graph graph = new Graph("UnirectedGraphID");
        GraphTest.buildGraph(graph);
        //--------------------------------------
        // 2. Run tests
        //--------------------------------------
        final DotRenderer dotRenderer = new DotRenderer();
        final String renderGraph = dotRenderer.renderUndirectedGraph(graph);

        LOGGER.debug(renderGraph);
        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        Assert.assertTrue(!renderGraph.contains("->"));
        Assert.assertTrue(renderGraph.contains("--"));
        Assert.assertTrue(renderGraph.contains("graph"));
    }

    @Test
    public void testSuperGraph() {
        //--------------------------------------
        // 1. Prepare data
        //--------------------------------------
        SuperGraph superGraph = GraphTest.buildSuperGraph();
        //--------------------------------------
        // 2. Run tests
        //--------------------------------------
        final DotRenderer dotRenderer = new DotRenderer();
        final String renderGraph = dotRenderer.renderGraph(superGraph, true);

        LOGGER.debug(renderGraph);

        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        Assert.assertTrue(renderGraph.contains("->"));
        Assert.assertTrue(!renderGraph.contains("--"));
        Assert.assertTrue(renderGraph.contains("digraph "));
    }

}