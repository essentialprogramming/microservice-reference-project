package com.api.service.jobs;

import com.api.service.jobs.definition.DistributedJob;
import lombok.*;
import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.jobs.context.JobContext;
import org.jobrunr.jobs.context.JobDashboardProgressBar;
import org.jobrunr.jobs.context.JobRunrDashboardLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.locks.LockSupport;

import static com.api.service.jobs.DemoJob.Model;

@Component
@RequiredArgsConstructor
public class DemoJob implements DistributedJob<Model> {

    private static final Logger log = new JobRunrDashboardLogger(LoggerFactory.getLogger(DemoJob.class));

    @Override
    public String getName() {
        return "Demo job";
    }

    @Job(name = "%0", retries = 5)
    public void execute(String jobName, Model jobParameters, JobContext jobContext) {
        log.info("Job started...");

        final JobDashboardProgressBar progressBar = jobContext.progressBar(100);
        progressBar.setValue(0);

        parkThread(16000);
        progressBar.setValue(40);

        log.info("Executing job... Progress {}%", progressBar.getProgress());

        parkThread(16000);
        progressBar.setValue(80);

        log.info("Executing job... Progress {}%", progressBar.getProgress());


        parkThread(1500);

        log.info("Almost done... ");

        parkThread(300);
        progressBar.setValue(100);

        log.info("Job finished!");

    }

    private static void parkThread(final Integer ms) {

        final Duration parkPeriod = Duration.of(ms, ChronoUnit.MILLIS);
        final Thread current = Thread.currentThread();

        LockSupport.parkNanos(parkPeriod.toNanos());

        if (Thread.interrupted()) {
            current.interrupt();
        }
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Model{

        @lombok.NonNull
        String name;
    }
}

