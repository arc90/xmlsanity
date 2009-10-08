package com.arc90.xmlsanity.validation;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.XMLConstants;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import com.arc90.xmlsanity.util.Pool;

abstract class ValidatorPool extends Pool<javax.xml.validation.Validator>
{
    protected final Logger        logger = Logger.getLogger(ValidatorPool.class.getName());

    protected SchemaFactory getSchemaFactory(ValidationType validationType) throws IllegalArgumentException
    {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.RELAXNG_NS_URI);
        
        try
        {
            // TODO: I'm not sure if this is the complete set that's needed -- TESTING NEEDED!
            schemaFactory.setFeature("http://xml.org/sax/features/validation", true);
            schemaFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
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
        
        return schemaFactory;
    }
    
    void prepValidator(javax.xml.validation.Validator validator)
    {

        try
        {
            // I'm not sure if this is the complete set that's needed -- TESTING
            // NEEDED!
            validator.setFeature("http://xml.org/sax/features/validation", true);
            validator.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
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
