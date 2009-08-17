import javax.xml.XMLConstants
import javax.xml.validation.Schema
import javax.xml.validation.SchemaFactory
import javax.xml.transform.sax.SAXSource
import org.xml.sax.ErrorHandler
import org.xml.sax.InputSource
import org.xml.sax.SAXException
import org.xml.sax.SAXParseException

class MyErrorHandler implements ErrorHandler
{
    private final List<SAXParseException> errors = new ArrayList<SAXParseException>();
    
    public Collection<SAXParseException> getErrors()
	{
		return errors;
	}
    
    public void error(SAXParseException exception) throws SAXException
	{
		errors.add(exception);
	}

	public void fatalError(SAXParseException exception) throws SAXException
	{
		errors.add(exception);
	}
	
	public void warning(SAXParseException exception) throws SAXException
	{
	}
}

schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(new File(args[0]))
validator = schema.newValidator()
validator.errorHandler = new MyErrorHandler()
source = new SAXSource(new InputSource(new FileReader(new File(args[1]))))

validator.validate(source)

if (validator.errorHandler.errors.size() == 0)
{
    println 'valid'
}
else
{                    
    println 'invalid, because: '
    
    validator.errorHandler.errors.each { e ->
        println e.message + ' Line ' + e.lineNumber + ', column ' + e.columnNumber + '.'
    }
}