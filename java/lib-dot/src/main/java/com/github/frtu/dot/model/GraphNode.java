package com.github.frtu.dot.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Dot Node with Dot attributes for a {@link Graph}.
 * <p>
 * Note : For internal package usage.
 * <p>
 * DO NOT change field order, since reuse the field id as Dot attributes
 *
 * @author frtu
 * @since 0.3.6
 */
public class GraphNode extends Element {
    // DO NOT CHANGE THESE FIELDS ORDER
    public final static int FIRST_VISIBLE_FIELD_INDEX = 2;
    List<GraphNode> children = new ArrayList<>();
    // DO NOT CHANGE THESE FIELDS ORDER

    String label;
    PolygonShapeDotEnum shape;

    GraphNode(String id, String label, PolygonShapeDotEnum shape) {
        super(id);
        this.label = label;
        this.shape = shape;
    }

    public GraphNode setShape(PolygonShapeDotEnum shape) {
        this.shape = shape;
        return this;
    }

    GraphNode addChild(GraphNode child) {
        this.children.add(child);
        return this;
    }

    public List<GraphNode> getChildren() {
        return children;
    }

    public String getLabel() {
        return label;
    }

    public PolygonShapeDotEnum getShape() {
        return shape;
    }

    @Override
    public String toString() {
        return "GraphNode{" +
                super.toString() +
                ", label='" + label + '\'' +
                ", shape=" + shape +
                '}';
    }
}
