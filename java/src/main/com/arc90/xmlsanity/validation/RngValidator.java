package com.arc90.xmlsanity.validation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.xml.sax.SAXException;

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
public class RngValidator extends JaxpValidator
{
    private final static Logger logger = Logger.getLogger(RngValidator.class.getName());

    /**
     * 
     * @param schemaFile
     *            a RELAX NG schema file, using either XML or Compact syntax.
     *            The file's name must end with either .rng or .rnc.
     * @throws ValidationException
     */
    public RngValidator(File schemaFile) throws ValidationException
    {
        try
        {
            validatorPool = new FileBasedValidatorPool(schemaFile, determineValidationTypeForFile(schemaFile));
        }
        catch (FileNotFoundException e)
        {
            throw new ValidationException(e);
        }
        catch (SAXException e)
        {
            throw new ValidationException(e);
        }
    }

    private ValidationType determineValidationTypeForFile(File schemaFile) throws ValidationException
    {
        String fileExtension = schemaFile.getName().substring(schemaFile.getName().lastIndexOf(".") + 1);

        logger.log(Level.FINE, "Extension of passed file {0} is {1}.", new Object[] { schemaFile.getName(), fileExtension });

        if (fileExtension.equalsIgnoreCase("rnc"))
        {
            return ValidationType.RELAXNG_COMPACT;
        }

        if (fileExtension.equalsIgnoreCase("rng"))
        {
            return ValidationType.RELAXNG_XML;
        }

        throw new ValidationException(String.format("Could not determine whether the RELAX NG schema file %s uses XML syntax or Compact syntax. Make sure the file name ends with .rng or .rnc.", schemaFile.getName()));
    }

}