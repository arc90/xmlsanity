using System;
using System.Collections.Generic;
using System.Text;
using System.Xml;
using System.Xml.Xsl;
using System.Xml.XPath;

namespace arc90.Xml.Validation
{
    public class SchematronValidator : IValidator
    {
        private static System.Collections.Generic.Dictionary<int, XslCompiledTransform> schematronValidationTransforms = new Dictionary<int, XslCompiledTransform>();
        private static System.Collections.Generic.Dictionary<int, XmlDocument> schematronValidationXslts = new Dictionary<int, XmlDocument>();

        #region IValidator Members
        public ValidationResults Validate(XmlReader testXml, Uri rulesetUri)
        {
            return Validate(testXml, rulesetUri, ValidationSeverity.Error);
        }

        public ValidationResults Validate(XmlNode testXml, XmlNode ruleset)
        {
            return Validate(testXml, ruleset, ValidationSeverity.Error);
        }

        public ValidationResults Validate(XmlNode testXml, XmlNode ruleset, ValidationSeverity minSeverity)
        {
            XmlDocument schematronReport = GetSVRL(testXml, ruleset);
            return BuildValidationResults(schematronReport, minSeverity);
            
        }

        public ValidationResults Validate(XmlReader testXml, Uri rulesetUri, ValidationSeverity minSeverity)
        {
            XmlDocument schematronReport = GetSVRL(testXml, rulesetUri);
            return BuildValidationResults(schematronReport, minSeverity);
        }

        public XmlDocument GetSVRL(XmlNode testXml, XmlNode ruleset)
        {
            if (ruleset == null)
                throw new ArgumentException("The Schematron ruleset cannot be null", "ruleset");

            XslCompiledTransform schematronValidationTransform = GetValidator(ruleset);

            XmlDocument schematronReport = new XmlDocument();
            schematronReport.Load(Transform(testXml, schematronValidationTransform));
            return schematronReport;
        }

        public XmlDocument GetSVRL(XmlReader testXml, Uri rulesetUri)
        {
            if (rulesetUri == null || rulesetUri.AbsoluteUri == string.Empty)
                throw new ArgumentException("The Schematron location cannot be blank", "rulesetUri");

            XslCompiledTransform schematronValidationTransform = GetValidator(rulesetUri);

            XmlDocument schematronReport = new XmlDocument();
            schematronReport.Load(Transform(testXml, schematronValidationTransform));
            return schematronReport;
        }

        private static ValidationResults BuildValidationResults(XmlDocument schematronReport, ValidationSeverity minSeverity)
        {
            XmlNamespaceManager nsm = new XmlNamespaceManager(schematronReport.NameTable);
            nsm.AddNamespace("svrl", "http://purl.oclc.org/dsdl/svrl");
            XmlNodeList schematronViolations = schematronReport.SelectNodes("/svrl:schematron-output/svrl:failed-assert | /svrl:schematron-output/svrl:successful-report", nsm);

            ValidationResults results = new ValidationResults();
            foreach (XmlNode violation in schematronViolations)
            {
                ValidationSeverity thisSeverity = GetSeverity(violation);
                if (thisSeverity >= minSeverity)
                {
                    bool isForbidden = violation.LocalName == "successful-report";

                    results.AddViolation(new ValidationMessage
                    {
                        Message = violation.InnerText,
                        // At xpath context 'IntegrationInsurancePolicy/Identifiers', required pattern "Identifier[@name='QuoteId']" not found
                        // At xpath context 'IntegrationInsurancePolicy/Identifiers', forbidden pattern "Identifier[@name='Joel'] and Identifier[@name='joel']/@value != 'awesome'" was found
                        Details = string.Format("At xpath context '{0}', {1} pattern \"{2}\" {3} found",
                            violation.Attributes["location"].Value,
                            string.Empty, //isForbidden ? "forbidden" : "required",
                            violation.Attributes["test"].Value,
                            isForbidden ? "was" : "not"),
                        Severity = thisSeverity,
                        LineNumber = -1,
                        Type = ValidationType.Schematron
                    });
                }
            }
            return results;
        }

        private static ValidationSeverity GetSeverity(XmlNode violation)
        {
            XmlAttribute flag = violation.Attributes["flag"];
            
            // If flag is not specified, default to error
            if (flag == null || flag.Value == null || flag.Value == string.Empty)
                return ValidationSeverity.Error;

            string[] splitFlags = flag.Value.ToLower().Split(' ');
            for (int x = 0; x < splitFlags.Length; x++)
            {
                switch (splitFlags[x])
                {
                    case "error":
                        return ValidationSeverity.Error;
                    case "warning":
                        return ValidationSeverity.Warning;
                    case "info":
                        return ValidationSeverity.Info;
                }
            }
            // Didn't recognize the flag, so default to error
            // i.e. "potato" -> error, but "potato info" -> info
            return ValidationSeverity.Error;
        }

        private XslCompiledTransform GetValidator(XmlNode ruleset)
        {
            if (!schematronValidationTransforms.ContainsKey(ruleset.GetHashCode()))
            {
                XmlDocument transformedRules = GetValidationXslt(ruleset);

                XslCompiledTransform schematronValidationTransform = new XslCompiledTransform();
                schematronValidationTransform.Load(transformedRules);
                schematronValidationTransforms.Add(ruleset.GetHashCode(), schematronValidationTransform);                
            }
            return schematronValidationTransforms[ruleset.GetHashCode()];
        }

