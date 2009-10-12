package com.arc90.xmlsanity.test.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;
import org.xml.sax.SAXException;

import com.arc90.xmlsanity.validation.RngValidator;
import com.arc90.xmlsanity.validation.ValidationException;
import com.arc90.xmlsanity.validation.ValidationResult;
import com.arc90.xmlsanity.validation.Validator;

public final class FileBasedRng
{
    @Test
    public void valid_documents_pass_validation() throws FileNotFoundException, SAXException, ValidationException
    {
        File rngFile = new File("test/resources/schemata/wadl20061109.rng");
        
        Validator validator = new RngValidator(rngFile);
        
        File xmlFile = new File("test/resources/test_docs/wadl_good.xml");
        
        ValidationResult results = validator.validate(xmlFile);
        
        assertTrue(results.isValid());
        
        assertFalse(results.isInvalid());
        
        assertTrue(results.getErrors().size() == 0);
        
        assertTrue(results.getErrorsAsHtmlList().length() == 0);
    }

}
