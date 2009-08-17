package com.arc90.xmlsanity.validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class ValidationResult implements ErrorHandler
{
	private final List<ValidationError> errors = new ArrayList<ValidationError>();
		
	public boolean isValid() {
		return errors.size() == 0;
	}
	
	public Collection<ValidationError> getErrors()
	{
		return errors;
	}
	
	public void addError(String error)
	{
		errors.add(new ValidationError(error));
	}
	
	@Override
	public String toString()
	{
		if (isValid())
		{
			return "valid";
		}
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("invalid, because:\n");
		
		for (int i=0; i < errors.size(); i++)
		{
			ValidationError error = errors.get(i);
			
			sb.append(i+1);
			sb.append(". ");
			sb.append(error);
			sb.append("\n");
		}
		
		return sb.toString();
	}

	public String getErrorsAsHtmlList()
	{
		return getErrorsAsHtmlList(null);
	}
	
	public String getErrorsAsHtmlList(String classValue)
	{
		StringBuilder sb = new StringBuilder();
		
		if (classValue != null)
		{
			sb.append("<ol class=");
			sb.append('"');
			sb.append(classValue);
			sb.append('"');
			sb.append(">\n");
		}
		else
		{
			sb.append("<ol>\n");
		}
		
		for (ValidationError error : errors)
		{
			sb.append("\t<li>");
			sb.append(error);
			sb.append("</li>\n");
		}
		
		sb.append("</ol>");
		
		return sb.toString();
	}
	
	public void error(SAXParseException exception) throws SAXException
	{
		errors.add(new ValidationError(exception));
	}

	public void fatalError(SAXParseException exception) throws SAXException
	{
		errors.add(new ValidationError(exception));
	}
	
	public void warning(SAXParseException exception) throws SAXException
	{
	}

}
