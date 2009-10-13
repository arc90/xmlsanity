/**
 * 
 */
package com.arc90.xmlsanity.validation;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.w3c.dom.Node;

import com.arc90.xmlsanity.transformation.TransformationException;
import com.arc90.xmlsanity.transformation.TransformationResult;
import com.arc90.xmlsanity.transformation.Transformer;

/**
 * @author avi
 *
 */
public class SchematronValidator implements Validator
{
    protected final Transformer schematronTransformer;
    
    public SchematronValidator(File schematronFile) throws ValidationException
    {
        try
        {
            Transformer step1Transformer = new Transformer(getClass().getResourceAsStream("iso-schematron-xslt2/iso_dsdl_include.xsl"));
            TransformationResult step1Result = step1Transformer.transform(schematronFile);
            
            if (step1Result.errorExists())
            {
                throw new ValidationException("An error occurred while compiling the Schematron schema: " + step1Result.getError());
            }
            else if (step1Result.outputExists() == false)
            {
                throw new ValidationException("A problem occurred while compiling the Schematron schema: step 1 returned no output.");
            }
            
            Transformer step2Transformer = new Transformer(getClass().getResourceAsStream("iso-schematron-xslt2/iso_abstract_expand.xsl"));
            TransformationResult step2Result = step2Transformer.transform(step1Result.toString());
            
            if (step2Result.errorExists())
            {
                throw new ValidationException("An error occurred while compiling the Schematron schema: " + step2Result.getError());
            }
            else if (step2Result.outputExists() == false)
            {
                throw new ValidationException("A problem occurred while compiling the Schematron schema: step 2 returned no output.");
            }
            
            Transformer step3Transformer = new Transformer(getClass().getResourceAsStream("iso-schematron-xslt2/iso_svrl_for_xslt2.xsl"));
            TransformationResult step3Result = step3Transformer.transform(step2Result.toString());
            
            if (step3Result.errorExists())
            {
                throw new ValidationException("An error occurred while compiling the Schematron schema: " + step3Result.getError());
            }
            else if (step3Result.outputExists() == false)
            {
                throw new ValidationException("A problem occurred while compiling the Schematron schema: step 3 returned no output.");
            }
            
            // The result of Step 3 is the XSLT document which is used to actually validate actual documents 
            this.schematronTransformer = new Transformer(new ByteArrayInputStream(step3Result.toString().getBytes("UTF-8")));
        }
        catch (FileNotFoundException e)
        {
            throw new ValidationException(e);
        }
        catch (TransformerConfigurationException e)
        {
            throw new ValidationException(e);
        }
        catch (TransformerFactoryConfigurationError e)
        {
            throw new ValidationException(e);
        }
        catch (TransformationException e)
        {
            throw new ValidationException(e);
        }
        catch (UnsupportedEncodingException e)
        {
            throw new ValidationException(e);
        }
    }
    
    
    /* (non-Javadoc)
     * @see com.arc90.xmlsanity.validation.Validator#validate(byte[])
     */
    @Override
    public ValidationResult validate(byte[] content) throws ValidationException
    {
        // TODO Auto-generated method stub
        return new ValidationResult();
    }

    /* (non-Javadoc)
     * @see com.arc90.xmlsanity.validation.Validator#validate(java.io.File)
     */
    @Override
    public ValidationResult validate(File content) throws ValidationException
    {
        // TODO Auto-generated method stub
        return new ValidationResult();
    }

    /* (non-Javadoc)
     * @see com.arc90.xmlsanity.validation.Validator#validate(java.io.InputStream)
     */
    @Override
    public ValidationResult validate(InputStream content) throws ValidationException
    {
        // TODO Auto-generated method stub
        return new ValidationResult();
    }

    /* (non-Javadoc)
     * @see com.arc90.xmlsanity.validation.Validator#validate(org.w3c.dom.Node)
     */
    @Override
    public ValidationResult validate(Node content) throws ValidationException
    {
        // TODO Auto-generated method stub
        return new ValidationResult();
    }

    /* (non-Javadoc)
     * @see com.arc90.xmlsanity.validation.Validator#validate(java.lang.String)
     */
    @Override
    public ValidationResult validate(String content) throws ValidationException
    {
        // TODO Auto-generated method stub
        return new ValidationResult();
    }

}
