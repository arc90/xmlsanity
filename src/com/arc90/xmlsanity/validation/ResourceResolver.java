package com.arc90.xmlsanity.validation;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

/**
 * From http://pbin.oogly.co.uk/listings/viewlistingdetail/2
 * a70d763929ce3053085bfaa1d78e2 From
 * http://stackoverflow.com/questions/1094893/
 * validate-an-xml-file-against-multiple-schema-definitions/1105871#1105871
 * 
 * @author Jon http://stackoverflow.com/users/82865/jon
 * @author Avi Flax
 * 
 */
class ResourceResolver implements LSResourceResolver
{

    protected class LSInputImpl implements LSInput
    {

        private BufferedInputStream inputStream;

        private String              publicId;

        private String              systemId;

        public LSInputImpl(String publicId, String sysId, InputStream input)
        {
            this.publicId = publicId;
            this.systemId = sysId;
            this.inputStream = new BufferedInputStream(input);
        }

        public String getBaseURI()
        {
            return null;
        }

        public InputStream getByteStream()
        {
            return null;
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

        public BufferedInputStream getInputStream()
        {
            return inputStream;
        }

        public String getPublicId()
        {
            return publicId;
        }

        public String getStringData()
        {
            synchronized (inputStream)
            {
                try
                {
                    byte[] input = new byte[inputStream.available()];
                    inputStream.read(input);
                    String contents = new String(input);
                    return contents;
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    System.out.println("Exception " + e);
                    return null;
                }
            }
        }

        public String getSystemId()
        {
            return systemId;
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

        public void setInputStream(BufferedInputStream inputStream)
        {
            this.inputStream = inputStream;
        }

        public void setPublicId(String publicId)
        {
            this.publicId = publicId;
        }

        public void setStringData(String stringData)
        {
        }

        public void setSystemId(String systemId)
        {
            this.systemId = systemId;
        }

    }

    public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI)
    {

        InputStream resourceAsStream = this.getClass().getResourceAsStream(systemId);
        return new LSInputImpl(publicId, systemId, resourceAsStream);
    }

}