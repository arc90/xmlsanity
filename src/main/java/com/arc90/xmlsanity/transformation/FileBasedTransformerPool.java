package com.arc90.xmlsanity.transformation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamSource;

class FileBasedTransformerPool extends TransformerPool
{
    protected final File         xsltFile;
    protected final Object       templatesLock = new Object();
    protected volatile Templates templates;
    protected volatile long      templatesDateTime;

    private final Logger         logger        = Logger.getLogger(FileBasedTransformerPool.class.getName());

    public FileBasedTransformerPool(File xsltFile) throws FileNotFoundException, TransformerConfigurationException, TransformerFactoryConfigurationError
    {
        if (xsltFile.exists() == false)
        {
            throw new FileNotFoundException("The file " + xsltFile.getAbsolutePath() + " does not exist.");
        }

        this.xsltFile = xsltFile;

        // Initialize the templates, which will test that it's valid
        getTemplates();
    }

    protected Templates getTemplates() throws FileNotFoundException, TransformerConfigurationException, TransformerFactoryConfigurationError
    {
        synchronized (templatesLock)
        {
            if (templates == null || templatesDateTime != xsltFile.lastModified() || templatesDateTime == 0L || xsltFile.lastModified() == 0L)
            {
                updateTemplates();
            }

            return templates;
        }
    }

    protected void updateTemplates() throws FileNotFoundException, TransformerConfigurationException, TransformerFactoryConfigurationError
    {
        synchronized (templatesLock)
        {
            if (xsltFile.exists() == false)
            {
                throw new FileNotFoundException("The file " + xsltFile.getAbsolutePath() + " does not exist.");
            }

            templates = getTransformerFactory().newTemplates(new StreamSource(xsltFile));
            templatesDateTime = xsltFile.lastModified();
        }
    }

    @Override
    protected javax.xml.transform.Transformer create()
    {
        try
        {
            return getTemplates().newTransformer();
        }
        catch (Exception e)
        {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public boolean validate(javax.xml.transform.Transformer transformer)
    {
        return getTimeCreated(transformer) > templatesDateTime;
    }
}
