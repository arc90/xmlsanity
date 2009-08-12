package com.arc90.sanevalidator;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.transform.Source;
import javax.xml.validation.Schema;

public interface Validator
{
	public ValidationResult validate(Source content) throws ValidationException;

	public void setSchema(Schema schema) throws IOException, FileNotFoundException;
}