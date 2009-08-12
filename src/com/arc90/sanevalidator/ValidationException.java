package com.arc90.sanevalidator;

public class ValidationException extends Exception
{

	private static final long serialVersionUID = 3815373749313675332L;

	public ValidationException(String message)
	{
		super(message);
	}

	public ValidationException(Throwable cause)
	{
		super(cause);
	}

	public ValidationException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
