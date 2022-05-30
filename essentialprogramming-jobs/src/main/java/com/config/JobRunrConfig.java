package com.config;

import com.crypto.Crypt;
import com.exceptions.codes.ErrorCode;
import com.spring.ApplicationContextFactory;
import com.util.cloud.ConfigurationManager;
import com.util.exceptions.ServiceException;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.jobrunr.configuration.JobRunr;
import org.jobrunr.configuration.JobRunrConfiguration;
import org.jobrunr.configuration.JobRunrMicroMeterIntegration;
import org.jobrunr.dashboard.JobRunrDashboardWebServerConfiguration;
import org.jobrunr.scheduling.JobRequestScheduler;
import org.jobrunr.scheduling.JobScheduler;
import org.jobrunr.server.JobActivator;
import org.jobrunr.storage.sql.common.SqlStorageProviderFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;
import java.security.GeneralSecurityException;

import static org.jobrunr.server.BackgroundJobServerConfiguration.usingStandardBackgroundJobServerConfiguration;
import static com.util.cloud.Environment.getProperty;

@Configuration
public class JobRunrConfig {

    final boolean isBackgroundJobServerEnabled = true; // or get it via ENV variables
    final boolean isDashboardEnabled = true; // or get it via ENV variables
    final MeterRegistry meterRegistry = new SimpleMeterRegistry();
    private static final com.util.cloud.Configuration configuration = ConfigurationManager.getConfiguration();
    private final String dashboardUser = getProperty("JOBRUNR_DASHBOARD_USER", configuration.getPropertyAsString("jobrunr.dashboard.user"));
    private final String encryptedDashboardPassword = getProperty("JOBRUNR_DASHBOARD_PASSWORD", configuration.getPropertyAsString("jobrunr.dashboard.password"));

    @Bean
    public JobRunrConfiguration.JobRunrConfigurationResult initJobRunner(final JobActivator jobActivator) {
        return JobRunr.configure()
                .useJobActivator(jobActivator)
                .useStorageProvider(SqlStorageProviderFactory
                        .using(ApplicationContextFactory.getBean(DataSource.class)))
                .useBackgroundJobServerIf(isBackgroundJobServerEnabled,
                        usingStandardBackgroundJobServerConfiguration()
                                .andWorkerCount(Runtime.getRuntime().availableProcessors())
                                .andPollIntervalInSeconds(15))
                .useDashboardIf(isDashboardEnabled, initJobRunrDashboard())
                .useMicroMeter(new JobRunrMicroMeterIntegration(meterRegistry))
                .useJmxExtensions()
                .initialize();
    }

    @Bean
    public JobScheduler initJobScheduler(JobRunrConfiguration.JobRunrConfigurationResult jobRunrConfigurationResult) {
        return jobRunrConfigurationResult.getJobScheduler();
    }

    @Bean
    public JobRequestScheduler initJobRequestScheduler(JobRunrConfiguration.JobRunrConfigurationResult jobRunrConfigurationResult) {
        return jobRunrConfigurationResult.getJobRequestScheduler();
    }

    @Bean
    public JobActivator jobActivator(final ApplicationContext applicationContext) {
        return applicationContext::getBean;
    }

    @Bean
    public JobRunrDashboardWebServerConfiguration initJobRunrDashboard()  {
        String decryptedPassword = decrypt(encryptedDashboardPassword);

        return JobRunrDashboardWebServerConfiguration
                .usingStandardDashboardConfiguration()
                .andPort(1000)
                .andBasicAuthentication(dashboardUser, decryptedPassword);
    }


    private String decrypt(final String value) {
        try {
            return Crypt.decrypt(value, "supercalifragilisticexpialidocious");
        } catch (GeneralSecurityException e) {
            throw new ServiceException(ErrorCode.UNABLE_TO_DECRYPT_PASSWORD, e);
        }
    }
}
