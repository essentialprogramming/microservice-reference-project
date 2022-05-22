package com.api.service.jobs.definition;

import org.jobrunr.jobs.lambdas.JobLambda;

@FunctionalInterface
public interface JobExecution<T> extends JobLambda {
    void run() throws Exception;
}
