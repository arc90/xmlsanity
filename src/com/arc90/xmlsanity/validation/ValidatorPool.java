package com.arc90.xmlsanity.validation;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import com.arc90.xmlsanity.util.Pool;

abstract class ValidatorPool extends Pool<javax.xml.validation.Validator>
{
    protected final Logger logger = Logger.getLogger(ValidatorPool.class.getName());

    void prepSchemaFactory(SchemaFactory schemaFactory)
    {
        try
        {
            schemaFactory.setFeature("http://xml.org/sax/features/validation", true);
            schemaFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

            // just in case
            schemaFactory.setFeature("http://saxon.sf.net/feature/validation", false);
        }
        catch (SAXNotRecognizedException e)
        {
            logger.log(Level.FINE, e.getMessage(), e);
        }
        catch (SAXNotSupportedException e)
        {
            logger.log(Level.FINE, e.getMessage(), e);
        }
    }
    
    void prepValidator(javax.xml.validation.Validator validator)
    {
        try
        {
            validator.setFeature("http://xml.org/sax/features/validation", true);
            validator.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

            // just in case
            validator.setFeature("http://saxon.sf.net/feature/validation", false);
        }
        catch (SAXNotRecognizedException e)
        {
            logger.log(Level.FINE, e.getMessage(), e);
        }
        catch (SAXNotSupportedException e)
        {
            logger.log(Level.FINE, e.getMessage(), e);
        }
    }
}
