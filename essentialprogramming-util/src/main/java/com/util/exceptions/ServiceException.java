package com.util.exceptions;


import static com.util.exceptions.ErrorCodes.ErrorCode;
/**
 * Wrapper Exception for service exceptions.
 * It will be converted into a response message with error code and message.
 *
 */
public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final ErrorCode failureCode;
    private final String failureDescription;
    private final Exception failureException;

    public ServiceException(final ErrorCode failureCode) {
        super(failureCode.getDescription());
        this.failureCode = failureCode;
        this.failureDescription = failureCode.getDescription();
        this.failureException = null;
    }

    public ServiceException(final ErrorCode failureCode, final String message) {
        super(failureCode.getDescription() + message);
        this.failureCode = failureCode;
        this.failureDescription = failureCode.getDescription() + message;
        this.failureException = null;
    }


    public ServiceException(final ErrorCode failureCode, final Exception failureException) {
        super(failureCode.getDescription());
        this.failureCode = failureCode;
        this.failureDescription = failureCode.getDescription();
        this.failureException = failureException;
    }

    /**
     * Get the failure code
     *
     * @return a code specifying the failure or null if command was successful
     */
    public ErrorCode getFailureCode() {
        return failureCode;
    }

    /**
     * Get info about the failure
     *
     * @return a failure description or null if not set
     */
    public String getFailureDescription() {
        return failureDescription;
    }

    /**
     * Get failure exception
     *
     * @return the failure exception or null if not set
     */
    public Exception getFailureException() {
        return failureException;
    }


}
