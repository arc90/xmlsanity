#!/usr/bin/env groovy -cp ../../lib/jdom.jar:../../bin

import com.arc90.xmlsanity.validation.Validator

xmlFile = new File("PolicyRequest 2.1 with Credit Card Payment.xml")
xsdFile = new File("insight_policyrequest_2.1.xsd")

println new Validator(xsdFile).validate(xmlFile)