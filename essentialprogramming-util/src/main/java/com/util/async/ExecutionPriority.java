package com.util.async;

import java.util.Comparator;
import java.util.stream.Stream;

public enum ExecutionPriority {

    VIP(5), // highest
    HIGHEST(4), // important operations
    HIGHER(3),
    HIGH(2), // REST calls
    MEDIUM(1), // default
    LOW(0);


    private final int priority;

    ExecutionPriority(int priority) {
        this.priority = priority;
    }

    public static ExecutionPriority fromPriority(final Integer priority) {
        return priority != null ?
                Stream.of(ExecutionPriority.values())
                        .filter(ep -> priority == ep.getPriority())
                        .findAny()
                        .orElse(ExecutionPriority.MEDIUM) :
                ExecutionPriority.MEDIUM;
    }

    public int getPriority() {
        return priority;
    }

    public boolean hasHigherPriority(final ExecutionPriority other) {
        return this.priority > other.priority;
    }

    public static Comparator<ExecutionPriority> COMPARATOR = Comparator.comparingInt(ExecutionPriority::getPriority);
}