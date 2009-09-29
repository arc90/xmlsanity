using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml;
using System.Xml.XPath;

namespace arc90.Xml.Validation
{
    public class XmlValidator : IValidator
    {
        private System.Collections.Generic.List<IValidator> validators = new List<IValidator>(1);

        public XmlValidator(Validation.ValidationType validationTypes)
        {
            if ((validationTypes | ValidationType.Xsd) == ValidationType.Xsd)
                validators.Add(new XsdValidator());
            if ((validationTypes | ValidationType.Schematron) == ValidationType.Schematron)
                validators.Add(new SchematronValidator());
        }

        #region IValidator Members
        public ValidationResults Validate(XmlReader testXml, Uri ruleset)
        {
            return Validate(testXml, ruleset, ValidationSeverity.Error);
        }

        public ValidationResults Validate(XmlNode testXml, XmlNode ruleset)
        {
            return Validate(testXml, ruleset, ValidationSeverity.Error);
        }

        public ValidationResults Validate(XmlNode testXml, XmlNode ruleset, ValidationSeverity minSeverity)
        {
            ValidationResults results = new ValidationResults();
            for (int x = 0; x < validators.Count; x++)
            {
                ValidationResults thisResult = validators[x].Validate(testXml, ruleset, minSeverity);
                results.Add(thisResult);
            }
            return results;

        }

        public ValidationResults Validate(XmlReader testXml, Uri ruleset, ValidationSeverity minSeverity)
        {
            ValidationResults results = new ValidationResults();
            for (int x = 0; x < validators.Count; x++)
            {
                ValidationResults thisResult = validators[x].Validate(testXml, ruleset, minSeverity);
                results.Add(thisResult);
            }
            return results;
        }

        public override string ToString()
        {
            StringBuilder sb = new StringBuilder();
            for (int x = 0; x < validators.Count; x++)
            {
                sb.Append(validators.ToString());
            }
            return sb.ToString();
        }

        #endregion
    }
}
