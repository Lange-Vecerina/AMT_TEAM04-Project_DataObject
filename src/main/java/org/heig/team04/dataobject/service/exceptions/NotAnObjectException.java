package org.heig.team04.dataobject.service.exceptions;

/**
 * Exception thrown when the object is not an object but a collection.
 * @author Ivan Vecerina, Yanik Lange
 * @version 1.0
 */
public class NotAnObjectException extends ServiceException {
    public NotAnObjectException(String uri) {
        super("This point to a collection, not an object: " + uri);
    }
}
