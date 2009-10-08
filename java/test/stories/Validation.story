import com.arc90.xmlsanity.validation.*

scenario "Using an XSD in an InputStream, a valid document should pass validation", {

    given "an XSD in an InputStream", {
        xsdStream = new FileInputStream(new File("test/stories/insight_policyrequest_2.1.xsd"))
    }

    and "a Validator", {
        validator = new Validator(xsdStream, this.getClass())
    }

    and "a valid XML document", {
        xmlFile = new File("test/validation/policyrequest_good.xml")
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

scenario "Using an XSD in an File, a valid document should pass validation", {

    given "an XSD in a File", {
        xsdFile = new File("test/stories/insight_policyrequest_2.1.xsd")
    }

    and "a Validator", {
        validator = new Validator(xsdFile)
    }

    and "a valid XML document", {
        xmlFile = new File("test/validation/policyrequest_good.xml")
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