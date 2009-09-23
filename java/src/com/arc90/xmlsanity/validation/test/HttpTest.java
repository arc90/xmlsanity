package com.arc90.xmlsanity.validation.test;

import java.io.File;

import org.restlet.Restlet;
import org.restlet.Router;
import org.restlet.Server;
import org.restlet.data.MediaType;
import org.restlet.data.Protocol;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;

import com.arc90.xmlsanity.validation.ValidationResult;
import com.arc90.xmlsanity.validation.Validator;

public class HttpTest
{

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		final File schemaFile = new File(args[0]);
		final Validator validator = new Validator(schemaFile);
		
		Restlet fastValidator = new Restlet()
		{
			@Override
			public void handle(Request request, Response response)
			{
				try
				{
					ValidationResult validationResult = validator.validate(request.getEntity().getStream());
					
					if (validationResult.isValid() == false)
					{
						response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
						response.setEntity(validationResult.getErrorsAsHtmlList(), MediaType.TEXT_HTML);
					}
				}
				catch (Exception e)
				{
					response.setStatus(Status.SERVER_ERROR_INTERNAL, e);
				}
			}
		};
		
		Restlet slowValidator = new Restlet()
		{
			@Override
			public void handle(Request request, Response response)
			{
				try
				{
					Validator validator = new Validator(schemaFile);
					ValidationResult validationResult = validator.validate(request.getEntity().getStream());
					response.setEntity(validationResult.toString(), MediaType.TEXT_PLAIN);
					
					if (validationResult.isValid() == false)
					{
						response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
					}
				}
				catch (Exception e)
				{
					response.setStatus(Status.SERVER_ERROR_INTERNAL, e);
				}
			}
		};
		
		Router router = new Router();
		router.attach("/fast", fastValidator);
		router.attach("/slow", slowValidator);
		
		Server server = new Server(Protocol.HTTP, 3000, router);
		server.start();
	}

}
