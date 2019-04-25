package com.lwp.docker.client.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by roger on 2016/9/23.
 */
public class CommonException extends RuntimeException {
    private final static Logger logger = LoggerFactory.getLogger(CommonException.class);

    public enum Status {
        NODFOUND,
        BADQUEST;
    }

    private Status status;

    public CommonException() {
        super();
    }

    public CommonException(Throwable cause) {
        super(cause);
        if (cause != null && cause instanceof CommonException) {
        } else {
            logger.error("", cause);
        }
    }

    public CommonException(String message) {
        super(message);
        logger.error(message);
    }

    public CommonException(String message, Throwable cause) {
        super(message, cause);
        if (cause != null && cause instanceof CommonException) {
            logger.error(message);
        } else {
            logger.error(message, cause);
        }
    }

    public CommonException setStatus(Status status) {
        this.status = status;
        return this;
    }

    public static void throwEx(Status status, String message) {
        logger.error(message);
        throw new CommonException(message).setStatus(status);
    }

    public static void throwEx(String message) {
        throw new CommonException(message);
    }

    public static void throwEx(Exception ex) {
        CommonException commonException = null;
        if (ex instanceof CommonException) {
            commonException = (CommonException) ex;
        } else {
            commonException = new CommonException(ex);
        }
        throw commonException;
    }

    public static void throwEx(String message, Throwable cause) {
        throw new CommonException(message, cause);
    }

    public Status getStatus() {
        return status;
    }
}
