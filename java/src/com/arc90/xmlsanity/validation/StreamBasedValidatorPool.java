package com.arc90.xmlsanity.validation;

import java.io.InputStream;
import java.util.logging.Logger;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

class StreamBasedValidatorPool extends ValidatorPool
{
    protected final Logger logger = Logger.getLogger(StreamBasedValidatorPool.class.getName());
    protected final Schema schema;

    protected StreamBasedValidatorPool(InputStream inputStream) throws SAXException
    {
        this(inputStream, null);
    }

    protected StreamBasedValidatorPool(InputStream inputStream, String baseURI) throws SAXException
    {
        super();
        this.schema = schemaFactory.newSchema(new StreamSource(inputStream));
    }

    @Override
    protected Validator create()
    {
        javax.xml.validation.Validator validator = schema.newValidator();
        prepValidator(validator);
        return validator;
    }

}
