package com.github.frtu.dot.attributes;

import com.github.frtu.dot.model.Graph;

/**
 * Attribute defining ALL the {@link Graph} of a {@link com.github.frtu.dot.model.Graph}
 *
 * @author frtu
 * @see <a href="http://graphviz.org/doc/info/attrs.html">Node, Edge and Graph Attributes</a>
 * @since 0.3.6
 */
public class GraphAttributes extends Attributes<Graph> {
    String style;

    String bgcolor;
    String color;

    String fontcolor;
    String fontname;
    Double fontsize;

    Boolean center;

    private GraphAttributes() {
    }

    public final static GraphAttributes build() {
        return new GraphAttributes();
    }

    public GraphAttributes setStyle(String style) {
        this.style = style;
        return this;
    }

    public GraphAttributes setBgcolor(String bgcolor) {
        this.bgcolor = bgcolor;
        return this;
    }

    public GraphAttributes setColor(String color) {
        this.color = color;
        return this;
    }

    public GraphAttributes setFontcolor(String fontcolor) {
        this.fontcolor = fontcolor;
        return this;
    }

    public GraphAttributes setFontname(String fontname) {
        this.fontname = fontname;
        return this;
    }

    public GraphAttributes setFontsize(Double fontsize) {
        this.fontsize = fontsize;
        return this;
    }

    public GraphAttributes setCenter(Boolean center) {
        this.center = center;
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

    public String getFontcolor() {
        return fontcolor;
    }

    public String getFontname() {
        return fontname;
    }

    public Double getFontsize() {
        return fontsize;
    }

    public Boolean getCenter() {
        return center;
    }

    @Override
    public String toString() {
        return "GraphAttributes{" +
                "style='" + style + '\'' +
                ", bgcolor='" + bgcolor + '\'' +
                ", color='" + color + '\'' +
                ", fontcolor='" + fontcolor + '\'' +
                ", fontname='" + fontname + '\'' +
                ", fontsize=" + fontsize +
                ", center=" + center +
                '}';
    }
}
