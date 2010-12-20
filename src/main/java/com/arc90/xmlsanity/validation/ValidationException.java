package com.arc90.xmlsanity.validation;

public class ValidationException extends Exception
{
	private static final long serialVersionUID = 3815373749313675332L;

	protected ValidationException()
    {
        super();
    }
	
	protected ValidationException(String message)
	{
		super(message);
	}

	protected ValidationException(Throwable cause)
	{
		super(cause);
	}

	protected ValidationException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
