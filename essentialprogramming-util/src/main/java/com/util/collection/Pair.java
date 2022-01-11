package com.util.collection;

@SuppressWarnings("unused")
public final class Pair<A, B> {
    private final A first;
    private final B second;

    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public A first() {
        return this.first;
    }

    public B second() {
        return this.second;
    }

    public static <A, B> Pair<A, B> makePair(A first, B second) {
        return new Pair<>(first, second);
    }

    @Override
    public String toString() {
        return "(" + this.first().toString() + ", " + this.second().toString() + ")";
    }

}