package com.github.frtu.dot;

import com.github.frtu.dot.model.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class FieldStreamTest {

    private void assertInvisibleField(Map<String, Object> values, String field) {
        final Object object = values.get(field);
        final String message = String.format("'%s' field should be invisible but value=%s, since needs special treatment",
                field, object);
        Assert.assertNull(message, object);
    }

    @Test
    public void applyForNode() {
        //--------------------------------------
        // 1. Prepare data
        //--------------------------------------
        final String label = "aLabel";
        final PolygonShapeDotEnum polygonShapeDotEnum = PolygonShapeDotEnum.BOX;

        final GraphNode graphNode = new PublicGraphNode("anId", label, polygonShapeDotEnum);
        //--------------------------------------
        // 2. Run tests
        //--------------------------------------
        final Map<String, Object> values = new HashMap<>();
        FieldStream.node(graphNode).apply((name, value) -> {
            values.put(name, value);
        });
        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        assertInvisibleField(values, "id");
        assertInvisibleField(values, "comment");
        Assert.assertEquals(label, values.get("label"));
        Assert.assertEquals(polygonShapeDotEnum, values.get("shape"));
    }


    @Test
    public void applyForEdge() {
        //--------------------------------------
        // 1. Prepare data
        //--------------------------------------
        final GraphEdge graphEdge = new PublicGraphEdge(new Graph("sourceId"), new Graph("targetId"));
        //--------------------------------------
        // 2. Run tests
        //--------------------------------------
        final Map<String, Object> values = new HashMap<>();
        FieldStream.edge(graphEdge).apply((name, value) -> {
            values.put(name, value);
        });
        //--------------------------------------
        // 3. Validate
        //--------------------------------------
        assertInvisibleField(values, "source");
        assertInvisibleField(values, "target");
    }
}