package com.arc90.xmlsanity.transformation;

import java.io.StringWriter;

import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;

public class TransformationResult extends StreamResult
{

    protected TransformerException error = null;

    public TransformationResult()
    {
        this.setWriter(new StringWriter());
    }

    public boolean errorExists()
    {
        return error != null;
    }

    public TransformerException getError()
    {
        return error;
    }

    public String toString()
    {
        return this.getWriter().toString();
    }

    protected void setError(TransformerException exception)
    {
        error = exception;
    }

}
