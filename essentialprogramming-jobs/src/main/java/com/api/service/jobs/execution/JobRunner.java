package com.api.service.jobs.execution;

import com.api.service.jobs.definition.JobExecution;
import lombok.RequiredArgsConstructor;
import org.jobrunr.jobs.JobId;
import org.springframework.stereotype.Service;

/**
 * Facade to job execution engine.
 */
@Service
@RequiredArgsConstructor
public class JobRunner {

    private final JobExecutionEngine executionEngine;

    public <T> JobId enqueueJob(final JobExecution<T> job) {
        return executionEngine.enqueueJob(job);
    }

    public <T> JobId scheduleForExecution(final String dateTime, final JobExecution<T> job) {
        return executionEngine.scheduleForExecution(dateTime, job);
    }

    public <T> void scheduleRecurrently(final String cron, final JobExecution<T> job) {
        executionEngine.scheduleRecurrently(cron, job);
    }

}
