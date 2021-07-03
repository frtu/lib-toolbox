package com.github.frtu.dot.model;

import com.github.frtu.dot.attributes.EdgeAttributes;
import com.github.frtu.dot.attributes.GraphAttributes;
import com.github.frtu.dot.attributes.NodeAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Object representing a Graph in memory that contains {@link GraphNode}.
 * <p>
 * Note : Many graph implementation exist and the goal here is to have {@link GraphNode} POJO have the same Dot attributes.
 *
 * @author frtu
 * @since 0.3.6
 */
public class Graph extends Element {
    private static final Logger LOGGER = LoggerFactory.getLogger(Graph.class);

    protected HashMap<String, GraphNode> allNodes = new HashMap<String, GraphNode>();
    protected List<GraphEdge> edges = new ArrayList<>();

    protected List<GraphNode> primoNodes = new ArrayList<>();
    protected GraphNode currentParentNode;

    private GraphAttributes graphAttributes;
    private NodeAttributes nodeAttributes;
    private EdgeAttributes edgeAttributes;

    public Graph(String id) {
        super(id);
    }

    public boolean hasPrimoNodes() {
        return !primoNodes.isEmpty();
    }

    public List<GraphNode> getPrimoNodes() {
        return primoNodes;
    }

    public GraphNode getCurrentParentNode() {
        return currentParentNode;
    }

    public GraphNode getNode(String id) {
        return allNodes.get(id);
    }

    public HashMap<String, GraphNode> getAllNodes() {
        return allNodes;
    }

    public List<GraphEdge> getAllEdges() {
        return edges;
    }

    public GraphEdge addEdge(String sourceId, String... targetIds) {
        GraphEdge graphEdge = null;

        String baseId = sourceId;
        for (String targetId : targetIds) {
            graphEdge = new GraphEdge(baseId, targetId);
            edges.add(graphEdge);
            baseId = targetId;
        }
        return graphEdge;
    }

    public GraphEdge addEdge(Element source, Element target) {
        return addEdge(source.getId(), target.getId());
    }

    public GraphEdge addEdge(String sourceId, Element target) {
        return addEdge(sourceId, target.getId());
    }

    public GraphEdge addEdge(Element source, String targetId) {
        return addEdge(source.getId(), targetId);
    }

    /**
     * @param id and label
     * @return newly create node
     * @since 0.3.7
     */
    public GraphNode addSingleNode(String id) {
        return addSingleNode(id, PolygonShapeDotEnum.PLAIN);
    }

    public GraphNode addSingleNode(String id, PolygonShapeDotEnum polygonShape) {
        return addSingleNode(id, id, polygonShape);
    }

    public GraphNode addSingleNode(String id, String label, PolygonShapeDotEnum polygonShape) {
        return addNodeToParent(id, label, polygonShape, null);
    }

    public GraphNode addNode(String id, PolygonShapeDotEnum polygonShape) {
        return addNode(id, id, polygonShape);
    }

    public GraphNode addNode(String id, String label, PolygonShapeDotEnum polygonShape) {
        return addNodeToParent(id, label, polygonShape, this.currentParentNode);
    }

    public GraphNode addNodeMoveToChild(String id, String label, PolygonShapeDotEnum polygonShape) {
        final GraphNode graphNode = addNodeToParent(id, label, polygonShape, this.currentParentNode);
        this.currentParentNode = graphNode;
        return graphNode;
    }

    public GraphNode addNodeMoveParent(String id, String label, PolygonShapeDotEnum polygonShape, GraphNode parentNode) {
        this.currentParentNode = parentNode;
        final GraphNode graphNode = addNodeToParent(id, label, polygonShape, parentNode);
        return graphNode;
    }

    private GraphNode addNodeToParent(String id, String label, PolygonShapeDotEnum polygonShape, GraphNode parentNode) {
        LOGGER.debug("id={} label={} polygonShape={} parentNode={}", id, label, polygonShape, parentNode);
        if (parentNode != null && parentNode.getId().equals(id)) {
            final IllegalArgumentException e = new IllegalArgumentException("Cannot add a node with the same name with Parent!! id=" + id);
            LOGGER.error(e.getMessage(), e);
            throw e;
        }

        final GraphNode graphNode = new GraphNode(id, label, polygonShape);
        allNodes.put(graphNode.getId(), graphNode);

        if (parentNode == null) {
            primoNodes.add(graphNode);
        } else {
            parentNode.addChild(graphNode);
        }

        if (currentParentNode == null) {
            currentParentNode = graphNode;
        }
        return graphNode;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[id='").append(getId()).append("\']\n");

        stringBuilder.append("- graphAttributes=").append(graphAttributes).append("\n");
        stringBuilder.append("- nodeAttributes=").append(nodeAttributes).append("\n");
        stringBuilder.append("- edgeAttributes=").append(edgeAttributes).append("\n");

        this.getPrimoNodes().forEach(primoNode -> {
            stringBuilder.append(primoNode.toString()).append('\n');
            buildChildren(stringBuilder, primoNode, 1);
        });
        this.getAllEdges().forEach(edge -> {
            stringBuilder.append("-> ").append(edge).append("\n");
        });
        return stringBuilder.toString();
    }

    private void buildChildren(StringBuilder stringBuilder, GraphNode currentNode, int level) {
        currentNode.children.forEach(child -> {
            stringBuilder.append("|-");
            for (int i = 1; i < level; i++) {
                stringBuilder.append('-');
            }
            stringBuilder.append(' ').append(child).append('\n');
            if (!child.children.isEmpty()) {
                buildChildren(stringBuilder, child, level + 1);
            }
        });
    }

    public GraphAttributes newGraphAttributes() {
        final GraphAttributes graphAttributes = GraphAttributes.build();
        setGraphAttributes(graphAttributes);
        return graphAttributes;
    }

    public GraphAttributes getGraphAttributes() {
        return graphAttributes;
    }

    public void setGraphAttributes(GraphAttributes graphAttributes) {
        this.graphAttributes = graphAttributes;
    }

    public NodeAttributes newNodeAttributes() {
        final NodeAttributes nodeAttributes = NodeAttributes.build();
        setNodeAttributes(nodeAttributes);
        return nodeAttributes;
    }

    public NodeAttributes getNodeAttributes() {
        return nodeAttributes;
    }

    public void setNodeAttributes(NodeAttributes nodeAttributes) {
        this.nodeAttributes = nodeAttributes;
    }

    public EdgeAttributes newEdgeAttributes() {
        final EdgeAttributes edgeAttributes = EdgeAttributes.build();
        setEdgeAttributes(edgeAttributes);
        return edgeAttributes;
    }

    public EdgeAttributes getEdgeAttributes() {
        return edgeAttributes;
    }

    public void setEdgeAttributes(EdgeAttributes edgeAttributes) {
        this.edgeAttributes = edgeAttributes;
    }
}
