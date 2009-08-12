package com.arc90.sanevalidator.jdom;

import javax.xml.transform.sax.SAXSource;

import org.jdom.Parent;

public class Source implements javax.xml.transform.Source
{
	private final SAXSource source;
	
	public Source(Parent content)
	{
		// TODO: stub
		source = null;
	}
	
	public Source(String content)
	{
		// TODO: stub
		source = null;
	}

	public String getSystemId()
	{
		return null;
	}

	public void setSystemId(String systemId)
	{
	}

}
