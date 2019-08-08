package com.example.demo.utils;

import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;

public class OptionalUtils {

    public static <T> Stream<T> toStream(Optional<T> opt) {
        return opt.map(Stream::of).orElseGet(Stream::<T>empty);
    }

    public static <T> Optional<T> head(Iterable<T> i) {
        Iterator<T> it = i.iterator();
        return it.hasNext() ? Optional.ofNullable(it.next()) : Optional.<T>empty();
    }
}
