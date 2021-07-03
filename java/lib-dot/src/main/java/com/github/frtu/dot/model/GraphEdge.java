package com.github.frtu.dot.model;

/**
 * Dot Edge with Dot attributes for a {@link Graph}.
 * <p>
 * Note : For internal package usage.
 * <p>
 * DO NOT change field order, since reuse the field id as Dot attributes
 *
 * @author frtu
 * @since 0.3.6
 */
public class GraphEdge {
    // DO NOT CHANGE THESE FIELDS ORDER
    public final static int FIRST_VISIBLE_FIELD_INDEX = 3;
    private String sourceId;
    private String targetId;
    // DO NOT CHANGE THESE FIELDS ORDER

    private String color;
    private String style;

    GraphEdge(Element source, Element target) {
        this(source.getId(), target.getId());
    }

    GraphEdge(String sourceId, String targetId) {
        this.sourceId = sourceId;
        this.targetId = targetId;
    }

    public GraphEdge setColor(String color) {
        this.color = color;
        return this;
    }

    public GraphEdge setStyle(String style) {
        this.style = style;
        return this;
    }

    public boolean hasAttributes() {
        return color != null || style != null;
    }

    public String getSourceId() {
        return sourceId;
    }

    public String getTargetId() {
        return targetId;
    }

    public String getColor() {
        return color;
    }

    public String getStyle() {
        return style;
    }

    @Override
    public String toString() {
        return "GraphEdge{" +
                "sourceId='" + sourceId + '\'' +
                ", targetId='" + targetId + '\'' +
                ", color='" + color + '\'' +
                ", style='" + style + '\'' +
                '}';
    }
}
