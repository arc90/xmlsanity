package com.arc90.sanevalidator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.validation.Schema;

import org.jdom.Parent;

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
	private final Schema schema;
	
	public Validator(File schemaFile) throws IOException, FileNotFoundException
	{
		// TODO: stub
		schema = null;
	}

	public ValidationResult validate(final Parent content) throws ValidationException
	{
		// TODO: stub
		return null;
	}
	
	public ValidationResult validate(final String content) throws ValidationException
	{
		// TODO: stub
		return null;
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
