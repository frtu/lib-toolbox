package com.github.frtu.dot.attributes;

import com.github.frtu.dot.model.GraphEdge;

/**
 * Attribute defining ALL the {@link GraphEdge} of a {@link com.github.frtu.dot.model.Graph}
 *
 * @author frtu
 * @see <a href="http://graphviz.org/doc/info/attrs.html">Node, Edge and Graph Attributes</a>
 * @since 0.3.6
 */
public class EdgeAttributes extends Attributes<GraphEdge> {
    String style;
    String color;
    Double arrowsize;

    String fontcolor;
    String fontname;
    Double fontsize;

    private EdgeAttributes() {
    }

    public final static EdgeAttributes build() {
        return new EdgeAttributes();
    }

    public EdgeAttributes setStyle(String style) {
        this.style = style;
        return this;
    }

    public EdgeAttributes setColor(String color) {
        this.color = color;
        return this;
    }

    public EdgeAttributes setArrowsize(Double arrowsize) {
        this.arrowsize = arrowsize;
        return this;
    }

    public EdgeAttributes setFontcolor(String fontcolor) {
        this.fontcolor = fontcolor;
        return this;
    }

    public EdgeAttributes setFontname(String fontname) {
        this.fontname = fontname;
        return this;
    }

    public EdgeAttributes setFontsize(Double fontsize) {
        this.fontsize = fontsize;
        return this;
    }

    public String getStyle() {
        return style;
    }

    public String getColor() {
        return color;
    }

    public Double getArrowsize() {
        return arrowsize;
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

    @Override
    public String toString() {
        return "EdgeAttributes{" +
                "style='" + style + '\'' +
                ", color='" + color + '\'' +
                ", arrowsize=" + arrowsize +
                ", fontcolor='" + fontcolor + '\'' +
                ", fontname='" + fontname + '\'' +
                ", fontsize=" + fontsize +
                '}';
    }
}
