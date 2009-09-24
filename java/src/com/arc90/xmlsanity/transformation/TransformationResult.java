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

    // TODO: look into using a stream instead of a string for better performance

    /**
     * used to store the result, if and when there is one
     */
    protected final StreamResult   streamResult = new StreamResult(new StringWriter());

    protected TransformationResult()
    {
    }

    public boolean errorExists()
    {
        return error != null;
    }

    public String getErrorMessage()
    {
        return errorExists() ? error.getMessage() : "";
    }

    public String getErrorLocation()
    {
        return errorExists() ? error.getLocationAsString() : "";
    }

    public String getErrorMessageAndLocation()
    {
        return errorExists() ? error.getMessageAndLocation() : "";
    }

    public boolean outputExists()
    {
        StringWriter writer = (StringWriter) streamResult.getWriter();
        return writer.getBuffer().length() > 0;
    }

    /***
     * @return If output exists, the output. If no output exists, and error(s)
     *         exist, the error(s). Otherwise, an empty String.
     */
    public String toString()
    {
        if (outputExists())
        {
            return streamResult.getWriter().toString();
        }
        else
        {
            // will return an empty string if there's no error
            return error.getMessageAndLocation();
        }
    }

    /**
     * 
     * @return If output exists, a JDOM Document of that output. Otherwise,
     *         null.
     * @throws JDOMException
     * @throws IOException
     */
    public Document toDocument() throws JDOMException, IOException
    {
        if (!outputExists())
        {
            return null;
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