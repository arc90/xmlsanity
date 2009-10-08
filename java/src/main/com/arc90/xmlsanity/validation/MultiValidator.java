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
public class MultiValidator implements com.arc90.xmlsanity.validation.Validator
{

    /*
     * (non-Javadoc)
     * 
     * @see com.arc90.xmlsanity.validation.interfaces.Validator#validate(byte[])
     */
    public ValidationResult validate(byte[] content) throws ValidationException
    {
        // TODO Auto-generated method stub
        return null;
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
        // TODO Auto-generated method stub
        return null;
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
        // TODO Auto-generated method stub
        return null;
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
        // TODO Auto-generated method stub
        return null;
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
        // TODO Auto-generated method stub
        return null;
    }

}
