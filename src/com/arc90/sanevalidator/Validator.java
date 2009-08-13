package com.arc90.sanevalidator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.jdom.Parent;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/***
 * Cacheable, fast, threadsafe, ultra-simple and ultra-usable XSD validator.
 * The idea is that you instantiate this class, and place the instance in an in-memory cache,
 * such as OSCache, EHcache, or even just a Map. Once that's done, multiple threads
 * can use the instance to validate XML documents or elements, without having to do any
 * complicated setup. Plus, the result, a ValidationResult object, makes any error messages
 * available in a simple, clear, readable, and useful way.  
 * @author avi
 *
 */
public class Validator
{
	protected final Schema schema;
	
	public Validator(File schemaFile) throws IOException, FileNotFoundException, SAXException
	{
		schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(schemaFile);
	}

	public ValidationResult validate(final Parent content) throws ValidationException
	{
		// TODO: stub
		return null;
	}
	
	public ValidationResult validate(final File content) throws ValidationException
	{
		try
		{
			Source source = new SAXSource(new InputSource(new FileReader(content)));
			return validate(source);
		}
		catch (FileNotFoundException e)
		{
			throw new ValidationException(e); 
		}
	}
	
	public ValidationResult validate(final String content) throws ValidationException
	{
		Source source = new SAXSource(new InputSource(new StringReader(content)));
		return validate(source);
	}
	
	protected ValidationResult validate(final Source source) throws ValidationException
	{
		javax.xml.validation.Validator validator = schema.newValidator();
		
		ValidationResult result = new ValidationResult();
		
		validator.setErrorHandler(result);
		
		try
		{
			validator.validate(source);
		}
		catch (SAXException e)
		{
			result.addError(e.getMessage());
		}
		catch (IOException e)
		{
			throw new ValidationException(e);
		}
		
		return result;
	}

	public ValidationResult validate(final byte[] content) throws ValidationException
	{
		// TODO: stub
		return null;
	}

	public ValidationResult validate(final InputStream content) throws ValidationException
	{
		// TODO: stub
		return null;
	}

	public ValidationResult validate(final org.w3c.dom.Document content) throws ValidationException
	{
		// TODO: stub
		return null;
	}

}
