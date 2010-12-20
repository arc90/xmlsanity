package com.arc90.xmlsanity.util;

public class PoolException extends Exception
{

    private static final long serialVersionUID = 7690674632934696655L;

    public PoolException(String message)
    {
        super(message);
    }

    public PoolException(Throwable cause)
    {
        super(cause);
    }

    public PoolException(String message, Throwable cause)
    {
        super(message, cause);
    }

}