        private XslCompiledTransform GetValidator(Uri rulesetUri)
        {
            if (!schematronValidationTransforms.ContainsKey(rulesetUri.GetHashCode()))
            {
                XmlDocument transformedRules = GetValidationXslt(rulesetUri);

                XslCompiledTransform schematronValidationTransform = new XslCompiledTransform();
                schematronValidationTransform.Load(transformedRules);
                schematronValidationTransforms.Add(rulesetUri.GetHashCode(), schematronValidationTransform);
            }
            return schematronValidationTransforms[rulesetUri.GetHashCode()];
        }

        public XmlDocument GetValidationXslt(XmlNode ruleset)
        {
            if (!schematronValidationXslts.ContainsKey(ruleset.GetHashCode()))
            {
                XmlDocument output = new XmlDocument();
                XmlReader transformedRules = BuildSchematronTransform(new XmlNodeReader(ruleset));
                output.Load(transformedRules);
                schematronValidationXslts.Add(ruleset.GetHashCode(), output);
            }
            return schematronValidationXslts[ruleset.GetHashCode()];
        }

        public XmlDocument GetValidationXslt(Uri rulesetUri)
        {
            XmlReader transformedRules = null;
            if (!schematronValidationXslts.ContainsKey(rulesetUri.GetHashCode()))
            {
                XmlReader schematronRules = null;
                try
                {
                    schematronRules = new XmlTextReader(rulesetUri.AbsoluteUri);

                    transformedRules = BuildSchematronTransform(schematronRules);

                    XmlDocument output = new XmlDocument();
                    output.Load(transformedRules);
                    schematronValidationXslts.Add(rulesetUri.GetHashCode(), output);
                }
                finally
                {
                    if (schematronRules != null)
                        schematronRules.Close();

                    if (transformedRules != null)
                        transformedRules.Close();
                }

            }
            return schematronValidationXslts[rulesetUri.GetHashCode()];
        }

        private XmlReader BuildSchematronTransform(XmlReader schematronRules)
        {
            XmlReader transformedRules = null;

            // step 1 - bring in any included schematrons
            bool useResource = false;

            if (useResource)
            {
                transformedRules = Transform(schematronRules, GetXslCompiledTransform(XmlSanity.Schematron.schematron_xslt_includes, null));

                // step 2 - bring in any abstract schematrons
                transformedRules = Transform(transformedRules, GetXslCompiledTransform(XmlSanity.Schematron.schematron_xslt_abstracts, null));
                // step 3 - generate the actual validation XSLT
                transformedRules = Transform(transformedRules, GetXslCompiledTransform(XmlSanity.Schematron.schematron_xslt_svrl, null));
            }
            else
            {
                // use file system
                string xsltPath = System.IO.Path.GetDirectoryName(new Uri(System.Reflection.Assembly.GetExecutingAssembly().CodeBase).LocalPath);
                MyXmlUrlResolver resolver = new MyXmlUrlResolver(xsltPath + "\\App_Data\\schematron\\");
                
                transformedRules = Transform(schematronRules, GetXslCompiledTransform(System.IO.File.ReadAllText(xsltPath + "\\App_Data\\schematron\\iso_dsdl_include.xsl"), resolver));
                transformedRules = Transform(transformedRules, GetXslCompiledTransform(System.IO.File.ReadAllText(xsltPath + "\\App_Data/schematron\\iso_abstract_expand.xsl"), resolver));
                transformedRules = Transform(transformedRules, GetXslCompiledTransform(System.IO.File.ReadAllText(xsltPath + "\\App_Data/schematron\\iso_svrl_for_xslt1.xsl"), resolver));
            }
            return transformedRules;
        }


        #endregion

        private string DebugOutput(XmlReader reader)
        {
            StringBuilder sb = new StringBuilder();
            while (reader.Read())
            {
                sb.Append(reader.ReadString());
            }
            return sb.ToString();
        }

        private XmlReader Transform(XmlNode xmlSource, XslCompiledTransform xsltDoc)
        {
            System.IO.MemoryStream ms = new System.IO.MemoryStream();
            xsltDoc.Transform(xmlSource, null, ms);
            ms.Position = 0;
            return XmlReader.Create(ms);
        }

        private XmlReader Transform(XmlReader xmlSource, XslCompiledTransform xsltDoc)
        {
            using (xmlSource)
            {
                System.IO.MemoryStream ms = new System.IO.MemoryStream();

                xsltDoc.Transform(xmlSource, null, ms);
                xmlSource.Close();
                ms.Position = 0;
                
                return XmlReader.Create(ms);
            }
        }

        private System.Xml.Xsl.XslCompiledTransform GetXslCompiledTransform(string xsltContents, XmlResolver resolver)
        {
            System.Xml.Xsl.XslCompiledTransform xslTransform = new System.Xml.Xsl.XslCompiledTransform();
            xslTransform.Load(XmlReader.Create(
                new System.IO.StringReader(xsltContents)), 
                XsltSettings.TrustedXslt, 
                resolver);
            
            return xslTransform;
        }

        private class MyXmlUrlResolver : XmlUrlResolver
        {
            public MyXmlUrlResolver(string defaultFolder)
            {
                _defaultFolder = defaultFolder;
            }

            private string _defaultFolder;

            public override Uri ResolveUri(Uri baseUri, string relativeUri)
            {
                if (baseUri != null)
                    return base.ResolveUri(baseUri, relativeUri);
                else
                    return base.ResolveUri(new Uri(_defaultFolder), relativeUri);
            }
        }

    }
}
