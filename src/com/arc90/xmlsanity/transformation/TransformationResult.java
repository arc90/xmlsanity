package com.arc90.xmlsanity.transformation;

import java.io.StringWriter;

import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;

public class TransformationResult
{

    protected TransformerException error = null;
    protected final StreamResult streamResult = new StreamResult(new StringWriter());

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

    protected StreamResult getStreamResult()
    {
        return streamResult;
    }

    protected void setError(TransformerException exception)
    {
        error = exception;
    }

}
