package com.arc90.xmlsanity.transformation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

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
import org.jdom.output.XMLOutputter;
import org.xml.sax.InputSource;

/***
 * Cacheable, fast, threadsafe, ultra-simple and ultra-usable XLST transformer.
 * The idea is that you instantiate this class, and place the instance in an
 * in-memory cache, such as OSCache, EHcache, or even just a Map. Once that's
 * done, multiple threads can use the instance to transform XML documents or
 * elements, without having to do any complicated setup.
 * 
 * @author Avi Flax <avif@arc90.com>
 * 
 */
public class Transformer
{
    protected final Map<String, String> defaultParams;
    protected final TransformerPool     transformerPool;
    protected StreamResult              result = null;

    public Transformer(File xsltFile) throws FileNotFoundException, TransformerConfigurationException, TransformerFactoryConfigurationError
    {
        this(xsltFile, null);
    }

    public Transformer(File xsltFile, Map<String, String> defaultParams) throws FileNotFoundException, TransformerConfigurationException, TransformerFactoryConfigurationError
    {
        if (xsltFile.exists() == false)
        {
            throw new FileNotFoundException("The file " + xsltFile.getAbsolutePath() + " does not exist.");
        }

        Templates templates = TransformerFactory.newInstance().newTemplates(new StreamSource(xsltFile));

        transformerPool = new TransformerPool(templates);

        if (defaultParams == null)
        {
            defaultParams = new HashMap<String, String>();
        }

        this.defaultParams = defaultParams;
    }

    public TransformationResult transform(File original) throws FileNotFoundException
    {
        return transform(original, null);
    }

    public TransformationResult transform(File original, Map<String, String> params) throws FileNotFoundException
    {
        Source source = new SAXSource(new InputSource(new FileReader(original)));
        return transform(source, params);
    }

    public TransformationResult transform(String original)
    {
        return transform(original, null);
    }

    public TransformationResult transform(String original, Map<String, String> params)
    {
        Source source = new SAXSource(new InputSource(new StringReader(original)));
        return transform(source, params);
    }

    public TransformationResult transform(Document original) throws JDOMException, IOException
    {
        return transform(original, null);
    }

    public TransformationResult transform(Document original, Map<String, String> params) throws JDOMException, IOException
    {
        return transform(new XMLOutputter().outputString(original), params);
    }

    protected javax.xml.transform.Transformer getTransformer()
    {
        return transformerPool.checkOut();
    }

    protected void returnTransformer(javax.xml.transform.Transformer transformer)
    {
        result = null;
        transformer.clearParameters();
        transformerPool.checkIn(transformer);
    }

    protected TransformationResult transform(Source source, Map<String, String> params)
    {
        javax.xml.transform.Transformer transformer = getTransformer();

        for (String name : defaultParams.keySet())
        {
            transformer.setParameter(name, defaultParams.get(name));
        }

        if (params != null)
        {
            for (String name : params.keySet())
            {
                transformer.setParameter(name, params.get(name));
            }
        }

        TransformationResult result = new TransformationResult();

        try
        {
            transformer.transform(source, result.getStreamResult());
        }
        catch (TransformerException e)
        {
            result.setError(e);
        }
        finally
        {
            returnTransformer(transformer);
        }

        return result;
    }

}
