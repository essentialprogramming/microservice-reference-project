package com.util.logging;

import com.util.number.NumberUtils;
import com.util.text.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.TimeUnit;

/**
 * Progress logger.
 *
 * This class provides a simple way to log progress information of potentially long running activities.
 * Example usage:
 *    final ProgressLogger progressLogger = new ProgressLogger(logger, 1, TimeUnit.MINUTES);
 *    progressLogger.start("Crafting missions...");
 *    ... iterate on missions and call progressLogger.update() on each mission ...
 *    progressLogger.done();
 *
 *    --------------------------------------------------------------------------------------------
 *
 *    final ProgressLogger progressLogger = new ProgressLogger();
 *    progressLogger.start("Crafting missions...");
 *    ... iterate on missions (no update() calls) ...
 *    progressLogger.done(howManyMissions);
 */

public final class ProgressLogger {

    private static final long DEFAULT_LOG_FREQUENCY = 10; //Every 10 seconds
    private static final Runtime RUNTIME = Runtime.getRuntime();
    private static final String NO_MESSAGE = "";

    /**
     * The SLF4J logger used by this progress logger.
     */
    private final Logger logger;

    /**
     * The time interval for a new log in milliseconds.
     */
    private long logInterval;

    /**
     * Whether to display the free memory at each progress log (default: false).
     */
    private boolean displayFreeMemory;

    /**
     * Whether to display additionally the local speed, that is, the detected speed between two consecutive logs, as opposed to the average speed since start (default: false).
     */
    private boolean displayLocalSpeed;

    /**
     * The number of calls to `update()` since the last `start()`
     */
    private long count;

    /**
     * The value of {@link #count} at the last call to {@link #updateInternal(long)} (i.e., at the last output).
     */
    private long lastCount;

    /**
     * The number of expected calls to {@link #update()} (used to compute the percentages, ignored if negative).
     */
    private long expectedUpdates;

    /**
     * A fixed time unit for printing the speed.
     */
    private TimeUnit speedTimeUnit;

    /**
     * A fixed time unit for printing the timing of an item.
     */
    private TimeUnit itemTimeUnit;

    /**
     * The name of counted items.
     */
    private final String itemsName;
    private final String itemName;

    private long startTime;
    private long stopTime;
    private long lastLogTime;


    /**
     * Calls to `lightUpdate()` will cause a call to {@link System#currentTimeMillis()}
     * only if the current value of `count`is a multiple of this mask plus one.
     */
    public final int LIGHT_UPDATE_MASK = (1 << 10) - 1;

