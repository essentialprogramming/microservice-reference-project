package com.util.async;

import com.util.task.PriorityTask;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;


import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ComputationTest {

    private final Random random = new Random();

    @Test
    @SneakyThrows
    void shouldExecuteTasksBasedOnPriority() {
        //given
        ExecutorService executorService = ExecutorsProvider.getManagedAsyncExecutor(true);

        //dummy to keep thread occupied
        Computation.computeAsync(new PriorityTask(ExecutionPriority.HIGHER), executorService, ExecutionPriority.HIGHER);

        List<PriorityTask> taskList = new ArrayList<>();
        int numberOfTests = 30;

        //when
        IntStream.range(0, numberOfTests).forEach(number -> {
            ExecutionPriority priority = getRandomPriority();
            Computation.computeAsync(new PriorityTask(priority), executorService, priority)
                    .whenComplete((priorityTask, throwable) -> taskList.add(priorityTask));
        });

        //then
        Thread.sleep(numberOfTests * 1150);

        List<PriorityTask> sortedByPriority = taskList.stream().sorted(Comparator.comparing(PriorityTask::getExecutionPriority)).collect(Collectors.toList());
        Assertions.assertThat(taskList.equals(sortedByPriority)).isTrue();
    }

    private ExecutionPriority getRandomPriority() {
        int max = 5;
        int min = 0;

        return ExecutionPriority.fromPriority(random.nextInt(max - min + 1) + min);
    }
}
