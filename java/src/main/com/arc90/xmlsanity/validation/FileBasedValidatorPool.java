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

    protected FileBasedValidatorPool(File schemaFile, ValidationType validationType) throws SAXException, FileNotFoundException
    {
        if (schemaFile.exists() == false)
        {
            throw new FileNotFoundException("The file " + schemaFile.getAbsolutePath() + " does not exist.");
        }

        this.schemaFile = schemaFile;
        this.validationType = validationType;

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

}
