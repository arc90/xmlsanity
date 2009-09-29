using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace arc90.Xml.Validation
{
    public enum ValidationSeverity
    {
        Info = 1,
        Warning = 2,
        Error = 4
    }

    public enum ValidationType
    {
        Xsd = 1,
        Schematron = 2
    }
}
