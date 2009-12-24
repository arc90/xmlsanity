import com.arc90.xmlsanity.validation.*

/* These scenarios were set up for an aluminum 13" Apple MacBook,
  2.4 GHz Intel Core 2 Duo, 4 GB RAM, and an SSD; running OS X 10.6.2, Java 1.6.0_17 64-bit, and easyb 0.9.6 */

scenario "Single-Threaded XSD validation should be fast", {
	
	given "an XSD file", {
		xsdFile = new File("resources/schemata/wadl.xsd")
	}
	
  	and "a Validator", {
  		validator = new XsdValidator(xsdFile)
  	}
	
  	and "a valid XML document", {
  		xmlBytes = new File("resources/test_docs/wadl_good.xml").readBytes()
  	}
	
  	and "a variable containing the time before starting", {
  		start = System.currentTimeMillis()
  	}

  print "Starting single-threaded XSD validation... "
	
	when "validation is called 2000 times", {
		2000.times {
		    validator.validate(new ByteArrayInputStream(xmlBytes))
		}
		
		elapsed = System.currentTimeMillis() - start
	}
	
	println "took " + elapsed / 1000 + " seconds"
	
	then "elapsed time should be less than 5 seconds", {	
		elapsed.shouldBeLessThan 5000
	}
}


scenario "Multi-Threaded XSD validation should be fast", {
	
	given "an XSD file", {
		xsdFile = new File("resources/schemata/wadl.xsd")
	}
	
  	and "a Validator", {
  		validator = new XsdValidator(xsdFile)
  	}
	
  	and "a valid XML document", {
  		xmlBytes = new File("resources/test_docs/wadl_good.xml").readBytes()
  	}

  	and "a variable containing the time before starting", {
  		start = System.currentTimeMillis()
  	}
	
	print "Starting multi-threaded XSD validation... "
	
	when "validation is called 1000 times each by 2 different threads", {
		threads = []
		
		2.times {
			thread = Thread.start {
        		1000.times {
        			validator.validate(new ByteArrayInputStream(xmlBytes))
        		}
			}
			
			threads.add(thread)
		}
		
		threads.each {
		    it.join()
		}
				
		elapsed = System.currentTimeMillis() - start
	}

  println "took " + elapsed / 1000 + " seconds"
	
	then "elapsed time should be less than 3 seconds", {
		elapsed.shouldBeLessThan 3000
	}
}


scenario "Single-Threaded RNG validation should be fast", {
	
	given "a RNG file", {
		schemaFile = new File("resources/schemata/wadl.rng")
	}
	
	  and "a Validator", {
  		validator = new RngValidator(schemaFile)
  	}
	
  	and "a valid XML document", {
  		xmlBytes = new File("resources/test_docs/wadl_good.xml").readBytes()
  	}
	
  	and "a variable containing the time before starting", {
  		start = System.currentTimeMillis()
  	}
	
	print "Starting single-threaded RNG validation... "
	
	when "validation is called 4000 times", {
		4000.times {
		    validator.validate(new ByteArrayInputStream(xmlBytes))
		}
		
		elapsed = System.currentTimeMillis() - start
	}

	println "took " + elapsed / 1000 + " seconds"
		
	then "elapsed time should be less than 2 seconds", {
		elapsed.shouldBeLessThan 2000
	}
}


scenario "Multi-Threaded RNG validation should be fast", {
	
	given "a RNC file", {
		schemaFile = new File("resources/schemata/wadl.rnc")
	}
	
  	and "a Validator", {
  		validator = new RngValidator(schemaFile)
  	}

  	and "a valid XML document", {
  		xmlBytes = new File("resources/test_docs/wadl_good.xml").readBytes()
  	}

  	and "a variable containing the time before starting", {
  		start = System.currentTimeMillis()
  	}

	print "Starting multi-threaded RNG validation... "
	
	when "validation is called 2000 times each by 2 different threads", {
		threads = []
		
		2.times {
			thread = Thread.start {
        2000.times {
        	validator.validate(new ByteArrayInputStream(xmlBytes))
        }
			}
			
			threads.add(thread)
		}
		
		threads.each {
		  it.join()
		}
				
		elapsed = System.currentTimeMillis() - start
	}

	println "took " + elapsed / 1000 + " seconds"
	
	then "elapsed time should be less than 1.2 seconds", {
		elapsed.shouldBeLessThan 1200
	}
}
