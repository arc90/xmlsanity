import com.arc90.xmlsanity.validation.*


scenario "Using a RngValidator and a RNG file, valid documents should pass validation", {

    given "a RNG file", {
        rngFile = new File("resources/schemata/wadl20061109.rng")
    }

      and "a Validator", {
          validator = new RngValidator(rngFile)
      }
      
      and "a valid XML document as a File", {
          xmlFile = new File("resources/test_docs/wadl_good.xml")
      }

      and "a valid XML document as a String", {
          xmlString = xmlFile.text
      }
    
      and "a valid XML document as an InputStream", {
          xmlStream = new FileInputStream(xmlFile)
      }

      and "a valid XML document as a ByteArray", {
          xmlByteArray = xmlString.getBytes("UTF-8")
      }

    when "validation is called", {
        results = []
        
        [xmlFile, xmlString, xmlStream, xmlByteArray].each {
          results.add(validator.validate(it))
        }
    }

    then "results should be valid", {
      results.each {
        assert it != null
        it.isValid().shouldBe true
        it.isInvalid().shouldBe false
      }
    }

      and "results should contain no errors", {
        results.each {
          assert it.errors != null
          it.errors.size().shouldBe 0
          it.errorsAsHtmlList.length().shouldBe 0
        }
      }

}


scenario "Using a RngValidator and a RNC file, valid documents should pass validation", {

    given "a RNG file", {
        rncFile = new File("resources/schemata/wadl20061109.rnc")
    }

      and "a Validator", {
          validator = new RngValidator(rncFile)
      }

      and "a valid XML document as a File", {
          xmlFile = new File("resources/test_docs/wadl_good.xml")
      }

      and "a valid XML document as a String", {
          xmlString = xmlFile.text
      }
    
      and "a valid XML document as an InputStream", {
          xmlStream = new FileInputStream(xmlFile)
      }

      and "a valid XML document as a ByteArray", {
          xmlByteArray = xmlString.getBytes("UTF-8")
      }

    when "validation is called", {
        results = []
        
        [xmlFile, xmlString, xmlStream, xmlByteArray].each {
          results.add(validator.validate(it))
        }
    }

    then "results should be valid", {
      results.each {
        assert it != null
        it.isValid().shouldBe true
        it.isInvalid().shouldBe false
      }
    }

      and "results should contain no errors", {
        results.each {
          assert it.errors != null
          it.errors.size().shouldBe 0
          it.errorsAsHtmlList.length().shouldBe 0
        }
      }

}


scenario "Using a RngValidator and a RNG file, invalid documents should fail validation", {

    given "an RNG file", {
        rngFile = new File("resources/schemata/wadl20061109.rng")
    }
    
    given "a Validator", {
        validator = new RngValidator(rngFile)
    }
    
    and "an invalid XML document as a File", {
        xmlFile = new File("resources/test_docs/wadl_bad.xml")
    }

    and "an invalid XML document as a String", {
        xmlString = xmlFile.text
    }
  
    and "an invalid XML document as an InputStream", {
        xmlStream = new FileInputStream(xmlFile)
    }

    and "an invalid XML document as a ByteArray", {
        xmlByteArray = xmlString.getBytes("UTF-8")
    }
    
    when "validation is called", {
        results = []
        
        [xmlFile, xmlString, xmlStream, xmlByteArray].each {
          results.add(validator.validate(it))
        }
    }
    
    then "results should be invalid", {
      results.each {
        assert it != null
        it.isInvalid().shouldBe true
        it.isValid().shouldBe false
      }
    }
    
    and "results should contain errors", {
      results.each {
        assert it.errors != null
        it.errors.size().shouldBeGreaterThan 0
        it.errorsAsHtmlList.length().shouldBeGreaterThan 0
      }
    }
    
}


scenario "Using a RngValidator and a RNC file, invalid documents should fail validation", {

    given "an RNG file", {
        rncFile = new File("resources/schemata/wadl20061109.rnc")
    }
    
    given "a Validator", {
        validator = new RngValidator(rncFile)
    }
    
    and "an invalid XML document as a File", {
        xmlFile = new File("resources/test_docs/wadl_bad.xml")
    }

    and "an invalid XML document as a String", {
        xmlString = xmlFile.text
    }
  
    and "an invalid XML document as an InputStream", {
        xmlStream = new FileInputStream(xmlFile)
    }

    and "an invalid XML document as a ByteArray", {
        xmlByteArray = xmlString.getBytes("UTF-8")
    }
    
    when "validation is called", {
        results = []
        
        [xmlFile, xmlString, xmlStream, xmlByteArray].each {
          results.add(validator.validate(it))
        }
    }
    
    then "results should be invalid", {
      results.each {
        assert it != null
        it.isInvalid().shouldBe true
        it.isValid().shouldBe false
      }
    }
    
    and "results should contain errors", {
      results.each {
        assert it.errors != null
        it.errors.size().shouldBeGreaterThan 0
        it.errorsAsHtmlList.length().shouldBeGreaterThan 0
      }
    }
    
}


scenario "An RngValidator should be reusable", {

    given "a RNG file", {
        rngFile = new File("resources/schemata/wadl20061109.rng")
    }

    and "a Validator", {
        validator = new RngValidator(rngFile)
    }

    and "an XML document", {
        xmlFile = new File("resources/test_docs/policyrequest_good.xml")
    }

    when "validation is called multiple times", {
        5.times {
            validator.validate(xmlFile)
        }
    }

    then "no exception should be thrown", {
    }

}