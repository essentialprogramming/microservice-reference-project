package com.util.date;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtil {

    public static boolean isBefore(String first, String second) {
        LocalDate d1 = LocalDate.parse(first);
        LocalDate d2 = LocalDate.parse(second);

        int compare = d1.compareTo(d2);
        return compare < 0;
    }

    public static boolean isLater(String first, String second) {
        LocalDate d1 = LocalDate.parse(first);
        LocalDate d2 = LocalDate.parse(second);

        int compare = d1.compareTo(d2);
        return compare > 0;
    }

    public static String format(final LocalDateTime date, final String pattern) {
        if (date == null) {
            return "";
        }
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static Date convertToDateViaSqlDate(final LocalDate dateToConvert) {
        return java.sql.Date.valueOf(dateToConvert);
    }

    public static LocalDate convertToLocalDateViaInstant(
            final Date dateToConvert
    ) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    /**
     * Returns the {@link Date} of midnight at the start of the given {@link Date}.
     *
     * <p>This returns a {@link Date} formed from the given {@link Date} at the time of midnight,
     * 00:00, at the start of this {@link Date}.
     *
     * @return the {@link Date} of midnight at the start of the given {@link Date}
     */
    public static Date startOfDay(final Date date) {
        final LocalDate localDate = convertToLocalDateViaInstant(date);
        final LocalDateTime startOfDay = localDate.atStartOfDay();

        return Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Returns the {@link Date} at the end of day of the given {@link Date}.
     *
     * <p>This returns a {@link Date} formed from the given {@link Date} at the time of 1 millisecond
     * prior to midnight the next day.
     *
     * @return the {@link Date} at the end of day of the given {@link Date}j
     */
    public static Date endOfDay(final Date date) {
        final LocalDate localDate = convertToLocalDateViaInstant(date);
        final LocalDateTime endOfDay = localDate.atTime(LocalTime.MAX);

        return Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }


    public static LocalDateTime startOfDay(final Clock clock) {
        final LocalDate currentDate = LocalDate.now(clock);
        return currentDate
                .atStartOfDay();
    }

    public static LocalDateTime endOfDay(final Clock clock) {
        final LocalDate currentDate = LocalDate.now(clock);
        return currentDate
                //.plusDays(1).atStartOfDay().minusNanos(1);
                .atTime(LocalTime.MAX);

    }

    public static OffsetDateTime startOfDay(final LocalDate date) {
        return date.atTime(OffsetTime.MIN);
    }

    public static OffsetDateTime startOfDay1(final LocalDate date) {
        final Instant startDate = Instant.parse(date.toString()+"T00:00:00Z");
        return startDate.atOffset(ZoneOffset.UTC);

    }

    public static OffsetDateTime startOfDay2(final LocalDate date) {
        return date.atStartOfDay(ZoneOffset.UTC).withEarlierOffsetAtOverlap().toOffsetDateTime();

    }

    public static OffsetDateTime endOfDay(final LocalDate date) {
        return date.plusDays(1).atTime(OffsetTime.MIN);
    }

    public static OffsetDateTime endOfDay1(final LocalDate date) {
        final Instant endDate = Instant.parse(date.toString()+"T23:59:59Z");
        return endDate.atOffset(ZoneOffset.UTC);

    }

    public static OffsetDateTime endOfDay2(final LocalDate date) {
        return date.atTime(LocalTime.MAX).atZone(ZoneOffset.UTC).toOffsetDateTime();

    }
}
