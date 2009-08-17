package com.arc90.xml.validation;

import javax.xml.validation.Schema;
import javax.xml.validation.Validator;

import com.arc90.util.Pool;

class ValidatorPool extends Pool<javax.xml.validation.Validator>
{
	private final Schema schema;
	
	public ValidatorPool(Schema schema)
	{
		this.schema = schema;
	}

	@Override
	protected Validator create()
	{
		return schema.newValidator();
	}	
	
	@Override
	public void expire(Validator o)
	{		
	}

	@Override
	public boolean validate(Validator o)
	{
		return true;
	}

}
