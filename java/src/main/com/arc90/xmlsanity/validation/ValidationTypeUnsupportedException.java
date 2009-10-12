/**
 * 
 */
package com.arc90.xmlsanity.validation;

/**
 * @author avi
 *
 */
class ValidationTypeUnsupportedException extends ValidationException
{

    /**
     * 
     */
    private static final long serialVersionUID = 5863237048070498222L;

    /**
     * 
     */
    public ValidationTypeUnsupportedException()
    {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     */
    public ValidationTypeUnsupportedException(String message)
    {
        super(message);
    }

    /**
     * @param cause
     */
    public ValidationTypeUnsupportedException(Throwable cause)
    {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public ValidationTypeUnsupportedException(String message, Throwable cause)
    {
        super(message, cause);
    }

}
