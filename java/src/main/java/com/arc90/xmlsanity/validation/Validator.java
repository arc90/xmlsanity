/**
 * 
 */
package com.arc90.xmlsanity.validation;

import java.io.File;
import java.io.InputStream;

import org.w3c.dom.Node;


/**
 * @author avi
 *
 */
public interface Validator
{
    public ValidationResult validate(byte[] content) throws ValidationException;
    
    public ValidationResult validate(File content) throws ValidationException;
    
    public ValidationResult validate(InputStream content) throws ValidationException;

    public ValidationResult validate(Node content) throws ValidationException;    
    
    public ValidationResult validate(String content) throws ValidationException;
}
