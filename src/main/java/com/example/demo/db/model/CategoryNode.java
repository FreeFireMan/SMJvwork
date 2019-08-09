package com.example.demo.db.model;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class CategoryNode {
    private CategoryHolder value;
    private Collection<CategoryNode> children = new ArrayDeque<>();
    private Collection<ProductNode> products = new ArrayDeque<>();

    public CategoryNode(CategoryHolder value) {
        this.value = value;
    }

    public void append(CategoryNode node) {
        children.add(node);
    }
    public CategoryNode appendCategories(Collection<CategoryNode> nodes) {
        children.addAll(nodes);
        return this;
    }

    public void append(ProductNode node) {
        products.add(node);
    }
    public CategoryNode appendProducts(Collection<ProductNode> nodes) {
        products.addAll(nodes);
        return this;
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
            for (ProductNode c : this.products) products.add(c.toJson());
        }

        return root;
    }

    public List<ObjectNode> flattenedProductShorts() {
        ArrayList<ObjectNode> nodes = new ArrayList<>();
        flattenProductShorts(nodes);
        return nodes;
    }

    private void flattenProductShorts(List<ObjectNode> nodes) {
        for (ProductNode p: products) {
            nodes.add(p.getValue().getValue());
        }

        for (CategoryNode c: children) {
            c.flattenProductShorts(nodes);
        }
    }

    public List<ObjectNode> flattenedProductLongs() {
        ArrayList<ObjectNode> nodes = new ArrayList<>();
        flattenProductLongs(nodes);
        return nodes;
    }

    private void flattenProductLongs(List<ObjectNode> nodes) {
        for (ProductNode p: products) {
            nodes.add(p.getDescription().getValue());
        }

        for (CategoryNode c: children) {
            c.flattenProductLongs(nodes);
        }
    }

    public List<ObjectNode> flattenedCategories() {
        ArrayList<ObjectNode> nodes = new ArrayList<>();
        flattenCategories(nodes);
        return nodes;
    }

    private void flattenCategories(List<ObjectNode> nodes) {
        nodes.add(value.getValue());
        for (CategoryNode c: children) {
            c.flattenCategories(nodes);
        }
    }
}
