package com.arc90.xmlsanity.validation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

import com.arc90.xmlsanity.util.Pool;

class ValidatorPool extends Pool<javax.xml.validation.Validator>
{
    protected final File schemaFile;
    protected final Object schemaLock = new Object();
    
    protected volatile Schema schema;
	protected volatile long schemaDateTime;
	
	private final Logger logger = Logger.getLogger(ValidatorPool.class.getName());
	
    protected ValidatorPool(File schemaFile) throws SAXException, FileNotFoundException
    {
        if (schemaFile.exists() == false)
        {
            throw new FileNotFoundException("The file " + schemaFile.getAbsolutePath() + " does not exist.");
        }
        
        this.schemaFile = schemaFile;
        
        // Initialize the schema, which will test that it's valid
        getSchema();
    }
    
    protected Schema getSchema() throws FileNotFoundException, SAXException
    {
        synchronized (schemaLock)
        {
            if (schema == null || schemaDateTime != schemaFile.lastModified() || schemaDateTime == 0L || schemaFile.lastModified() == 0L)
            {
                updateSchema();
            }
            
            return schema;
        }
    }
    
    protected void updateSchema() throws FileNotFoundException, SAXException
    {
        synchronized (schemaLock)
        {
            if (schemaFile.exists() == false)
            {
                throw new FileNotFoundException("The file " + schemaFile.getAbsolutePath() + " does not exist.");
            }
            
            schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(schemaFile);
            schemaDateTime = schemaFile.lastModified();
        }
    }
	
	@Override
	protected Validator create()
	{
		try
        {
            return getSchema().newValidator();
        }
        catch (Exception e)
        {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new RuntimeException(e);
        }
	}	
	
	@Override
	public void expire(Validator o)
	{
	    // No need to do anything.
	}

	@Override
	public boolean validate(Validator o)
	{
		// TODO: Check whether the schema instance which produced the validator has expired
	    return true;
	}

}
