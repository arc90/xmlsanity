package com.arc90.xmlsanity.transformation;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class TransformationResult
{

    protected TransformerException error        = null;
    protected final StreamResult   streamResult = new StreamResult(new StringWriter());

    public boolean errorExists()
    {
        return error != null;
    }

    public TransformerException getError()
    {
        return error;
    }

    public boolean outputExists()
    {
        StringWriter writer = (StringWriter) streamResult.getWriter();
        return writer.getBuffer().length() > 0;
    }

    public String toString()
    {
        if (outputExists())
        {
            return streamResult.getWriter().toString();
        }
        else if (errorExists())
        {
            return error.getMessageAndLocation();
        }
        else
        {
            return "";
        }
    }

    /**
     * 
     * @return If output exists, a JDOM Document of that output. Otherwise, an empty Document.
     * @throws JDOMException
     * @throws IOException
     */
    public Document toDocument() throws JDOMException, IOException
    {
        if (!outputExists())
        {
            // TODO: maybe this should return null?
            return new Document();
        }

        return new SAXBuilder().build(new StringReader(this.toString()));
    }

    protected StreamResult getStreamResult()
    {
        return streamResult;
    }

    protected void setError(TransformerException exception)
    {
        error = exception;
    }

}
