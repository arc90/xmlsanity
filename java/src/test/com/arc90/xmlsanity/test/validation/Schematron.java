package com.arc90.xmlsanity.test.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;
import org.xml.sax.SAXException;

import com.arc90.xmlsanity.validation.SchematronValidator;
import com.arc90.xmlsanity.validation.ValidationException;
import com.arc90.xmlsanity.validation.ValidationResult;
import com.arc90.xmlsanity.validation.Validator;

public final class Schematron
{
    @Test
    public void valid_documents_pass_validation() throws FileNotFoundException, SAXException, ValidationException
    {
        File schematronFile = new File("test/resources/schemata/person.sch");
        
        Validator validator = new SchematronValidator(schematronFile);
        
        File xmlFile = new File("test/resources/test_docs/person_good.xml");
        
        ValidationResult results = validator.validate(xmlFile);
        
        assertTrue(results.isValid());
        
        assertFalse(results.isInvalid());
        
        assertTrue(results.getErrors().size() == 0);
        
        assertTrue(results.getErrorsAsHtmlList().length() == 0);
    }

}
