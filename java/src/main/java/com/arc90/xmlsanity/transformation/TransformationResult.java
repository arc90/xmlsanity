package com.arc90.xmlsanity.transformation;

import java.io.StringWriter;

import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;

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

    public String getError()
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

    protected StreamResult getStreamResult()
    {
        return streamResult;
    }

    protected void setError(TransformerException exception)
    {
        error = exception;
    }

}
