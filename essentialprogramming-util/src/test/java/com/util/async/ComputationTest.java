package com.util.async;

import com.util.task.PriorityTask;
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
    private static final int numberOfTests = 30;
    @Test
    @SneakyThrows
    void shouldExecuteTasksBasedOnPriority() {
        //given
        final List<PriorityTask> taskList = new ArrayList<>();

        final ExecutorService executorService = ExecutorsProvider.getManagedAsyncExecutor(true);
        final CountDownLatch latch = new CountDownLatch(numberOfTests);

        BiConsumer<PriorityTask, Throwable> consumer = (priorityTask, error) -> {
            if (error != null) {
               //
            } else {
                taskList.add(priorityTask);
            }
            latch.countDown();
        };

        //dummy to keep thread occupied
        Computation.computeAsync(new PriorityTask(ExecutionPriority.HIGHER), executorService, ExecutionPriority.HIGHER);


        //when
        IntStream.range(0, numberOfTests).forEach(number -> {
            ExecutionPriority priority = getRandomPriority();
            Computation.computeAsync(new PriorityTask(priority), executorService, priority)
                    .whenComplete(consumer);
        });

        //then
        latch.await();

        List<PriorityTask> sortedByPriority = taskList.stream().sorted(Comparator.comparing(PriorityTask::getExecutionPriority)).collect(Collectors.toList());
        Assertions.assertThat(taskList.equals(sortedByPriority)).isTrue();
    }

    private ExecutionPriority getRandomPriority() {
        int max = 5;
        int min = 0;

        return ExecutionPriority.fromPriority(random.nextInt(max - min + 1) + min);
    }
}
