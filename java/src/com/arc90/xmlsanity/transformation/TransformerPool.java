package com.arc90.xmlsanity.transformation;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;

import com.arc90.xmlsanity.util.Pool;

abstract class TransformerPool extends Pool<javax.xml.transform.Transformer>
{
    protected static final Logger logger = Logger.getLogger(TransformerPool.class.getName());
    protected final TransformerFactory transformerFactory;
    
    protected TransformerPool()
    {
        transformerFactory = TransformerFactory.newInstance();
        
        try
        {
            // I'm not sure if this is the complete set that's needed -- TESTING NEEDED!
            transformerFactory.setFeature("http://xml.org/sax/features/validation", true);
            transformerFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            transformerFactory.setFeature("http://saxon.sf.net/feature/validation", false);
        }
        catch (TransformerConfigurationException e)
        {
            logger.log(Level.FINE, e.getMessage(), e);
        }
    }
    
}
