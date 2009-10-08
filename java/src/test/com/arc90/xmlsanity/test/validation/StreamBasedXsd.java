package com.arc90.xmlsanity.test.validation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.junit.*;
import org.xml.sax.SAXException;

import com.arc90.xmlsanity.validation.ValidationException;
import com.arc90.xmlsanity.validation.ValidationResult;
import com.arc90.xmlsanity.validation.Validator;

import static org.junit.Assert.*;

public final class StreamBasedXsd
{
    @Test
    public void valid_documents_pass_validation() throws FileNotFoundException, SAXException, ValidationException
    {
        InputStream xsdStream = StreamBasedXsd.class.getResourceAsStream("insight_policyrequest_2.1.xsd");
        
        Validator validator = new Validator(xsdStream, StreamBasedXsd.class);
        
        File xmlFile = new File("test/validation/policyrequest_good.xml");
        
        ValidationResult results = validator.validate(xmlFile);
        
        assertTrue(results.isValid());
        
        assertFalse(results.isInvalid());
        
        assertTrue(results.getErrors().size() == 0);
        
        assertTrue(results.getErrorsAsHtmlList().length() == 0);
    }

    @Test
    public void invalid_documents_fail_validation() throws FileNotFoundException, SAXException, ValidationException
    {
        InputStream xsdStream = StreamBasedXsd.class.getResourceAsStream("insight_policyrequest_2.1.xsd");
        
        Validator validator = new Validator(xsdStream, StreamBasedXsd.class);
        
        File xmlFile = new File("test/validation/policyrequest_bad.xml");
        
        ValidationResult results = validator.validate(xmlFile);
        
        assertTrue(results.isInvalid());
        
        assertFalse(results.isValid());
        
        assertTrue(results.getErrors().size() > 0);
        
        assertTrue(results.getErrorsAsHtmlList().length() > 0);
    }
}
