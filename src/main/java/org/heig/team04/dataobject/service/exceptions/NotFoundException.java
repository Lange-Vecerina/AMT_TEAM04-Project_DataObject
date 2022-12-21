package org.heig.team04.dataobject.service.exceptions;

/**
 * Exception thrown when the URL is not accessible.
 * @author Ivan Vecerina, Yanik Lange
 * @version 1.0
 */
public class NotFoundException extends ServiceException {
    public NotFoundException(String uri) {
        super("Object doesn't exist: " + uri);
    }
}
