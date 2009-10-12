package com.arc90.xmlsanity.validation;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import org.iso_relax.verifier.Verifier;
import org.iso_relax.verifier.VerifierConfigurationException;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.arc90.xmlsanity.util.Pool;
import com.arc90.xmlsanity.util.PoolException;

/***
 * Cacheable, fast, threadsafe, ultra-simple and ultra-usable RELAX NG
 * validator. The idea is that you instantiate this class, and place the
 * instance in an in-memory cache, such as OSCache, EHcache, or even just a Map
 * or a static field. Once that's done, multiple threads can use the instance to
 * validate XML documents or elements, without having to do any complicated
 * setup. Plus, the result, a ValidationResult object, makes any error messages
 * available in a simple, clear, readable, and useful way.
 * 
 * @author Avi Flax <avif@arc90.com>
 * 
 */
public class RngValidator implements Validator
{
    protected final Pool<Verifier> verifierPool;

    public RngValidator(File schemaFile) throws ValidationException, ValidationTypeUnsupportedException
    {
        try
        {
            verifierPool = new FileBasedVerifierPool(schemaFile);
        }
        catch (FileNotFoundException e)
        {
            throw new ValidationException(e);
        }
        catch (FileUnreadableException e)
        {
            throw new ValidationException(e);
        }
        catch (SAXException e)
        {
            throw new ValidationException(e);
        }
        catch (VerifierConfigurationException e)
        {
            throw new ValidationException(e);
        }
        catch (IOException e)
        {
            throw new ValidationException(e);
        }
    }

    public ValidationResult validate(byte[] content) throws ValidationException
    {
        return validate(new ByteArrayInputStream(content));
    }

    public ValidationResult validate(File content) throws ValidationException
    {
        try
        {
            return validate(new InputSource(new FileReader(content)));
        }
        catch (FileNotFoundException e)
        {
            throw new ValidationException(e);
        }
    }

    public ValidationResult validate(InputStream content) throws ValidationException
    {
        return validate(new InputSource(content));
    }

    public ValidationResult validate(Node content) throws ValidationException
    {
        Verifier verifier;

        try
        {
            verifier = getVerifier();
        }
        catch (PoolException e)
        {
            throw new ValidationException(e);
        }

        ValidationResult result = new ValidationResult();

        verifier.setErrorHandler(new ErrorHandler(result));

        try
        {
            verifier.verify(content);
        }
        catch (SAXException e)
        {
            result.addError(e.getMessage());
        }
        finally
        {
            returnVerifier(verifier);
        }

        return result;        
    }

    public ValidationResult validate(String content) throws ValidationException
    {
        return validate(new InputSource(new StringReader(content)));
    }
    
    protected ValidationResult validate(InputSource source) throws ValidationException
    {
        Verifier verifier;

        try
        {
            verifier = getVerifier();
        }
        catch (PoolException e)
        {
            throw new ValidationException(e);
        }

        ValidationResult result = new ValidationResult();

        verifier.setErrorHandler(new ErrorHandler(result));

        try
        {
            verifier.verify(source);
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
            returnVerifier(verifier);
        }

        return result;
    }
    
    protected Verifier getVerifier() throws PoolException
    {
        return verifierPool.checkOut();
    }

    protected void returnVerifier(Verifier verifier)
    {
        verifierPool.checkIn(verifier);
    }

}