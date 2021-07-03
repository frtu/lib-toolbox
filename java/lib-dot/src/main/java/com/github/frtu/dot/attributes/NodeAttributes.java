package com.github.frtu.dot.attributes;

import com.github.frtu.dot.model.GraphNode;
import com.github.frtu.dot.model.PolygonShapeDotEnum;

/**
 * Attribute defining ALL the {@link GraphNode} of a {@link com.github.frtu.dot.model.Graph}
 *
 * @author frtu
 * @see <a href="http://graphviz.org/doc/info/attrs.html">Node, Edge and Graph Attributes</a>
 * @since 0.3.6
 */
public class NodeAttributes extends Attributes<GraphNode> {
    String style;

    String bgcolor;
    String color;
    String fillcolor;

    String fontcolor;
    String fontname;
    Double fontsize;

    PolygonShapeDotEnum shape;

    private NodeAttributes() {
    }

    public final static NodeAttributes build() {
        return new NodeAttributes();
    }

    public NodeAttributes setStyle(String style) {
        this.style = style;
        return this;
    }

    public NodeAttributes setBgcolor(String bgcolor) {
        this.bgcolor = bgcolor;
        return this;
    }

    public NodeAttributes setColor(String color) {
        this.color = color;
        return this;
    }

    public NodeAttributes setFillcolor(String fillcolor) {
        this.fillcolor = fillcolor;
        return this;
    }

    public NodeAttributes setFontcolor(String fontcolor) {
        this.fontcolor = fontcolor;
        return this;
    }

    public NodeAttributes setFontname(String fontname) {
        this.fontname = fontname;
        return this;
    }

    public NodeAttributes setFontsize(Double fontsize) {
        this.fontsize = fontsize;
        return this;
    }

    public NodeAttributes setShape(PolygonShapeDotEnum shape) {
        this.shape = shape;
        return this;
    }

    public String getStyle() {
        return style;
    }

    public String getBgcolor() {
        return bgcolor;
    }

    public String getColor() {
        return color;
    }

    public String getFillcolor() {
        return fillcolor;
    }

    public String getFontcolor() {
        return fontcolor;
    }

    public String getFontname() {
        return fontname;
    }

    public Double getFontsize() {
        return fontsize;
    }

    public PolygonShapeDotEnum getShape() {
        return shape;
    }

    @Override
    public String toString() {
        return "NodeAttributes{" +
                "style='" + style + '\'' +
                ", bgcolor='" + bgcolor + '\'' +
                ", color='" + color + '\'' +
                ", fillcolor='" + fillcolor + '\'' +
                ", fontcolor='" + fontcolor + '\'' +
                ", fontname='" + fontname + '\'' +
                ", fontsize=" + fontsize +
                ", shape=" + shape +
                '}';
    }
}
