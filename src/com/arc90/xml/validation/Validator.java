package com.arc90.xml.validation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Parent;
import org.jdom.output.XMLOutputter;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/***
 * Cacheable, fast, threadsafe, ultra-simple and ultra-usable XSD validator. The
 * idea is that you instantiate this class, and place the instance in an
 * in-memory cache, such as OSCache, EHcache, or even just a Map. Once that's
 * done, multiple threads can use the instance to validate XML documents or
 * elements, without having to do any complicated setup. Plus, the result, a
 * ValidationResult object, makes any error messages available in a simple,
 * clear, readable, and useful way.
 * 
 * @author Avi Flax <avif@arc90.com>
 * 
 */
public class Validator
{
	protected final Schema schema;
	protected final ValidatorPool validatorPool;

	public Validator(File schemaFile) throws FileNotFoundException, SAXException
	{
		if (schemaFile.exists() == false)
		{
			throw new FileNotFoundException("The file " + schemaFile.getAbsolutePath() + " does not exist.");
		}
		
		schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(schemaFile);
		
		validatorPool = new ValidatorPool(schema);
	}

	public ValidationResult validate(Parent content) throws ValidationException
	{
		if (content instanceof Document)
		{
			return validate(new XMLOutputter().outputString((Document) content));
		}
		else
		{
			return validate(new XMLOutputter().outputString((Element) content));
		}
	}

	public ValidationResult validate(File content) throws ValidationException
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

	public ValidationResult validate(String content) throws ValidationException
	{
		Source source = new SAXSource(new InputSource(new StringReader(content)));
		return validate(source);
	}

	public ValidationResult validate(byte[] content) throws ValidationException
	{
		return validate(new String(content));
	}

	public ValidationResult validate(InputStream content) throws ValidationException
	{
		Source source = new SAXSource(new InputSource(content));
		return validate(source);
	}

	public ValidationResult validate(org.w3c.dom.Document content) throws ValidationException
	{
		Source source = new DOMSource(content);
		return validate(source);
	}

	protected ValidationResult validate(Source source) throws ValidationException
	{
		javax.xml.validation.Validator validator = getValidator();

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
		finally
		{
			returnValidator(validator);
		}

		return result;
	}

	protected javax.xml.validation.Validator getValidator()
	{
		return validatorPool.checkOut();
	}
	
	protected void returnValidator(javax.xml.validation.Validator validator)
	{
		validatorPool.checkIn(validator);
	}

}