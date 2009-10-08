import com.arc90.xmlsanity.validation.*

/*scenario "Using an XsdValidator and an XSD file, a valid document should pass validation", {

    given "an XSD file", {
        xsdFile = new File("test/resources/schemata/insight_policyrequest_2.1.xsd")
    }

    and "a Validator", {
        validator = new XsdValidator(xsdFile)
    }

    and "a valid XML document", {
        xmlFile = new File("test/resources/test_docs/policyrequest_good.xml")
    }

    when "validation is called", {
        results = validator.validate(xmlFile)
    }

    then "results should be valid", {
        results.isValid().shouldBe true
        results.isInvalid().shouldBe false
    }

    and "results should contain no errors", {
        results.errors.size().shouldBe 0
        results.errorsAsHtmlList.length().shouldBe 0
    }

}  */

scenario "Using a RngValidator and a RNC file, a valid document should pass validation", {

    given "an RNC file", {
        rngFile = new File("test/resources/schemata/wadl20061109.rnc")
    }

    and "a Validator", {
        validator = new RngValidator(rngFile)
    }

    and "a valid XML document", {
        xmlFile = new File("test/resources/test_docs/wadl_good.xml")
    }

    when "validation is called", {
        results = validator.validate(xmlFile)
    }

    then "results should be valid", {
        results.isValid().shouldBe true
        results.isInvalid().shouldBe false
    }

    and "results should contain no errors", {
        results.errors.size().shouldBe 0
        results.errorsAsHtmlList.length().shouldBe 0
    }

}