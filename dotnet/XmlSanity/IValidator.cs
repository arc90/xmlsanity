using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml;
using System.Xml.XPath;

namespace arc90.Xml.Validation
{
    public interface IValidator
    {
        ValidationResults Validate(XmlReader testXml, Uri ruleset);
        ValidationResults Validate(XmlReader testXml, Uri ruleset, ValidationSeverity minSeverity);

        ValidationResults Validate(XmlNode testXml, XmlNode ruleset);
        ValidationResults Validate(XmlNode testXml, XmlNode ruleset, ValidationSeverity minSeverity);
    }
}
