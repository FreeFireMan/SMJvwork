package com.example.demo.utils;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class OptionalUtils {

    public static <T> Stream<T> toStream(Optional<T> opt) {
        return opt.map(Stream::of).orElseGet(Stream::<T>empty);
    }

    public static <T> Optional<T> head(Iterable<T> i) {
        Iterator<T> it = i.iterator();
        return it.hasNext() ? Optional.ofNullable(it.next()) : Optional.<T>empty();
    }

    public static Optional<Integer> optInt(JsonNode n, String field) {
        return optVal(n, field, JsonNode::isInt, JsonNode::asInt);
    }

    public static Optional<String> optStr(JsonNode n, String field) {
        return optVal(n, field, JsonNode::isTextual, JsonNode::asText);
    }

    public static Optional<JsonNode> optNode(JsonNode n, String field) {
        JsonNode i = n.get(field);
        return !i.isMissingNode()? Optional.of(i) : Optional.<JsonNode>empty();
    }

    public static Optional<JsonNode> optNode(JsonNode n, String field, Predicate<? super JsonNode> filter) {
        return optNode(n, field).filter(filter);
    }

    public static <T> Optional<T> optVal(
            JsonNode n,
            String field,
            Predicate<? super JsonNode> filter,
            Function<JsonNode, T> fn) {
        return optNode(n, field, filter).map(fn);
    }
}
