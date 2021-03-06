/**
 * 
 */
package com.arc90.xmlsanity.validation;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

/**
 * Cacheable, fast, threadsafe, ultra-simple and ultra-usable multi-validator.
 * 
 * Essentially a wrapper for multiple validators, so an XML document can be
 * validated using multiple schemata at once.
 * 
 * The primary use case is to validate a document's structure using a RELAX NG
 * or XSD schema and its content using a Schematron schema, in one step.
 * 
 * The idea is that you instantiate this class, and place the instance in an
 * in-memory cache, such as OSCache, EHcache, or even just a Map or a static
 * field. Once that's done, multiple threads can use the instance to validate
 * XML documents or elements, without having to do any complicated setup. Plus,
 * the result, a ValidationResult object, makes any error messages available in
 * a simple, clear, readable, and useful way.
 * 
 * @author Avi Flax <avif@arc90.com>
 * 
 */
public class MultiValidator implements com.arc90.xmlsanity.validation.Validator
{
    private final List<Validator> validators = new ArrayList<Validator>();

    /**
     * 
     * @param xsdFile
     * @param rngFile
     * @param schematronFile
     * @throws ValidationException
     */
    public MultiValidator(File xsdFile, File rngFile, File schematronFile) throws ValidationException
    {
        if (xsdFile != null)
        {
            this.validators.add(new XsdValidator(xsdFile));
        }

        if (rngFile != null)
        {
            this.validators.add(new RngValidator(rngFile));
        }

        if (schematronFile != null)
        {
            this.validators.add(new SchematronValidator(schematronFile));
        }

        if (this.validators.size() == 0)
        {
            throw new ValidationException("Um, you seem to have passed 3 nulls to MultiValidator(). That's not gonna work.");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.arc90.xmlsanity.validation.interfaces.Validator#validate(byte[])
     */
    public ValidationResult validate(byte[] content) throws ValidationException
    {
        return validate(new ByteArrayInputStream(content));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.arc90.xmlsanity.validation.interfaces.Validator#validate(java.io.
     * File)
     */
    public ValidationResult validate(File content) throws ValidationException
    {
        return validate((Object) content);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.arc90.xmlsanity.validation.interfaces.Validator#validate(java.lang
     * .String)
     */
    public ValidationResult validate(String content) throws ValidationException
    {
        return validate((Object) content);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.arc90.xmlsanity.validation.interfaces.Validator#validate(java.io.
     * InputStream)
     */
    public ValidationResult validate(InputStream content) throws ValidationException
    {
        return validate((Object) content);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.arc90.xmlsanity.validation.interfaces.Validator#validate(org.w3c.
     * dom.Node)
     */
    public ValidationResult validate(Node content) throws ValidationException
    {
        return validate((Object) content);
    }

    /**
     * 
     * @param content
     *            an Object which must be either an InputStream, Node, String,
     *            File, or byte[]
     * @return indicates whether or not the content is valid, and if not, why
     * @throws ValidationException
     */
    private ValidationResult validate(Object content) throws ValidationException
    {
        ValidationResult result = new ValidationResult();

        for (Validator validator : this.validators)
        {
            ValidationResult thisResult;

            if (content instanceof Node)
            {
                thisResult = validator.validate((Node) content);
            }
            else if (content instanceof InputStream)
            {
                thisResult = validator.validate((InputStream) content);
            }
            else if (content instanceof File)
            {
                thisResult = validator.validate((File) content);
            }
            else if (content instanceof String)
            {
                thisResult = validator.validate((String) content);
            }
            else
            {
                throw new ValidationException("Content is an instance of " + content.getClass() + " which is not supported.");
            }

            result.addErrors(thisResult.getErrors());
        }

        return result;
    }
}
