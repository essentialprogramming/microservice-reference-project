package com.api.service;

import com.api.enums.CronEnum;
import com.api.service.jobs.SleepJob;
import com.util.web.JsonResponse;
import lombok.RequiredArgsConstructor;
import org.jobrunr.jobs.context.JobContext;
import org.jobrunr.scheduling.BackgroundJob;
import org.jobrunr.scheduling.cron.Cron;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;


@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JobService {

    public JsonResponse enqueueJob() {
        BackgroundJob.enqueue(() -> SleepJob.executeJob(JobContext.Null));
        return new JsonResponse()
                .with("Status", "ok")
                .with("Message", "Job enqueued successfully!")
                .done();
    }

    public JsonResponse schedule(final String dateTime) {
        ZonedDateTime dateAndTime = parseTime(dateTime);
        BackgroundJob.schedule(dateAndTime, () -> SleepJob.executeJob(JobContext.Null));
        return new JsonResponse()
                .with("Status", "ok")
                .with("Message", "Job scheduled successfully!")
                .done();
    }

    public JsonResponse scheduleJobRecurrently(final CronEnum cronEnum) {
        switch (cronEnum) {
            case MINUTELY:
                createCronJob(Cron.minutely());
                break;
            case HOURLY:
                createCronJob(Cron.hourly());
                break;
            case DAILY:
                createCronJob(Cron.daily());
                break;
            case YEARLY:
                createCronJob(Cron.yearly());
                break;
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid cron type! Valid types: MINUTELY, HOURLY, DAILY, YEARLY.");
        }
        return new JsonResponse()
                .with("Status", "ok")
                .with("Message", "Job scheduled successfully!")
                .done();
    }

    private void createCronJob(final String cron) {
        BackgroundJob.scheduleRecurrently(String.valueOf(UUID.randomUUID()), cron, () -> SleepJob.executeJob(JobContext.Null));
    }

    private ZonedDateTime parseTime(final String dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").withZone(ZoneId.of("Europe/Bucharest"));

        try {
            return ZonedDateTime.parse(dateTime, formatter);
        } catch (DateTimeParseException e) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Invalid date and time format! Must be of format: dd/MM/yyyy HH:mm:ss");
        }
    }
}
