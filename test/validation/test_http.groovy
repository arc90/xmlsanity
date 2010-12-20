#!/usr/bin/env groovy -cp ../../bin:../../lib/jdom.jar:../../lib/org.restlet.jar:../../lib/com.noelios.restlet.jar:../../lib/com.noelios.restlet.ext.jetty_6.1.jar:../../lib/org.mortbay.jetty.jar:../../lib/org.mortbay.jetty.util.jar:../../lib/org.mortbay.jetty.ajp.jar:../../lib/org.mortbay.jetty.https.jar

import com.arc90.xmlsanity.validation.Validator

import org.restlet.*
import org.restlet.data.*
import org.restlet.resource.*

class FastValidator extends Resource
{
    private static final validator = new Validator(new File("insight_policyrequest_2.1.xsd"))

    public FastValidator(Context context, Request request, Response response)
    {
        super(context, request, response)
        readable = false
    }
    
    public boolean allowPost()
    {
        return true
    }
    
    public void acceptRepresentation(Representation representation)
    {
        def validationResult = validator.validate(representation.text)

        if (validationResult.isValid() == false)
        {
            response.status = Status.CLIENT_ERROR_BAD_REQUEST
        }

        response.entity = new StringRepresentation(validationResult.toString() + '\n', MediaType.TEXT_PLAIN, Language.ENGLISH, null)
    }
}




finder = new Finder()
finder.targetClass = FastValidator.class

server = new Server(Protocol.HTTP, 3000, finder)
server.start()