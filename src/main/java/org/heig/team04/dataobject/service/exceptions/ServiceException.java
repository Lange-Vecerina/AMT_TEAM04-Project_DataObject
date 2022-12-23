package org.heig.team04.dataobject.service.exceptions;

/**
 * Base class for all exceptions thrown by the service layer.
 * @author Ivan Vecerina, Yanik Lange
 * @version 1.0
 */
public class ServiceException extends Exception {
    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}

