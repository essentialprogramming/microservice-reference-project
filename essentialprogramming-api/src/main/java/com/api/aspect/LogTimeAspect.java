package com.api.aspect;

import com.util.function.CheckedSupplier;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Aspect
@Service
@Slf4j
@RequiredArgsConstructor
public class LogTimeAspect {

    @Around("@annotation(com.util.annotations.LogExecutionTime)")
    public Object logExecutionTime(final ProceedingJoinPoint joinPoint) throws Throwable {

        val result = measureTimedValue(joinPoint::proceed);
        final String methodNameAndParameters = format(
                "%s.%s(%s)",
                joinPoint.getSignature().getDeclaringType().getSimpleName(),
                joinPoint.getSignature().getName(),
                Arrays.stream(joinPoint.getArgs())
                        .filter(Objects::nonNull)
                        .map(Object::toString)
                        .collect(Collectors.joining(", "))
        );

        log.info("Executed {} in {}", methodNameAndParameters, prettyPrint(result.getDuration()));
        return result.getValue();
    }

    /**
     * Executes the given function and returns an instance of {@link TimedValue}, containing both the result of the
     * function execution and duration of the elapsed time interval.
     *
     * This API is based on <a href="https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.time/measure-timed-value.html">Kotlin's measureTimedValue</a>.
     */
    public static <T, E extends Throwable> TimedValue<T> measureTimedValue(final CheckedSupplier<T, E> fn) throws E {
        final Instant start = Instant.now();
        final T result = fn.get();
        final Duration time = Duration.between(start, Instant.now());

        return TimedValue.of(result, time);
    }

    private static String prettyPrint(final Duration duration) {
        return duration.toString()
                .substring(2)
                .replaceAll("(\\d[HMS])(?!$)", "$1 ")
                .toLowerCase();
    }

    /**
     * Value class representing a result of executing an action, along with the duration of elapsed time interval.
     * <p>
     * This API is based on <a href="https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.time/-timed-value/">Kotlin's TimedValue</a>.
     */
    @Value(staticConstructor = "of")
    public static class TimedValue<T> {

        T value;
        Duration duration;
    }
}
