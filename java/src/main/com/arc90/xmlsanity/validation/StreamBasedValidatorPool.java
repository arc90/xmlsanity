package com.arc90.xmlsanity.validation;

import java.io.InputStream;
import java.util.logging.Logger;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

class StreamBasedValidatorPool extends ValidatorPool
{
    protected final Logger logger = Logger.getLogger(StreamBasedValidatorPool.class.getName());
    protected final Schema schema;

    protected StreamBasedValidatorPool(InputStream inputStream) throws SAXException
    {
        this.schema = getSchemaFactory().newSchema(new StreamSource(inputStream)); 
    }

    protected StreamBasedValidatorPool(InputStream inputStream, String basePathForResourceResolution) throws SAXException
    {
        SchemaFactory factory = getSchemaFactory();
        //factory.setResourceResolver(new ResourceResolver(basePathForResourceResolution));
        this.schema = getSchemaFactory().newSchema(new StreamSource(inputStream));
    }
    
    @SuppressWarnings("unchecked")
    protected StreamBasedValidatorPool(InputStream inputStream, Class classForResourceResolution) throws SAXException
    {
        this.schema = getSchemaFactory(classForResourceResolution).newSchema(new StreamSource(inputStream));
    }    
    

    @Override
    protected Validator create()
    {
        javax.xml.validation.Validator validator = schema.newValidator();
        prepValidator(validator);
        return validator;
    }

}
