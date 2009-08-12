package com.arc90.sanevalidator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.validation.Schema;

import org.jdom.Parent;

public class Validator
{
	private Schema schema;
	
	public Validator(File schemaFile)
	{
		// TODO Auto-generated constructor stub
	}

	public ValidationResult validate(Parent content) throws ValidationException
	{
		return validate(new Source(content));
	}
	
	public ValidationResult validate(javax.xml.transform.Source content) throws ValidationException
	{
		if (content instanceof Source == false)
		{
			throw new ValidationException("This method accepts only instances of com.arc90.sanevalidateor.jdom.Source");
		}
	
		// TODO: stub
		return null;
	}

	public synchronized void setSchema(Schema schema) throws IOException, FileNotFoundException
	{
		// TODO Auto-generated method stub
		
	}

}
