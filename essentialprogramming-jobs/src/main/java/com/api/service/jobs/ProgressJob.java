package com.api.service.jobs;

import com.api.service.jobs.definition.DistributedJob;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.util.logging.ProgressLogger;
import com.util.random.XORShiftRandom;
import lombok.RequiredArgsConstructor;
import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.jobs.context.JobContext;
import org.jobrunr.jobs.context.JobDashboardProgressBar;
import org.jobrunr.jobs.context.JobRunrDashboardLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class ProgressJob implements DistributedJob<Integer> {

    private static final Logger log = new JobRunrDashboardLogger(LoggerFactory.getLogger(ProgressJob.class));


    @Override
    public String getName() {
        return "Generate nonce job";
    }

    @Job(name = "%0", retries = 5)
    public void execute(final String jobName, final Integer jobParameters, final JobContext jobContext) {
        executeJob(jobParameters, jobContext);
    }

    private void executeJob(final Integer itemsToProcess,  final JobContext jobContext) {
        log.info("Job started...");

        final JobDashboardProgressBar progressBar = jobContext.progressBar(itemsToProcess);

        progressBar.setValue(0);
        final ProgressLogger progressLogger = new ProgressLogger(log)
                .displayFreeMemory(true)
                .displayLocalSpeed(true)
                .logFrequency(3, TimeUnit.SECONDS)
                .expectedUpdates(itemsToProcess);
        progressLogger.start("Generating nonce's...");

        for (int i = itemsToProcess; i-- != 0; ) {
            final Random random = new XORShiftRandom();
            final String nonce = NanoIdUtils.randomNanoId(random,
                    NanoIdUtils.DEFAULT_ALPHABET,
                    NanoIdUtils.DEFAULT_SIZE);

            progressBar.increaseByOne();
            progressLogger.update();
        }
        progressLogger.done();

        log.info("Job ended..");
    }

}
