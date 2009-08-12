package com.arc90.sanevalidator;

import java.util.Collection;

public class ValidationResult
{
	private final boolean valid;
	private final Collection<ValidationError> errors;
	
	public ValidationResult(boolean valid)
	{
		this.valid = valid;
		
		// TODO: stub
		this.errors = null;
	}
	
	public boolean isValid() {
		return valid;
	}
	
	public Collection<ValidationError> getErrors()
	{
		return errors;
	}
	
	@Override
	public String toString()
	{
		// TODO Auto-generated method stub
		return super.toString();
	}
}
