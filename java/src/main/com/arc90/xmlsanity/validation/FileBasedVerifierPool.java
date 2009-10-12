package com.arc90.xmlsanity.validation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.iso_relax.verifier.Schema;
import org.iso_relax.verifier.Verifier;
import org.iso_relax.verifier.VerifierConfigurationException;
import org.iso_relax.verifier.VerifierFactory;
import org.xml.sax.SAXException;

import com.arc90.xmlsanity.util.Pool;

class FileBasedVerifierPool extends Pool<Verifier>
{
    protected final File            schemaFile;
    protected final Object          schemaLock      = new Object();
    protected volatile Schema       schema;
    protected volatile long         schemaDateTime;
    protected final VerifierFactory verifierFactory = new com.sun.msv.verifier.jarv.TheFactoryImpl();

    private final Logger            logger          = Logger.getLogger(FileBasedVerifierPool.class.getName());

    protected FileBasedVerifierPool(File schemaFile) throws SAXException, VerifierConfigurationException, IOException
    {
        checkFile(schemaFile);

        this.schemaFile = schemaFile;

        // Initialize the schema, which will test that it's valid
        getSchema();
    }

    protected Schema getSchema() throws SAXException, VerifierConfigurationException, IOException
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

    protected void updateSchema() throws VerifierConfigurationException, IOException, SAXException
    {
        synchronized (schemaLock)
        {
            checkFile(schemaFile);

            schema = verifierFactory.compileSchema(schemaFile);
            schemaDateTime = schemaFile.lastModified();
        }
    }

    @Override
    protected Verifier create()
    {
        try
        {
            return getSchema().newVerifier();
        }
        catch (Exception e)
        {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean validate(Verifier o)
    {
        return getTimeCreated(o) > schemaDateTime;
    }

    public static void checkFile(File file) throws FileNotFoundException, FileUnreadableException
    {
        if (file.exists() == false)
        {
            throw new FileNotFoundException("The file " + file.getAbsolutePath() + " does not exist.");
        }

        // TODO: check for readability
    }

}
