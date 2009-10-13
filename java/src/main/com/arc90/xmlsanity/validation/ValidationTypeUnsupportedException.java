/**
 * 
 */
package com.arc90.xmlsanity.validation;

/**
 * @author avi
 *
 */
public class ValidationTypeUnsupportedException extends ValidationException
{

    /**
     * 
     */
    private static final long serialVersionUID = 5863237048070498222L;

    /**
     * @param message
     */
    protected ValidationTypeUnsupportedException(String message)
    {
        super(message);
    }

    /**
     * @param cause
     */
    protected ValidationTypeUnsupportedException(Throwable cause)
    {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    protected ValidationTypeUnsupportedException(String message, Throwable cause)
    {
        super(message, cause);
    }

}
