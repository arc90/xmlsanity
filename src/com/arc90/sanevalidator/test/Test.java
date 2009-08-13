package com.arc90.sanevalidator.test;

import java.io.File;

import com.arc90.sanevalidator.ValidationResult;
import com.arc90.sanevalidator.Validator;

public class Test
{

	public static void main(String[] args) throws Exception
	{
		File schemaFile = new File(args[0]);
		
		File contentFile = new File(args[1]);	
		
		Validator validator = new Validator(schemaFile);
		
		ValidationResult result = validator.validate(contentFile);
		
		System.out.println(result);
	}

}
