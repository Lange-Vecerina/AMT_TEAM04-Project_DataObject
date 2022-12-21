package org.heig.team04.dataobject.service.exceptions;

/**
 * An exception thrown when the object already exists.
 * @author Ivan Vecerina, Yanik Lange
 * @version 1.0
 */
public class AlreadyExistsException extends ServiceException {
    public AlreadyExistsException(String uri) {
        super("Object already exists: " + uri);
    }
}
