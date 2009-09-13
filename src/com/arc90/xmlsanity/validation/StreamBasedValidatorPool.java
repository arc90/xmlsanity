package com.arc90.xmlsanity.validation;

import java.io.InputStream;
import java.util.logging.Logger;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

import com.arc90.xmlsanity.util.Pool;

class StreamBasedValidatorPool extends Pool<javax.xml.validation.Validator>
{
    protected final Logger  logger                = Logger.getLogger(StreamBasedValidatorPool.class.getName());
    protected final Schema  schema;

    protected StreamBasedValidatorPool(InputStream inputStream) throws SAXException
    {
        super();
        this.schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(new StreamSource(inputStream));
    }

    @Override
    protected Validator create()
    {
        return schema.newValidator();
    }

}
