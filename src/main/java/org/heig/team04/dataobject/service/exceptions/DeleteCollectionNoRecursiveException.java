package org.heig.team04.dataobject.service.exceptions;

/**
 * Exception thrown when trying to delete a collection without the recursive flag.
 * @author Ivan Vecerina, Yanik Lange
 * @version 1.0
 */
public class DeleteCollectionNoRecursiveException extends ServiceException {
    public DeleteCollectionNoRecursiveException(String uri) {
        super("This is a collection, you must use the recursive option to delete it: " + uri);
    }
}
