package com.config;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.api.env.resources.AppResources.DB_HOSTNAME;
import static com.api.env.resources.AppResources.DB_PASSWORD;
import static com.api.env.resources.AppResources.DB_USER;
import static java.lang.String.format;

@Slf4j
public class FlywayConfig {

    public static final String FLYWAY_DEFAULT_VERSION = "1";

    public static Flyway migrateDatabase() {
        final Flyway flyway = Flyway.configure()
                .dataSource(DB_HOSTNAME.value(), DB_USER.value(), DB_PASSWORD.value())
                .baselineVersion(FLYWAY_DEFAULT_VERSION)
                .baselineDescription("Init")
                .baselineOnMigrate(false)
                .locations("classpath:db/migration")
                .load();

        val info = flyway.info();

        //Find which versions are not applied yet.
        final MigrationInfo[] migrationInfo = info.pending();
        final String migrations =
                System.getProperty("line.separator") +
                        format("       Successfully validated %s migrations", info.applied().length) +
                        System.getProperty("line.separator") +
                        format("       %s  ", info.pending().length > 0
                                ?  info.pending().length + " migrations are pending"
                                : "No migration necessary."
                        ) +
                        System.getProperty("line.separator") +
                        format("%s", Arrays.stream(migrationInfo)
                                .filter(Objects::nonNull)
                                .map(migration -> {
                                    final String version = migration.getVersion().toString();
                                    return format("          ...applying migration %s", version);
                                })
                                .collect(Collectors.joining(System.getProperty("line.separator")))
                        );
        log.info(migrations);

        final boolean pending = info.pending().length > 0;
        if (pending) {
            flyway.migrate();
        }

        return flyway;
    }
}
