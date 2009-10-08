package com.arc90.xmlsanity.util;

import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

public class ClassBasedResourceResolver implements LSResourceResolver
{
    private final List<String> resolvedResources = new ArrayList<String>();
    
    @SuppressWarnings("unchecked")
    private final Class classForResourceResolution;
    
    @SuppressWarnings("unchecked")
    public ClassBasedResourceResolver(Class classForResourceResolution)
    {
        super();
        this.classForResourceResolution = classForResourceResolution;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.w3c.dom.ls.LSResourceResolver#resolveResource(java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public LSInput resolveResource(String type, String namespaceURI, String publicId, final String systemId, String baseURI)
    {
        return new LSInput()
        {
            public String getBaseURI()
            {
                return null;
            }

            public InputStream getByteStream()
            {
                /*
                 * Only resolve each include once. Otherwise there will be
                 * errors about duplicate definitions
                 */
                if (!resolvedResources.contains(systemId))
                {
                    String path = systemId;

                    if (path.startsWith("http"))
                    {
                        // Don't try to handle urls
                        return null;
                    }

                    resolvedResources.add(systemId);
                    return classForResourceResolution.getResourceAsStream(path);
                }
                else
                {
                    return null;
                }
            }

            public boolean getCertifiedText()
            {
                return false;
            }

            public Reader getCharacterStream()
            {
                return null;
            }

            public String getEncoding()
            {
                return null;
            }

            public String getPublicId()
            {
                return null;
            }

            public String getStringData()
            {
                return null;
            }

            public String getSystemId()
            {
                return null;
            }

            public void setBaseURI(String baseURI)
            {

            }

            public void setByteStream(InputStream byteStream)
            {

            }

            public void setCertifiedText(boolean certifiedText)
            {

            }

            public void setCharacterStream(Reader characterStream)
            {

            }

            public void setEncoding(String encoding)
            {

            }

            public void setPublicId(String publicId)
            {

            }

            public void setStringData(String stringData)
            {

            }

            public void setSystemId(String systemId)
            {

            }
        };
    }
}