package com.api.service.jobs.execution;

import com.api.service.jobs.definition.JobExecution;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import org.jobrunr.jobs.JobId;
import org.jobrunr.scheduling.BackgroundJob;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Execution engine.
 * <p>
 * The main responsibility of execution engine is jobs execution.
 * <p>
 * Execution engine also covers exception handling it means it catches all possible exceptions and manages the states of
 * jobs.
 * <p>
 * Execution engine is always called via job runner.
 * <p>
 * Execution engine also covers all logging requirements.
 */
@Service
public class JobExecutionEngine {

    public <T> JobId enqueueJob(final JobExecution<T> jobExecution) {
        return BackgroundJob.enqueue(jobExecution);
    }

    public <T> JobId scheduleForExecution(final String dateTime, final JobExecution<T> jobExecution) {
        return BackgroundJob.schedule(parseTime(dateTime), jobExecution);
    }

    public <T> void scheduleRecurrently(final String cron, final JobExecution<T> jobExecution) {
        BackgroundJob.scheduleRecurrently(NanoIdUtils.randomNanoId(), cron, jobExecution);
    }


    private static ZonedDateTime parseTime(final String dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").withZone(ZoneId.of("Europe/Bucharest"));

        try {
            return ZonedDateTime.parse(dateTime, formatter);
        } catch (DateTimeParseException e) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Invalid date and time format! Must be of format: dd/MM/yyyy HH:mm:ss");
        }
    }
}
