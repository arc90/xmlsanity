import com.arc90.xmlsanity.transformation.*

scenario "an XSLT document in a file, which uses an import statement with a relative path, should successfully compile", {

	given "an XSLT document in a file which uses an import statement with a relative path", {
		xsltFile = new File("test/resources/xslt/iso_svrl_for_xslt2.xsl")
	}
	
	when "a Transformer is created from the XSLT file", {
		transformer = new Transformer(xsltFile)
	}
	
	then "the Transformer should be valid", {
		assert transformer instanceof Transformer
	}

}

scenario "an XSLT document in an InputStream, which uses an import statement with a relative path, should successfully compile", {

	given "an XSLT document in an InputStream which uses an import statement with a relative path", {
		xsltDocStream = new FileInputStream("test/resources/xslt/iso_svrl_for_xslt2.xsl")
	}
	
	when "a Transformer is created from the XSLT file", {
		transformer = new Transformer(xsltDocStream)
	}
	
	then "the Transformer should be valid", {
		assert transformer instanceof Transformer
	}

}