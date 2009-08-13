package com.arc90.sanevalidator;

import org.xml.sax.SAXParseException;

public class ValidationError
{
	private final String message;
	private int lineNumber = -1;
	private int columnNumber = -1;
	
	public ValidationError(String message)
	{
		this.message = message;
	}

	public ValidationError(SAXParseException exception)
	{
		this.message = exception.getMessage().split(":", 2)[1].trim();
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
