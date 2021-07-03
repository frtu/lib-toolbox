package com.github.frtu.dot;

import com.github.frtu.dot.attributes.Attributes;
import com.github.frtu.dot.model.Element;
import com.github.frtu.dot.model.Graph;
import com.github.frtu.dot.model.GraphEdge;
import com.github.frtu.dot.model.GraphNode;

/**
 * From a {@link Graph} object, create a Dot language.
 *
 * @author frtu
 * @see <a href="https://en.wikipedia.org/wiki/DOT_%28graph_description_language%29">DOT (graph description language) in WIKIPEDIA</a>
 * @see <a href="https://graphviz.gitlab.io/_pages/doc/info/lang.html">DOT Grammar</a>
 * @since 0.3.6
 */
public class DotRenderer {
    private StringBuilder result;

    public DotRenderer() {
        result = new StringBuilder();
    }

    public String renderGraph(SuperGraph graph, boolean directed) {
        if (directed) {
            result.append("di");
        }
        // TODO OPTIMIZE!
        result.append("graph ").append(graph.getId()).append(" {\n");
        renderComment(graph);

        renderAttributes("graph", graph.getGraphAttributes());
        renderAttributes("node", graph.getNodeAttributes());
        renderAttributes("edge", graph.getEdgeAttributes());

        // SuperGraph
        final String rankdir = graph.getRankdir();
        if (rankdir != null) {
            indent();
            result.append("rankdir=").append(rankdir);
            newline();
        }
        graph.getSubgraphs().forEach(subgraph -> {
            indent();
            result.append("sub");
            renderGraph(subgraph, directed);
            newline();
        });
        // SuperGraph

        graph.getPrimoNodes().forEach(primoNode -> {
            renderGraphNode(primoNode, directed);
        });
        graph.getAllEdges().stream().forEach(graphEdge -> {
            renderStatementEdge(graphEdge, directed);
        });

        result.append("}");
        return result.toString();
    }

    public String renderDirectedGraph(Graph graph) {
        result.append("di");
        return renderGraph(graph, true);
    }

    public String renderUndirectedGraph(Graph graph) {
        return renderGraph(graph, false);
    }

    public String renderGraph(Graph graph, boolean directed) {
        result.append("graph ").append(graph.getId()).append(" {\n");
        renderComment(graph);

        renderAttributes("graph", graph.getGraphAttributes());
        renderAttributes("node", graph.getNodeAttributes());
        renderAttributes("edge", graph.getEdgeAttributes());

        graph.getPrimoNodes().forEach(primoNode -> {
            renderGraphNode(primoNode, directed);
        });
        graph.getAllEdges().stream().forEach(graphEdge -> {
            renderStatementEdge(graphEdge, directed);
        });

        result.append("}");
        return result.toString();
    }

    private DotRenderer renderAttributes(String section, Attributes attributes) {
        if (attributes != null) {
            final StringBuilder stringBuilder = new StringBuilder();
            FieldStream.attributes(attributes).apply((name, value) -> {
                stringBuilder.append(name).append('=').append(value).append(',');
            });

            if (stringBuilder.length() > 1) {
                indent();
                result.append(section).append(" [").append(stringBuilder.deleteCharAt(stringBuilder.length() - 1)).append("]");
                newline();
            }
        }
        return this;
    }

    private DotRenderer renderComment(Element element) {
        if (element.hasComment()) {
            indent();
            result.append("/* ").append(element.getComment()).append(" */");
            newline();
        }
        return this;
    }

    private DotRenderer renderGraphNode(GraphNode node, boolean directed) {
        renderComment(node);
        // statement : node_stmt
        renderStatementNode(node);
        node.getChildren().forEach(childNode -> {
                    renderGraphNode(childNode, directed);
                    // statement : edge_stmt
                    renderStatementEdge(node.getId(), childNode.getId(), directed);
                }
        );
        return this;
    }

    private DotRenderer renderStatementEdge(GraphEdge graphEdge, boolean directed) {
        renderStatementEdge(graphEdge.getSourceId(), graphEdge.getTargetId(), directed, false);

        if (graphEdge.hasAttributes()) {
            result.append(" [");
            FieldStream.edge(graphEdge).apply((name, value) -> {
                result.append(name).append('=').append(value).append(',');
            });
            result.deleteCharAt(result.length() - 1).append("]");
        }
        newline();
        return this;
    }

    private DotRenderer renderStatementEdge(String sourceId, String targetId, boolean directed) {
        return renderStatementEdge(sourceId, targetId, directed, true);
    }

    private DotRenderer renderStatementEdge(String sourceId, String targetId, boolean directed, boolean terminateLine) {
        indent();
        result.append(sourceId);
        if (directed) {
            result.append(" -> ");
        } else {
            result.append(" -- ");
        }
        result.append(targetId);
        if (terminateLine) {
            newline();
        }
        return this;
    }

    private DotRenderer renderStatementNode(GraphNode graphNode) {
        indent();
        result.append(graphNode.getId()).append(" [");

        FieldStream.node(graphNode).apply((name, value) -> {
            result.append(name).append('=');
            if (value instanceof String) {
                result.append('"');
            }

            result.append(value);

            if (value instanceof String) {
                result.append('"');
            }
            result.append(',');
        });
        result.deleteCharAt(result.length() - 1).append(']');
        newline();
        return this;
    }

    private void indent() {
        indent(1);
    }

    private void indent(int times) {
        for (int i = 0; i < times; i++) {
            result.append("  ");
        }
    }

    private void newline() {
        result.append("\n");
    }
}
