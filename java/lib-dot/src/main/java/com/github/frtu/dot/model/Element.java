package com.github.frtu.dot.model;

import com.github.frtu.dot.utils.IdUtil;

/**
 * Dot Element containing all common fields like ID, comment.
 *
 * @author frtu
 * @since 0.3.6
 */
public abstract class Element {
    private String id;
    private String comment;

    protected Element(String id) {
        IdUtil.assertFormatId(id);
        this.id = id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public boolean hasComment() {
        return comment != null && !"".equals(comment);
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment.trim();
    }

    @Override
    public String toString() {
        return "Element { id='" + id + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
