package com.arc90.xmlsanity.validation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.validation.Schema;

import org.xml.sax.SAXException;


class FileBasedValidatorPool extends ValidatorPool
{
    protected final File           schemaFile;
    protected final Object         schemaLock = new Object();
    protected final ValidationType validationType;
    protected volatile Schema      schema;
    protected volatile long        schemaDateTime;

    private final Logger           logger     = Logger.getLogger(FileBasedValidatorPool.class.getName());

    protected FileBasedValidatorPool(File schemaFile, ValidationType validationType) throws ValidationException, SAXException, FileNotFoundException
    {
        checkFile(schemaFile);
        
        // Ensure that we can perform the specified type of validation

        this.schemaFile = schemaFile;
        this.validationType = validationType;

        // Initialize the schema, which will test that it's valid
        getSchema();
    }

    protected Schema getSchema() throws FileNotFoundException, SAXException, ValidationException
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

    protected void updateSchema() throws FileNotFoundException, SAXException, ValidationException
    {
        synchronized (schemaLock)
        {
            checkFile(schemaFile);
            
            schema = getSchemaFactory(validationType).newSchema(schemaFile);
            schemaDateTime = schemaFile.lastModified();
        }
    }

    @Override
    protected javax.xml.validation.Validator create()
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
    public boolean validate(javax.xml.validation.Validator o)
    {
        return getTimeCreated(o) > schemaDateTime;
    }

    public static void checkFile(File file) throws FileNotFoundException
    {
        if (file.exists() == false)
        {
            throw new FileNotFoundException("The file " + file.getAbsolutePath() + " does not exist.");
        }
        
        if (file.canRead() == false)
        {
            throw new FileNotFoundException("The file " + file.getAbsolutePath() + " cannot be read.");
        }    
    }
    
}
