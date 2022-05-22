package com.api.service.jobs.definition;

import org.jobrunr.jobs.context.JobContext;

public interface DistributedJob<T> {

    String getName();
    void execute(final String jobName, final T jobParameters, final JobContext jobContext);
}
