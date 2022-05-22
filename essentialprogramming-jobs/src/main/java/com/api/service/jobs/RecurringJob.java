package com.api.service.jobs;

import lombok.RequiredArgsConstructor;
import org.jobrunr.jobs.context.JobContext;
import org.jobrunr.jobs.context.JobDashboardProgressBar;
import org.jobrunr.jobs.context.JobRunrDashboardLogger;
import org.jobrunr.scheduling.JobScheduler;
import org.jobrunr.scheduling.annotations.Recurring;
import org.jobrunr.jobs.annotations.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.locks.LockSupport;

@Component
@RequiredArgsConstructor
public class RecurringJob {

    private static final Logger log = new JobRunrDashboardLogger(LoggerFactory.getLogger(RecurringJob.class));

    private final JobScheduler jobScheduler;

    @Recurring(id = "my-recurring-job", cron = "#{T(com.env.resources.JobResources).RECURRENT_JOB.value()}")
    @Job(name = "A recurring job", retries = 5)
    public void execute() {
        log.info("Recurring job has started..");

        parkThread(12000);
        log.info("Executing job... Progress {}%", 30);

        parkThread(9000);
        log.info("Executing job... Progress {}%", 70);


        parkThread(1500);
        log.info("Almost done... ");

        parkThread(300);
        log.info("Recurring job has finished...");

    }

    private static void parkThread(final Integer ms) {

        final Duration parkPeriod = Duration.of(ms, ChronoUnit.MILLIS);
        final Thread current = Thread.currentThread();

        LockSupport.parkNanos(parkPeriod.toNanos());

        if (Thread.interrupted()) {
            current.interrupt();
        }
    }
}