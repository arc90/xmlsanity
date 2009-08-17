package com.arc90.xml.transformation;

import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;

import com.arc90.util.Pool;

class TransformerPool extends Pool<javax.xml.transform.Transformer>
{
	private final Templates templates;
	
	public TransformerPool(Templates templates)
	{
		this.templates = templates;
	}

	@Override
	protected javax.xml.transform.Transformer create()
	{
		try
		{
			return templates.newTransformer();
		}
		catch (TransformerConfigurationException e)
		{
			throw new RuntimeException(e);
		}
	}	
	
	@Override
	public void expire(javax.xml.transform.Transformer o)
	{		
	}

	@Override
	public boolean validate(javax.xml.transform.Transformer o)
	{
		return true;
	}

}
