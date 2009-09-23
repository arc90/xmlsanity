#!/usr/bin/env groovy -cp ../../lib/jdom.jar:../../bin

import javax.xml.XMLConstants
import javax.xml.validation.Schema
import javax.xml.validation.SchemaFactory
import javax.xml.transform.sax.SAXSource
import org.xml.sax.InputSource

xmlFile = new File("PolicyRequest 2.1 with Credit Card Payment.xml")
xsdFile = new File("insight_policyrequest_2.1.xsd")

schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(xsdFile)
validator = schema.newValidator()
source = new SAXSource(new InputSource(new FileReader(xmlFile)))

try {
    validator.validate(source)
    println 'valid'
}
catch (Exception e)
{                    
    println 'invalid, because: ' + e.message + ' Line ' + e.lineNumber + ', column ' + e.columnNumber + '.'
}