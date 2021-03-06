package com.arc90.xmlsanity.transformation;

import java.io.InputStream;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamSource;

import com.arc90.xmlsanity.util.PoolException;

class StreamBasedTransformerPool extends TransformerPool
{
    protected final Logger    logger = Logger.getLogger(StreamBasedTransformerPool.class.getName());
    protected final Templates templates;

    public StreamBasedTransformerPool(InputStream xsltStream) throws TransformerConfigurationException, TransformerFactoryConfigurationError
    {
        templates = getTransformerFactory().newTemplates(new StreamSource(xsltStream));
    }
    
    public StreamBasedTransformerPool(String xsltString) throws TransformerConfigurationException
    {
        templates = getTransformerFactory().newTemplates(new StreamSource(new StringReader(xsltString)));
    }

    @Override
    protected Transformer create() throws PoolException
    {
        try
        {
            return templates.newTransformer();
        }
        catch (TransformerConfigurationException e)
        {
            logger.log(Level.FINE, e.getMessage(), e);
            throw new PoolException(e);
        }
    }

}
