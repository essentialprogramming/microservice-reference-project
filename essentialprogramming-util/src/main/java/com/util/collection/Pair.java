package com.util.collection;

import java.util.Arrays;

@SuppressWarnings("unused")
public final class Pair<A, B> implements Comparable<Pair<A, B>> {
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

    public static <A, B> Pair<A, B> of(A first, B second) {
        return new Pair<>(first, second);
    }

    @Override
    public String toString() {
        return "(" + this.first().toString() + ", " + this.second().toString() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return first.equals(pair.first) && second.equals(pair.second);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[]{first, second});
    }

    @SuppressWarnings("unchecked")
    @Override
    public int compareTo(Pair<A, B> o) {
        if (first instanceof Comparable<?>) {
            int l = ((Comparable<A>) first).compareTo(o.first);
            if (l != 0) {
                return l;
            }
        }
        if (second instanceof Comparable<?>) {
            int r = ((Comparable<B>) second).compareTo(o.second);
            if (r != 0) {
                return r;
            }
        }
        return 0;
    }
}