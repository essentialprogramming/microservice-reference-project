package org.jobrunr.scheduling;

import java.lang.reflect.Method;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.jobrunr.jobs.JobParameter;
import org.jobrunr.scheduling.annotations.Recurring;
import org.jobrunr.jobs.JobDetails;
import org.jobrunr.jobs.context.JobContext;
import org.jobrunr.scheduling.cron.CronExpression;
import org.jobrunr.scheduling.interval.Interval;
import org.jobrunr.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringValueResolver;

import static org.jobrunr.utils.StringUtils.isNotNullOrEmpty;


public class RecurringJobProcessor {
    private static final Logger log = LoggerFactory.getLogger(RecurringJobProcessor.class);

    private final JobScheduler jobScheduler;
    private final StringValueResolver embeddedValueResolver;


    public RecurringJobProcessor(JobScheduler jobScheduler, StringValueResolver valueResolver) {
        this.jobScheduler = jobScheduler;
        this.embeddedValueResolver = valueResolver;
    }


    public void doWith(final Method method) throws IllegalArgumentException {
        if (!method.isAnnotationPresent(Recurring.class)) {
            return;
        }
        if (hasParametersOutsideOfJobContext(method)) {
            throw new IllegalStateException("Methods annotated with " + Recurring.class.getName() + " can only have zero parameters or a single parameter of type JobContext.");
        }

        final Recurring recurringAnnotation = method.getAnnotation(Recurring.class);
        String id = getId(recurringAnnotation);
        String cron = resolveStringValue(recurringAnnotation.cron());
        String interval = resolveStringValue(recurringAnnotation.interval());

        if (StringUtils.isNullOrEmpty(cron) && StringUtils.isNullOrEmpty(interval))
            throw new IllegalArgumentException("Either cron or interval attribute is required.");
        if (isNotNullOrEmpty(cron) && isNotNullOrEmpty(interval))
            throw new IllegalArgumentException("Both cron and interval attribute provided. Only one is allowed.");

        if (Recurring.RECURRING_JOB_DISABLED.equals(cron) || Recurring.RECURRING_JOB_DISABLED.equals(interval)) {
            if (id == null) {
                log.warn("You are trying to disable a recurring job using placeholders but did not define an id.");
            } else {
                jobScheduler.delete(id);
            }
        } else {
            JobDetails jobDetails = getJobDetails(method);
            ZoneId zoneId = getZoneId(recurringAnnotation);
            if (isNotNullOrEmpty(cron)) {
                jobScheduler.scheduleRecurrently(id, jobDetails, CronExpression.create(cron), zoneId);
            } else {
                jobScheduler.scheduleRecurrently(id, jobDetails, new Interval(interval), zoneId);
            }
        }
    }

    private boolean hasParametersOutsideOfJobContext(Method method) {
        if (method.getParameterCount() == 0) {
            return false;
        } else if (method.getParameterCount() > 1) {
            return true;
        } else {
            return !method.getParameterTypes()[0].equals(JobContext.class);
        }
    }

    private String getId(Recurring recurringAnnotation) {
        String id = this.resolveStringValue(recurringAnnotation.id());
        return StringUtils.isNullOrEmpty(id) ? null : id;
    }

    private JobDetails getJobDetails(Method method) {
        List<JobParameter> jobParameters = new ArrayList<>();
        if(method.getParameterCount() == 1 && method.getParameterTypes()[0].equals(JobContext.class)) {
            jobParameters.add(JobParameter.JobContext);
        }
        final JobDetails jobDetails = new JobDetails(method.getDeclaringClass().getName(), null, method.getName(), jobParameters);
        jobDetails.setCacheable(true);
        return jobDetails;
    }

    private ZoneId getZoneId(Recurring recurringAnnotation) {
        String zoneId = this.resolveStringValue(recurringAnnotation.zoneId());
        return StringUtils.isNullOrEmpty(zoneId) ? ZoneId.systemDefault() : ZoneId.of(zoneId);
    }

    private String resolveStringValue(String value) {
        return this.embeddedValueResolver != null && value != null ? this.embeddedValueResolver.resolveStringValue(value) : value;
    }
}
