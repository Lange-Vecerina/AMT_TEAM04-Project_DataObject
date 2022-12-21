package org.heig.team04.dataobject.service.exceptions;

/**
 * Base class for all exceptions thrown by the External service used by the service layer.
 * @author Ivan Vecerina, Yanik Lange
 * @version 1.0
 */
public class ExternalServiceException extends ServiceException {
    public ExternalServiceException(Throwable cause) {
        super("Internal service error", cause);
    }
}
