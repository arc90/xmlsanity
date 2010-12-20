import com.arc90.xmlsanity.validation.*

scenario "Using an XsdValidator and an XSD file, a valid document should pass validation", {

    given "an XSD file", {
        xsdFile = new File("resources/schemata/wadl.xsd")
    }

    and "a Validator", {
        validator = new XsdValidator(xsdFile)
    }

    and "a valid XML document", {
        xmlFile = new File("resources/test_docs/wadl_good.xml")
    }

    when "validation is called", {
        results = validator.validate(xmlFile)
    }

    then "results should be valid", {
        assert results != null
        results.isValid().shouldBe true
        results.isInvalid().shouldBe false
    }

    and "results should contain no errors", {
        assert results.errors != null
        results.errors.size().shouldBe 0
        results.errorsAsHtmlList.length().shouldBe 0
    }

}

scenario "Using an XsdValidator and an XSD file, an invalid document should fail validation", {

    given "an XSD file", {
        xsdFile = new File("resources/schemata/wadl.xsd")
    }

    and "a Validator", {
        validator = new XsdValidator(xsdFile)
    }

    and "an invalid XML document", {
        xmlFile = new File("resources/test_docs/wadl_bad.xml")
    }

    when "validation is called", {
        results = validator.validate(xmlFile)
    }

    then "results should be invalid", {
        assert results != null
        results.isInvalid().shouldBe true
        results.isValid().shouldBe false
    }

    and "results should contain errors", {
        assert results.errors != null
        results.errors.size().shouldBeGreaterThan 0
        results.errorsAsHtmlList.length().shouldBeGreaterThan 0
    }

}

scenario "An XsdValidator should be reusable", {

    given "an XSD file", {
        xsdFile = new File("resources/schemata/wadl.xsd")
    }

    and "a Validator", {
        validator = new XsdValidator(xsdFile)
    }

    and "a valid XML document", {
        xmlFile = new File("resources/test_docs/wadl_good.xml")
    }

    when "validation is called multiple times", {
        results = []
        
        5.times {
            results += validator.validate(xmlFile)
        }
    }

    then "no exception should be thrown", {
    }
    
    and "the results should be valid", {
        results.each {
            it.isValid().shouldBe true
            it.isInvalid().shouldBe false
        }
    }

}

scenario "A malformed XML document should fail validation", {

    given "an XSD file", {
        xsdFile = new File("resources/schemata/wadl.xsd")
    }
    
    given "a Validator", {
        validator = new XsdValidator(xsdFile)
    }
    
    and "a malformed XML document", {
        xmlFile = new File("resources/test_docs/malformed.xml")
    }
    
    when "validation is called", {
        results = validator.validate(xmlFile)
    }
    
    then "results should be invalid", {
        assert results != null
        results.isInvalid().shouldBe true
        results.isValid().shouldBe false
    }
    
    and "results should contain errors", {
        assert results.errors != null
        results.errors.size().shouldBeGreaterThan 0
        results.errorsAsHtmlList.length().shouldBeGreaterThan 0
    }
    
}
