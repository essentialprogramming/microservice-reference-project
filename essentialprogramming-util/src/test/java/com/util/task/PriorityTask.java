package com.util.task;

import com.util.async.ExecutionPriority;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;

import java.util.concurrent.Callable;

@Getter
@Setter
@RequiredArgsConstructor
public class PriorityTask implements Callable<PriorityTask>{

    private final ExecutionPriority executionPriority;

    @SneakyThrows
    public PriorityTask call() {

        Thread.sleep(1000);

        return this;
    }
}
