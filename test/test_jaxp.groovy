import javax.xml.XMLConstants
import javax.xml.validation.Schema
import javax.xml.validation.SchemaFactory
import javax.xml.transform.sax.SAXSource
import org.xml.sax.InputSource

schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(new File(args[0]))
validator = schema.newValidator()
source = new SAXSource(new InputSource(new FileReader(new File(args[1]))))

try {
    validator.validate(source)
    println 'valid'
}
catch (Exception e)
{                    
    println 'invalid, because: ' + e.message + ' Line ' + e.lineNumber + ', column ' + e.columnNumber + '.'
}