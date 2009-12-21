package com.arc90.xmlsanity.validation;

import org.xml.sax.SAXParseException;

/**
 * Provides details about a single error. 
 * 
 * @author Avi Flax
 */
public class ValidationError
{
	private final String message;
	private int lineNumber = -1;
	private int columnNumber = -1;
	
	protected ValidationError(String message)
	{
		this.message = message;
	}

	protected ValidationError(SAXParseException exception)
	{
		if (exception.getMessage().contains(":"))
		{
			this.message = exception.getMessage().split(":", 2)[1].trim();	
		}
		else
		{
			this.message = exception.getMessage();
		}
		
		this.lineNumber = exception.getLineNumber();
		this.columnNumber = exception.getColumnNumber();
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append(this.message);
		
		if (lineNumber > 0 && columnNumber > 0)
		{
			sb.append(" Line ");
			sb.append(lineNumber);
			sb.append(", column ");
			sb.append(columnNumber);
			sb.append(".");
		}
		
		return sb.toString();
	}
	
	public String getMessage()
	{
		return message;
	}

	public int getLineNumber()
	{
		return lineNumber;
	}

	public int getColumnNumber()
	{
		return columnNumber;
	}
	
}
