package com.arc90.xmlsanity.validation;

import javax.xml.XMLConstants;

public class ValidationType
{
    public final static ValidationType XSD = new ValidationType("XSD", XMLConstants.W3C_XML_SCHEMA_NS_URI);
    
    public final static ValidationType RELAXNG = new ValidationType("RELAX NG", XMLConstants.RELAXNG_NS_URI);
    
    // TODO: figure out what to use for the "schemaLanguage value for Schematron
    // public final static ValidationType SCHEMATRON = new ValidationType("xsd", "");
    
    private final String name;
    private final String schemaLanguage;
    
    protected ValidationType(String name, String schemaLanguage)
    {
        this.name = name;
        this.schemaLanguage = schemaLanguage;
    }

    /**
     * @return the schemaLanguage, useful for passing to a SchemaFactory
     */
    protected String getSchemaLanguage()
    {
        return schemaLanguage;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

}
