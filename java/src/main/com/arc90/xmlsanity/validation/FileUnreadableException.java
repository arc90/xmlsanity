/**
 * 
 */
package com.arc90.xmlsanity.validation;

import java.io.IOException;

/**
 * @author avi
 * 
 */
public class FileUnreadableException extends IOException
{

    /**
     * 
     */
    private static final long serialVersionUID = 4246526169560880327L;

    /**
     * @param s
     */
    protected FileUnreadableException(String message)
    {
        super(message);
    }

    /**
     * Constructs a <code>FileUnreadableException</code> with a detail message
     * consisting of the given pathname string followed by the given reason
     * string. If the <code>reason</code> argument is <code>null</code> then it
     * will be omitted.
     * 
     * @since 1.2
     */
    protected FileUnreadableException(String path, String reason)
    {
        super(path + ((reason == null) ? "" : " (" + reason + ")"));
    }

}
