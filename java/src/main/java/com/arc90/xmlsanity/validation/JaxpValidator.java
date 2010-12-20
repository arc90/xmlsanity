package com.arc90.xmlsanity.validation;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;

import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.arc90.xmlsanity.util.Pool;
import com.arc90.xmlsanity.util.PoolException;

/***
 * Cacheable, fast, threadsafe, ultra-simple and ultra-usable XSD validator. The
 * idea is that you instantiate this class, and place the instance in an
 * in-memory cache, such as OSCache, EHcache, or even just a Map or a static
 * field. Once that's done, multiple threads can use the instance to validate
 * XML documents or elements, without having to do any complicated setup. Plus,
 * the result, a ValidationResult object, makes any error messages available in
 * a simple, clear, readable, and useful way.
 * 
 * @author Avi Flax <avif@arc90.com>
 * 
 */
abstract class JaxpValidator implements Validator
{
    // I'd prefer for this to be final, but that doesn't seem possible, because subclasses must be able to set it
    protected Pool<javax.xml.validation.Validator> validatorPool;

    public ValidationResult validate(File content) throws ValidationException
    {
        try
        {
            Source source = new SAXSource(new InputSource(new FileReader(content)));
            return validate(source);
        }
        catch (FileNotFoundException e)
        {
            throw new ValidationException(e);
        }
    }

    public ValidationResult validate(String content) throws ValidationException
    {
        Source source = new SAXSource(new InputSource(new StringReader(content)));
        return validate(source);
    }

    public ValidationResult validate(byte[] content) throws ValidationException
    {
        return validate(new ByteArrayInputStream(content));
    }

    public ValidationResult validate(InputStream content) throws ValidationException
    {
        Source source = new SAXSource(new InputSource(content));
        return validate(source);
    }

    public ValidationResult validate(Node node) throws ValidationException
    {
        Source source = new DOMSource(node);
        return validate(source);
    }

    protected ValidationResult validate(Source source) throws ValidationException
    {
        javax.xml.validation.Validator validator;

        try
        {
            validator = getValidator();
        }
        catch (PoolException e)
        {
            throw new ValidationException(e);
        }

        ValidationResult result = new ValidationResult();

        validator.setErrorHandler(new ErrorHandler(result));

        try
        {
            validator.validate(source);
        }
        catch (SAXException e)
        {
            result.addError(e.getMessage());
        }
        catch (IOException e)
        {
            throw new ValidationException(e);
        }
        finally
        {
            returnValidator(validator);
        }

        return result;
    }

    protected javax.xml.validation.Validator getValidator() throws PoolException
    {
        return validatorPool.checkOut();
    }

    protected void returnValidator(javax.xml.validation.Validator validator)
    {
        validatorPool.checkIn(validator);
    }

}