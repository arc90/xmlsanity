#!/usr/bin/env groovy -cp ../../lib/saxon9.jar

import java.io.FileReader
import java.io.StringWriter

import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamSource
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.sax.SAXSource

import org.xml.sax.InputSource

contentFile = new File('1xPolicy.xml')
xsltFile = new File('InsurancePolicy-1.x_to_InsurancePolicy-2.x.xslt')

xslt = TransformerFactory.newInstance().newTemplates(new StreamSource(xsltFile))
transformer = xslt.newTransformer()

transformer.setParameter('OrganizationId', 'whatever')
transformer.setParameter('Program', 'whatever')
transformer.setParameter('SystemOfRecord', 'whatever')
transformer.setParameter('DesiredVersion', '2.3')
transformer.setParameter('DesiredVariant', 'singleterm')

source = new SAXSource(new InputSource(new FileReader(contentFile)))
result = new StreamResult(new StringWriter())

transformer.transform(source, result)

println result.writer