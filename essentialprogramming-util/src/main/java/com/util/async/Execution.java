package com.util.async;

public class Execution<T> {

    /**
     * The execution result, if any
     */
    private T result;

    /**
     * The execution failure, if any
     */
    private Throwable failure;

    /**
     * Whether the execution was a success
     */
    private boolean successful;

    /* Whether the execution has been interrupted */
    private volatile boolean interrupted;

    private long startTime;
    private long endTime;

    private Execution(T result, Throwable failure, boolean success) {
        this.result = result;
        this.failure = failure;
        this.successful = success;
        this.startTime = System.currentTimeMillis();
    }

    public static <T> Execution<T> create() {
        return new Execution<>(null, null, false);
    }


    public boolean wasSuccessful() {
        return successful && !interrupted;
    }
    public void setSuccessful(boolean wasSuccessful) {
        this.successful = wasSuccessful;
    }

    public boolean wasInterrupted() {
        return interrupted;
    }
    public void setInterrupted(boolean interrupted) {
        this.interrupted = interrupted;
    }

    public T getResult() {
        return result;
    }
    public void setResult(T result) {
        this.result = result;
    }

    public Throwable getFailure() {
        return failure;
    }
    public void setFailure(Throwable failure) {
        this.failure = failure;
    }


    public long getStartTime() {
        return startTime;
    }
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }
    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

}
