package com.arc90.xmlsanity.validation;

import java.io.InputStream;
import java.util.logging.Logger;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

class StreamBasedValidatorPool extends ValidatorPool
{
    protected final Logger  logger                = Logger.getLogger(StreamBasedValidatorPool.class.getName());
    protected final Schema  schema;

    protected StreamBasedValidatorPool(InputStream inputStream) throws SAXException
    {
        super();
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        prepSchemaFactory(schemaFactory);
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
