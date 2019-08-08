package com.example.demo.db.model;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.ArrayDeque;
import java.util.Collection;

@Data
public class CategoryNode {
    private CategoryHolder value;
    private Collection<CategoryNode> children = new ArrayDeque<>();
    private Collection<ShortProductNode> products = new ArrayDeque<>();

    public CategoryNode(CategoryHolder value) {
        this.value = value;
    }

    public void append(CategoryNode node) {
        children.add(node);
    }

    public void append(ShortProductNode node) {
        products.add(node);
    }

    public ObjectNode toJson() {
        ObjectNode root = value.getValue();

        // add children
        if (!this.children.isEmpty()) {
            ArrayNode children = root.putArray("children");
            for (CategoryNode c : this.children) children.add(c.toJson());
        }

        // add children
        if (!this.products.isEmpty()) {
            ArrayNode products = root.putArray("products");
            for (ShortProductNode c : this.products) products.add(c.toJson());
        }

        return root;
    }
}
