package com.arc90.xmlsanity.transformation;

public class TransformationException extends Exception
{

    private static final long serialVersionUID = -2347256368493384122L;

    public TransformationException(String message)
    {
        super(message);
    }

    public TransformationException(Throwable cause)
    {
        super(cause);
    }

    public TransformationException(String message, Throwable cause)
    {
        super(message, cause);
    }

}
