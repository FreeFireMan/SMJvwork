package com.example.demo.db.model.filterConfig;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class FilterConfigTest {

    @Test
    public void merge() {
        FilterConfig filterConfig = new FilterConfig();
        ObjectNode json = product(
            group(
                0,
                "group-0",
                attr(
                    0,
                    "attr-0",
                    null,
                    val(
                        0,
                        "val-0",
                        null))));

        filterConfig.merge(json);

        assertThat(filterConfig.getGroups(), hasKey(0));

        FilterGroup g = filterConfig.getGroups().get(0);
        assertThat(g, notNullValue());
        assertThat(g.getId(), is(0));
        assertThat(g.getName(), is("group-0"));

        assertThat(g.getAttributes(), hasKey(0));
        FilterAttribute a = g.getAttributes().get(0);
        assertThat(a, notNullValue());
        assertThat(a.getId(), is(0));
        assertThat(a.getName(), is("attr-0"));
        assertThat(a.getUnit(), is(Optional.empty()));

        assertThat(a.getValues(), hasKey(0));
        FilterAttributeValue v = a.getValues().get(0);
        assertThat(v, notNullValue());
        assertThat(v.getId(), is(0));
        assertThat(v.getValue(), is("val-0"));
        assertThat(v.getQuantifier(), is(Optional.empty()));
    }

    private static JsonNodeFactory f = JsonNodeFactory.instance;

    static ObjectNode product(JsonNode ...groupNodes) {
        ObjectNode o = f.objectNode();
        o.putArray("groups").addAll(Arrays.asList(groupNodes));
        return o;
    }

    static ObjectNode group(int id, String name, JsonNode ...attributeNodes) {
        ObjectNode o = f.objectNode()
            .put("id", id)
            .put("name", name);
        o.putArray("attributes")
            .addAll(Arrays.asList(attributeNodes));

        return o;
    }

    static ObjectNode attr(int id, String name, String unit, JsonNode ...valueNodes) {
        ObjectNode o = f.objectNode()
            .put("id", id)
            .put("name", name)
            .put("unit", unit);

        o.putArray("values").addAll(Arrays.asList(valueNodes));

        return o;
    }

    static ObjectNode val(int id, String name, String quantifier) {
        return f.objectNode()
            .put("id", id)
            .put("value", name)
            .put("quantifier", quantifier);
    }
}
