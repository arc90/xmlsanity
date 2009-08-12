package com.arc90.sanevalidator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.validation.Schema;

import org.jdom.Parent;

public class Validator
{
	private final Schema schema;
	
	public Validator(File schemaFile) throws IOException, FileNotFoundException
	{
		// TODO: stub
		schema = null;
	}

	public ValidationResult validate(Parent content) throws ValidationException
	{
		// TODO: stub
		return null;
	}
	
	public ValidationResult validate(String content) throws ValidationException
	{
		// TODO: stub
		return null;
	}

	public ValidationResult validate(byte[] content) throws ValidationException
	{
		// TODO: stub
		return null;
	}

	public ValidationResult validate(InputStream content) throws ValidationException
	{
		// TODO: stub
		return null;
	}

	public ValidationResult validate(org.w3c.dom.Document content) throws ValidationException
	{
		// TODO: stub
		return null;
	}

}
