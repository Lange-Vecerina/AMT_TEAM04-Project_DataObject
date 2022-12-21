package org.heig.team04.dataobject.service.exceptions;

/**
 * Exception thrown when the URL is not accessible.
 * @author Ivan Vecerina, Yanik Lange
 * @version 1.0
 */
public class URLNotAccessibleException extends ServiceException {
    public URLNotAccessibleException(String url, Exception cause) {
        super("URL not accessible: " + url, cause);
    }
}
