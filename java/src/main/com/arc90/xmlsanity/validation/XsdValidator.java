package com.arc90.xmlsanity.validation;

import java.io.File;
import java.io.FileNotFoundException;

import org.xml.sax.SAXException;

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
public class XsdValidator extends AbstractValidator
{
    public XsdValidator(File schemaFile) throws FileNotFoundException, SAXException
    {
        validatorPool = new FileBasedValidatorPool(schemaFile, ValidationType.XSD);
    }
  
}