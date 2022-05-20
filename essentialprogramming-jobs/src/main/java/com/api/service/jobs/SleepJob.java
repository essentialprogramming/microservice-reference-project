package com.api.service.jobs;

import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.jobs.context.JobContext;
import org.jobrunr.jobs.context.JobDashboardProgressBar;
import org.jobrunr.jobs.context.JobRunrDashboardLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SleepJob {

    private static final Logger LOGGER = new JobRunrDashboardLogger(LoggerFactory.getLogger(SleepJob.class));

    @Job(name = "Thread sleep job")
    public static void executeJob(JobContext jobContext) {
        final JobDashboardProgressBar progressBar = jobContext.progressBar(100);

        LOGGER.info("Job started...");
        progressBar.setValue(0);
        try {
            Thread.sleep(16000);
            progressBar.setValue(40);
            LOGGER.info("Executing job... Progress {}%", progressBar.getProgress());
            Thread.sleep(16000);
            progressBar.setValue(80);
            LOGGER.info("Almost done... Progress {}%", progressBar.getProgress());
            Thread.sleep(1500);
            progressBar.setValue(100);
            LOGGER.info("Job finished!");

        } catch (InterruptedException e) {
            LOGGER.info("Job interrupted!");
            Thread.currentThread().interrupt();
        }
    }
}
