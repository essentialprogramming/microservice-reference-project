package com.api.service;

import com.api.enums.CronEnum;
import com.api.service.jobs.DemoJob;
import com.api.service.jobs.execution.JobRunner;
import com.api.service.jobs.ProgressJob;
import com.util.web.JsonResponse;
import lombok.RequiredArgsConstructor;
import org.jobrunr.jobs.JobId;
import org.jobrunr.jobs.context.JobContext;
import org.jobrunr.scheduling.cron.Cron;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static com.api.service.jobs.DemoJob.Model;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JobService {

    private final JobRunner jobRunner;
    private final ProgressJob progressJob;
    private final DemoJob demoJob;

    public JsonResponse enqueueProgressJob() {
        final JobId jobId = jobRunner.enqueueJob(() ->
                progressJob.execute(progressJob.getName(), 100000000, JobContext.Null)
        );
        return new JsonResponse()
                .with("Status", "ok")
                .with("JobId", jobId.toString())
                .with("Message", "Job enqueued successfully!")
                .done();
    }


    public JsonResponse scheduleDemoJob(final String dateTime) {
        final JobId jobId = jobRunner.scheduleForExecution(dateTime, () ->
                demoJob.execute(
                        demoJob.getName(),
                        Model.builder().name("demo").build(),
                        JobContext.Null
                ));
        return new JsonResponse()
                .with("Status", "ok")
                .with("JobId", jobId.toString())
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
        jobRunner.scheduleRecurrently(cron,
                () -> demoJob.execute(
                        demoJob.getName(),
                        Model.builder().name("demo").build(),
                        JobContext.Null
                )
        );
    }

}
