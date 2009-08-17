package com.arc90.xmlsanity.transformation;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.xml.sax.InputSource;

public class Transformer
{
	protected final TransformerPool transformerPool;

	public Transformer(File xsltFile) throws FileNotFoundException, TransformerConfigurationException, TransformerFactoryConfigurationError
	{
		if (xsltFile.exists() == false)
		{
			throw new FileNotFoundException("The file " + xsltFile.getAbsolutePath() + " does not exist.");
		}
		
		Templates templates = TransformerFactory.newInstance().newTemplates(new StreamSource(xsltFile));
		
		transformerPool = new TransformerPool(templates);
	}	
	
	public Document transformToDoc(Document original) throws TransformerException, JDOMException, IOException
	{
		String transformed = transformToString(new XMLOutputter().outputString(original));
		return new SAXBuilder().build(new StringReader(transformed));
	}
	
	public OutputStream transformToStream(File original) throws FileNotFoundException, TransformerException
	{
		Source source = new SAXSource(new InputSource(new FileReader(original)));
		return transformToStream(source);
	}
	
	public OutputStream transformToStream(InputStream original) throws TransformerException
	{
		Source source = new SAXSource(new InputSource(original));
		return transformToStream(source);
	}
	
	public OutputStream transformToStream(String original) throws TransformerException
	{
		Source source = new SAXSource(new InputSource(new StringReader(original)));
		return transformToStream(source);
	}
	
	public String transformToString(File original) throws FileNotFoundException, TransformerException
	{
		Source source = new SAXSource(new InputSource(new FileReader(original)));
		return transformToString(source);
	}

	public String transformToString(String original) throws TransformerException
	{
		Source source = new SAXSource(new InputSource(new StringReader(original)));
		return transformToString(source);
	}
	
	public void transformToStream(String original, OutputStream stream) throws TransformerException, IOException
	{
		String transformed = transformToString(original);
		stream.write(transformed.getBytes());
	}
	
	public void transformToStream(File original, OutputStream stream) throws TransformerException, IOException
	{
		String transformed = transformToString(original);
		stream.write(transformed.getBytes());
	}
	
	protected javax.xml.transform.Transformer getTransformer()
	{
		return transformerPool.checkOut();
	}
	
	protected void returnTransformer(javax.xml.transform.Transformer transformer)
	{
		transformerPool.checkIn(transformer);
	}	
	
	protected OutputStream transformToStream(Source source) throws TransformerException
	{
		javax.xml.transform.Transformer transformer = getTransformer();
		
		try
		{
			StreamResult result = new StreamResult(new ByteArrayOutputStream());
		
			transformer.transform(source, result);
		
			return result.getOutputStream();
		}
		finally
		{
			returnTransformer(transformer);
		}
	}
	
	protected String transformToString(Source source) throws TransformerException
	{
		javax.xml.transform.Transformer transformer = getTransformer();
		
		try
		{
			StreamResult result = new StreamResult(new StringWriter());
		
			transformer.transform(source, result);
		
			return result.getWriter().toString();
		}
		finally
		{
			returnTransformer(transformer);
		}
	}

}
