package com.arc90.xmlsanity.transformation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.sax.SAXSource;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Parent;
import org.jdom.output.XMLOutputter;
import org.xml.sax.InputSource;

import com.arc90.xmlsanity.util.PoolException;

/***
 * Cacheable, fast, threadsafe, ultra-simple and ultra-usable XLST transformer.
 * The idea is that you instantiate this class, and place the instance in an
 * in-memory cache, such as OSCache, EHcache, or even just a Map or a static
 * field. Once that's done, multiple threads can use the instance to transform
 * XML documents or elements, without having to do any complicated setup.
 * 
 * @author Avi Flax <avif@arc90.com>
 * 
 */
public class Transformer
{
    protected final Map<String, String> defaultParams;
    protected final TransformerPool     transformerPool;

    public Transformer(File xsltFile) throws FileNotFoundException, TransformerConfigurationException, TransformerFactoryConfigurationError
    {
        this(xsltFile, null);
    }

    public Transformer(File xsltFile, Map<String, String> defaultParams) throws FileNotFoundException, TransformerConfigurationException, TransformerFactoryConfigurationError
    {
        transformerPool = new FileBasedTransformerPool(xsltFile);

        if (defaultParams == null)
        {
            defaultParams = new HashMap<String, String>();
        }

        this.defaultParams = defaultParams;
    }

    public Transformer(InputStream xsltStream) throws FileNotFoundException, TransformerConfigurationException, TransformerFactoryConfigurationError
    {
        this(xsltStream, null);
    }    
    
    public Transformer(InputStream xsltStream, Map<String, String> defaultParams) throws FileNotFoundException, TransformerConfigurationException, TransformerFactoryConfigurationError
    {
        transformerPool = new StreamBasedTransformerPool(xsltStream);

        if (defaultParams == null)
        {
            defaultParams = new HashMap<String, String>();
        }

        this.defaultParams = defaultParams;
    }    
    
    public TransformationResult transform(File original) throws TransformationException
    {
        return transform(original, null);
    }

    public TransformationResult transform(File original, Map<String, String> params) throws TransformationException
    {
        try
        {
            Source source = new SAXSource(new InputSource(new FileReader(original)));
            return transform(source, params);
        }
        catch (FileNotFoundException e)
        {
            throw new TransformationException(e);
        }
    }

    public TransformationResult transform(String original) throws TransformationException
    {
        return transform(original, null);
    }

    public TransformationResult transform(String original, Map<String, String> params) throws TransformationException
    {
        Source source = new SAXSource(new InputSource(new StringReader(original)));
        return transform(source, params);
    }

    public TransformationResult transform(Parent original) throws TransformationException
    {
        return transform(original, null);
    }

    public TransformationResult transform(Parent original, Map<String, String> params) throws TransformationException
    {
        // TODO: Look into using XMLOutputter.output() with streams instead of a String, for better performance
        
        if (original instanceof Document)
        {
            return transform(new XMLOutputter().outputString((Document) original), params);
        }
        else if (original instanceof Element)
        {
            return transform(new XMLOutputter().outputString((Element) original), params);
        }
        else
        {
            throw new TransformationException("The object to transform must be either a JDOM Document or a Element. The passed object is of type " + original.getClass().getName());
        }
    }

    protected javax.xml.transform.Transformer getTransformer() throws PoolException
    {
        return transformerPool.checkOut();
    }

    protected void returnTransformer(javax.xml.transform.Transformer transformer)
    {
        transformer.clearParameters();
        transformerPool.checkIn(transformer);
    }

    protected TransformationResult transform(Source source, Map<String, String> params) throws TransformationException
    {
        javax.xml.transform.Transformer transformer;

        try
        {
            transformer = getTransformer();
        }
        catch (PoolException e)
        {
            throw new TransformationException(e);
        }

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
