#!/usr/bin/env groovy -cp ../../lib/saxon9.jar:../../lib/jdom.jar:../../bin

import com.arc90.xmlsanity.transformation.Transformer

contentFile = new File('1xPolicy.xml')
xsltFile = new File('InsurancePolicy-1.x_to_InsurancePolicy-2.x.xslt')

try {
    transformer = new Transformer(xsltFile)
}
catch (Exception e) 
{
    println e.getMessage()
    e.printStackTrace()
    return
}

params = [:]
params['OrganizationId'] = 'whatever'
params['Program'] = 'whatever'
params['SystemOfRecord'] = 'whatever'
params['DesiredVersion'] = '2.3'
params['DesiredVariant'] = 'singleterm'

println transformer.transform(contentFile, params)