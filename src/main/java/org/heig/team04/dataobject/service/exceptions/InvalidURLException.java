package org.heig.team04.dataobject.service.exceptions;

import java.net.MalformedURLException;

/**
 * Exception thrown when the URL is invalid.
 * @author Ivan Vecerina, Yanik Lange
 * @version 1.0
 */
public class InvalidURLException extends ServiceException {
    public InvalidURLException(String url, MalformedURLException cause) {
        super("Invalid URL: " + url, cause);
    }
}
