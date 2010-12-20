/**
 * 
 */
package com.arc90.xmlsanity.validation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.w3c.dom.Node;

import com.arc90.xmlsanity.transformation.TransformationException;
import com.arc90.xmlsanity.transformation.TransformationResult;
import com.arc90.xmlsanity.transformation.Transformer;

/**
 * Cacheable, fast, threadsafe, ultra-simple and ultra-usable Schematron validator.
 * 
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
            Transformer step1Transformer = new Transformer(new File("iso-schematron-xslt2/iso_dsdl_include.xsl"));
            TransformationResult step1Result = step1Transformer.transform(schematronFile);
            
            if (step1Result.errorExists())
            {
                throw new ValidationException("An error occurred while compiling the Schematron schema: " + step1Result.getError());
            }
            else if (step1Result.outputExists() == false)
            {
                throw new ValidationException("A problem occurred while compiling the Schematron schema: step 1 returned no output.");
            }
            
            Transformer step2Transformer = new Transformer(new File("iso-schematron-xslt2/iso_abstract_expand.xsl"));
            TransformationResult step2Result = step2Transformer.transform(step1Result.toString());
            
            if (step2Result.errorExists())
            {
                throw new ValidationException("An error occurred while compiling the Schematron schema: " + step2Result.getError());
            }
            else if (step2Result.outputExists() == false)
            {
                throw new ValidationException("A problem occurred while compiling the Schematron schema: step 2 returned no output.");
            }
            
            Map<String,String> step3Params = new HashMap<String,String>();
            step3Params.put("output-encoding", "UTF-8");
            
            Transformer step3Transformer = new Transformer(new File("iso-schematron-xslt2/iso_svrl_for_xslt2.xsl"), step3Params);
            TransformationResult step3Result = step3Transformer.transform(step2Result.toString());
            
            if (step3Result.errorExists())
            {
                throw new ValidationException("An error occurred while compiling the Schematron schema: " + step3Result.getError());
            }
            else if (step3Result.outputExists() == false)
            {
                throw new ValidationException("A problem occurred while compiling the Schematron schema: step 3 returned no output.");
            }
            
            try
            {
                File debugFile = new File("debug.xsl");
                FileWriter fw = new FileWriter(debugFile);
                fw.write(step3Result.toString());
                fw.close();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            // The result of Step 3 is the XSLT document which is used to actually validate actual documents 
//            this.schematronTransformer = new Transformer(step3Result.toString());
            
this.schematronTransformer = new Transformer(new File("debug.xsl"));
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
