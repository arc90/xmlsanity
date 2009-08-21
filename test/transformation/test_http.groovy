#!/usr/bin/env groovy -cp ../../lib/saxon9.jar:../../lib/jdom.jar:../../bin

import com.arc90.xmlsanity.transformation.Transformer

contentFile = new File('1xPolicy.xml')
xsltFile = new File('InsurancePolicy-1.x_to_InsurancePolicy-2.x.xslt')

transformer = new Transformer(xsltFile)

params = [:]
params['OrganizationId'] = 'whatever'
params['Program'] = 'whatever'
params['SystemOfRecord'] = 'whatever'
params['DesiredVersion'] = '2.3'
params['DesiredVariant'] = 'singleterm'

println transformer.transform(contentFile, params)



class FastTransformer extends Resource
{
    def void AcceptRepresentation()
}