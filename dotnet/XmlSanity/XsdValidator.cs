using System;
using System.Collections.Generic;
using System.Text;
using System.Xml;
using System.Xml.Schema;
using System.Xml.XPath;

namespace arc90.Xml.Validation
{
    public class XsdValidator : IValidator
    {
        private static System.Collections.Generic.Dictionary<int, XmlSchema> cachedSchemas = new Dictionary<int, XmlSchema>();
        private ValidationResults _validationResults = new ValidationResults();
        private ValidationSeverity _minSeverity = ValidationSeverity.Error;

        #region IValidator Members
        public ValidationResults Validate(XmlReader testXml, Uri xsdUri)
        {
            return Validate(testXml, xsdUri, ValidationSeverity.Error);
        }

        public ValidationResults Validate(XmlNode testXml, XmlNode xsd)
        {
            return Validate(testXml, xsd, ValidationSeverity.Error);
        }

        public ValidationResults Validate(XmlNode testXml, XmlNode xsd, ValidationSeverity minSeverity)
        {
            if (testXml == null)
                throw new ArgumentNullException("testXml", "The test document cannot be null");
            if (xsd == null)
                throw new ArgumentNullException("xsd", "The XSD document cannot be null");

            XmlReaderSettings settings = GetReaderSettings(xsd);
            _minSeverity = minSeverity;

            lock (_validationResults = new ValidationResults())
            {
                using (XmlReader docReader = new XmlNodeReader(testXml))
                {
                    using (XmlReader validatingReader = XmlReader.Create(docReader, settings))
                    {
                        while (validatingReader.Read()) ;
                        validatingReader.Close();
                    }
                    docReader.Close();
                }
            }
            return _validationResults;
        }

        public ValidationResults Validate(XmlReader testXml, Uri xsdUri, ValidationSeverity minSeverity)
        {
            if (xsdUri == null || xsdUri.AbsoluteUri == string.Empty)
                throw new ArgumentException("The schema location cannot be blank", "xsdUri");

            XmlReaderSettings settings = GetReaderSettings(xsdUri);
            _minSeverity = minSeverity;

            lock (_validationResults = new ValidationResults())
            {
                using (testXml)
                {
                    using (XmlReader xr = XmlReader.Create(testXml, settings))
                    {
                        while (xr.Read()) ;
                        xr.Close();
                    }
                }
            }
            return _validationResults;
        }

        #endregion

        private void ValidationCallback(object sender, ValidationEventArgs args)
        {
            // ignore
            if (args.Severity == XmlSeverityType.Warning && _minSeverity > ValidationSeverity.Warning)
                return;

            _validationResults.AddViolation(new ValidationMessage
            {
                Exception = args.Exception,
                LineNumber = args.Exception == null ? -1 : args.Exception.LineNumber,
                Message = args.Message,
                Details = "XSD rule violation",
                Severity = (args.Severity == XmlSeverityType.Error) ? ValidationSeverity.Error : ValidationSeverity.Warning,
                Type = ValidationType.Xsd
            });
        }

        private XmlReaderSettings GetReaderSettings(XmlNode xsd)
        {
            XmlSchema schema = null;
            if (cachedSchemas.ContainsKey(xsd.GetHashCode()))
            {
                schema = cachedSchemas[xsd.GetHashCode()];
            }
            else
            {
                using (XmlReader xsdReader = new XmlNodeReader(xsd))
                {
                    schema = XmlSchema.Read(xsdReader, null);
                    cachedSchemas[xsd.GetHashCode()] = schema;
                    xsdReader.Close();
                }
            }
            return InitializeSettings(schema);
        }

        private XmlReaderSettings GetReaderSettings(Uri xsdUri)
        {
            XmlSchema schema = null;

            if (cachedSchemas.ContainsKey(xsdUri.GetHashCode()))
            {
                schema = cachedSchemas[xsdUri.GetHashCode()];
            }
            else
            {
                using (XmlReader xsdReader = XmlReader.Create(xsdUri.AbsoluteUri))
                {
                    schema = XmlSchema.Read(XmlReader.Create(xsdUri.AbsoluteUri), null);
                    cachedSchemas[xsdUri.GetHashCode()] = schema;
                    xsdReader.Close();
                }
            }

            return InitializeSettings(schema);
        }

        private XmlReaderSettings InitializeSettings(XmlSchema schema)
        {
            XmlReaderSettings settings = new XmlReaderSettings();
            
            settings.ProhibitDtd = true;
            settings.ValidationType = System.Xml.ValidationType.Schema;
            settings.ValidationFlags = XmlSchemaValidationFlags.ReportValidationWarnings
                | XmlSchemaValidationFlags.ProcessIdentityConstraints
                | XmlSchemaValidationFlags.ProcessInlineSchema
                | XmlSchemaValidationFlags.ProcessSchemaLocation;
            settings.Schemas.Add(schema);
            settings.ValidationEventHandler += ValidationCallback;
            settings.Schemas.Compile();
            return settings;
        }
    }
}
