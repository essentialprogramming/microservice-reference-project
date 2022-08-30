package com.util.async;

import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;


import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ComputationTest {

    private final Random random = new Random();
    private static final int NUMBER_OF_TASKS = 30;

    @Test
    @SneakyThrows
    void should_execute_tasks_based_on_priority() {
        //given
        final List<PriorityTask> executedTasks = new ArrayList<>();

        final ExecutorService executorService = ExecutorsProvider.getManagedAsyncExecutor(true);
        final CountDownLatch latch = new CountDownLatch(NUMBER_OF_TASKS);

        BiConsumer<PriorityTask, Throwable> consumer = (priorityTask, error) -> {
            if (error != null) {
               //
            } else {
                executedTasks.add(priorityTask);
            }
            latch.countDown();
        };

        //dummy to keep thread occupied
        Computation.computeAsync(new PriorityTask(ExecutionPriority.HIGHER), executorService, ExecutionPriority.HIGHER);


        //when
        IntStream.range(0, NUMBER_OF_TASKS).forEach(number -> {
            ExecutionPriority priority = getRandomPriority();
            Computation.computeAsync(new PriorityTask(priority), executorService, priority)
                    .whenComplete(consumer);
        });

        //then
        latch.await();

        List<PriorityTask> tasksSortedByPriority = executedTasks.stream()
                .sorted(Comparator.comparing(PriorityTask::getExecutionPriority))
                .collect(Collectors.toList());

        Assertions.assertThat(executedTasks.equals(tasksSortedByPriority)).isTrue();
        Assertions.assertThat(executedTasks.size()).isEqualTo(NUMBER_OF_TASKS);
    }

    private ExecutionPriority getRandomPriority() {
        int min = 0;
        int max = 5;

        return ExecutionPriority.fromPriority(random.nextInt(max - min + 1) + min);
    }
}
