package com.arc90.xmlsanity.validation;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

class ErrorHandler implements org.xml.sax.ErrorHandler
{
	protected final ValidationResult validationResult;
	
	public ErrorHandler(ValidationResult validationResult)
	{
		this.validationResult = validationResult;
	}
	
	public void error(SAXParseException exception) throws SAXException
	{
		this.validationResult.addError(new ValidationError(exception));
	}

	public void fatalError(SAXParseException exception) throws SAXException
	{
		this.validationResult.addError(new ValidationError(exception));
	}
	
	public void warning(SAXParseException exception) throws SAXException
	{
	}

}