    public ProgressLogger() {
        this(LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME));
    }

    public ProgressLogger(final String itemsName) {
        this(LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME), itemsName);
    }

    public ProgressLogger(final Logger logger) {
        this(logger, DEFAULT_LOG_FREQUENCY, TimeUnit.SECONDS);
    }

    public ProgressLogger(final Logger logger, final String itemsName) {
        this(logger, DEFAULT_LOG_FREQUENCY, TimeUnit.SECONDS, itemsName);
    }

    public ProgressLogger(final Logger logger, final long logInterval, final TimeUnit timeUnit) {
        this(logger, logInterval, timeUnit, "items");
    }

    /**
     * Creates a new progress logger.
     *
     * @param logger      the logger to which messages will be sent.
     * @param logInterval the logging interval.
     * @param timeUnit    the unit of time of {@code logInterval}.
     * @param itemsName   a plural name denoting the counted items.
     */
    public ProgressLogger(final Logger logger, final long logInterval, final TimeUnit timeUnit, final String itemsName) {
        this.logger = logger;
        this.logInterval = timeUnit.toMillis(logInterval);
        this.itemsName = itemsName;
        this.itemName = StringUtils.singularize(itemsName);
        this.expectedUpdates = -1;
    }

    /**
     * Updates the internal count of this progress logger by adding one; if enough time has passed since the
     * last log, information will be logged.
     *
     * <p>This method performs a call to {@link System#currentTimeMillis()} that takes microseconds (not nanoseconds).
     * If you plan on calling this method more than a few hundred times per second, you should use {@link #lightUpdate()}.
     */
    public void update() {
        update(1);
    }

    /**
     * Updates the internal count of this progress logger by adding a specified value; if enough time has passed since the
     * last log, information will be logged.
     */
    public void update(final long value) {
        set(this.count + value);
    }

    /**
     * Sets the internal count of this progress logger to a specified value; if enough time has passed since the
     * last log, information will be logged.
     */
    public void set(final long count) {
        this.count = count;
        final long time = System.currentTimeMillis();
        if (time - lastLogTime >= logInterval){
            updateInternal(time);
        }
    }

    /**
     * Updates the internal count of this progress logger by adding one, forcing a display.
     */
    public void updateAndDisplay() {
        updateAndDisplay(1);
    }

    /**
     * Updates the internal count of this progress logger by adding a specified value, forcing a display.
     */
    public void updateAndDisplay(final long count) {
        setAndDisplay(this.count + count);
    }

    /**
     * Sets the internal count of this progress logger to a specified value, forcing a display.
     */
    public void setAndDisplay(final long count) {
        this.count = count;
        updateInternal(System.currentTimeMillis());
    }

    private String itemsPerTimeInterval(final long startCount, final long currentTime, final long baseTime) {
        final double secondsPerItem = ((count - startCount) * 1000.0) / (currentTime - baseTime);
        if (speedTimeUnit == TimeUnit.SECONDS || speedTimeUnit == null && secondsPerItem >= 1)
            return NumberUtils.format(secondsPerItem) + " " + itemsName + "/s";
        if (speedTimeUnit == TimeUnit.MINUTES || speedTimeUnit == null && secondsPerItem * 60 >= 1)
            return NumberUtils.format(secondsPerItem * 60) + " " + itemsName + "/m";
        if (speedTimeUnit == TimeUnit.HOURS || speedTimeUnit == null && secondsPerItem * 3600 >= 1)
            return NumberUtils.format(secondsPerItem * 3600) + " " + itemsName + "/h";
        return NumberUtils.format(secondsPerItem * 86400) + " " + itemsName + "/d";
    }

    private String timePerItem(final long startCount, final long currentTime, final long baseTime) {
        final double secondsPerItem = (currentTime - baseTime) / ((count - startCount) * 1000.0);
        if (itemTimeUnit == null && secondsPerItem >= 86400)
            return NumberUtils.format(secondsPerItem / 86400) + " d/" + itemName;

        if (itemTimeUnit == TimeUnit.HOURS || itemTimeUnit == null && secondsPerItem >= 3600)
            return NumberUtils.format(secondsPerItem / 3600) + " h/" + itemName;
        if (itemTimeUnit == TimeUnit.MINUTES || itemTimeUnit == null && secondsPerItem >= 60)
            return NumberUtils.format(secondsPerItem / 60) + " m/" + itemName;
        if (itemTimeUnit == TimeUnit.SECONDS || itemTimeUnit == null && secondsPerItem >= 1)
            return NumberUtils.format(secondsPerItem) + " s/" + itemName;

        if (itemTimeUnit == TimeUnit.MILLISECONDS || itemTimeUnit == null && secondsPerItem >= 1E-3)
            return NumberUtils.format(secondsPerItem * 1E3) + " ms/" + itemName;
        if (itemTimeUnit == TimeUnit.MICROSECONDS || itemTimeUnit == null && secondsPerItem >= 1E-6)
            return NumberUtils.format(secondsPerItem * 1E6) + " \u00b5s/" + itemName;
        return NumberUtils.format(secondsPerItem * 1E9) + " ns/" + itemName;
    }

    private void updateInternal(final long currentTime) {
        final long millisToEnd = Math.round((expectedUpdates - count) * ((currentTime - startTime) / (count + 1.0)));
        // Formatting is expensive, so we check for actual logging.
        if (logger.isInfoEnabled())
            logger.info(NumberUtils.format(count) + " " + itemsName + ", " +
                    formatDuration(millis()) + ", " + itemsPerTimeInterval(0, currentTime, startTime) + ", " + timePerItem(0, currentTime, startTime) +
                    (displayLocalSpeed ? " [" + itemsPerTimeInterval(lastCount, currentTime, lastLogTime) + ", " + timePerItem(lastCount, currentTime, lastLogTime) + "]" : "") +
                    (expectedUpdates > 0 ? "; " + NumberUtils.format((100 * count) / expectedUpdates) + "% done, " +
                            formatDuration(millisToEnd) + " to end" : "") + freeMemory() + "; ");

        lastLogTime = currentTime;
        lastCount = count;
    }

    /**
     *
     * This call updates the progress logger internal counter as {@link #update()}. However,
     * it will actually call {@link System#currentTimeMillis()} only if the new {@link #count}
     * is a multiple of `LIGHT_UPDATE_MASK + 1`. This mechanism makes it possible to reduce the number of
     * calls to {@link System#currentTimeMillis()} significantly.
     *
     * This method is useful when the operations being counted take less than a few microseconds.
     *
     */
    public final void lightUpdate() {
        if ((++count & LIGHT_UPDATE_MASK) == 0) {
            final long currentTime = System.currentTimeMillis();
            if (currentTime - lastLogTime >= logInterval) updateInternal(currentTime);
        }
    }


    /**
     * Starts the progress logger, resetting the count.
     */
    public void start() {
        start(NO_MESSAGE);
    }

    /**
     * Starts the progress logger, displaying a message and resetting the counter.
     *
     * @param message the message to display.
     */
    public void start(final CharSequence message) {
        if (message != NO_MESSAGE) {
            logger.info(message.toString());
        }
        startTime = lastLogTime = System.currentTimeMillis();
        lastCount = count = 0;
        stopTime = -1;
    }

    /**
     * Stops the progress logger.
     */
    private void stop() {
        stop(NO_MESSAGE);
    }

    /**
     * Stops the progress logger, displaying a message.
     *
     * @param message the message to display.
     */
    private void stop(final CharSequence message) {
        if (stopTime != -1) return; //already stopped
        if (message != NO_MESSAGE) {
            logger.info(message.toString());
        }
        stopTime = System.currentTimeMillis();
        expectedUpdates = -1;
    }


    /**
     * Completes a run of this progress logger.
     */
    public void done() {
        stop("Completed.");
        prettyPrint();
    }

    /**
     * Completes a run of this progress logger and sets the internal counter.
     *
     * @param count will replace the internal counter value.
     */
    public void done(final long count) {
        this.count = count;
        stop("Completed.");
        prettyPrint();
    }

    /**
     * Returns the number of milliseconds between present time and the last call to {@link #start()}
     */
    private long millis() {
        return (stopTime != -1) ? stopTime - startTime : System.currentTimeMillis() - startTime;
    }

    private String formatDuration(final long millis) {
        final long seconds = (millis / 1000) % 60;
        final long minutes = ((millis / 1000) / 60) % 60;
        final long hours = millis / (3600 * 1000);

        if (millis < 1000)
            return millis + "ms";
        if (hours == 0 && minutes == 0)
            return seconds + "s";
        if (hours == 0)
            return minutes + "m " + seconds + "s";
        return hours + "h " + minutes + "m " + seconds + "s";
    }

    /**
     * Prints current data in a pretty form.
     */
    public void prettyPrint() {
        final long time = (stopTime != -1 ? stopTime : System.currentTimeMillis()) - startTime + 1;

        if (time <= 0)
            logger.info("Nothing to print");

        logger.info("Elapsed: " + formatDuration(time) + (count != 0 ? " [" + NumberUtils.format(count) + " " + itemsName + ", " + itemsPerTimeInterval(0, stopTime, startTime) + ", " + timePerItem(0, stopTime, startTime) + "]" : "") + freeMemory());
    }

    private String freeMemory() {
        return (displayFreeMemory ? "; Memory - used/available/free/total/max: "
                + NumberUtils.formatSize(RUNTIME.totalMemory() - RUNTIME.freeMemory()) + "/"
                + NumberUtils.formatSize(RUNTIME.freeMemory() + (RUNTIME.maxMemory() - RUNTIME.totalMemory())) + "/"
                + NumberUtils.formatSize(RUNTIME.freeMemory()) + "/"
                + NumberUtils.formatSize(RUNTIME.totalMemory()) + "/"
                + NumberUtils.formatSize(RUNTIME.maxMemory()) : "");
    }


    public ProgressLogger displayFreeMemory(boolean display) {
        displayFreeMemory = display;
        return this;
    }

    public ProgressLogger displayLocalSpeed(boolean display) {
        displayLocalSpeed = display;
        return this;
    }

    public ProgressLogger expectedUpdates(long expectedUpdates) {
        this.expectedUpdates = expectedUpdates;
        return this;
    }

    public ProgressLogger logFrequency(long frequency, TimeUnit timeUnit) {
        this.logInterval = timeUnit.toMillis(frequency);
        return this;
    }
}