using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace arc90.Xml.Validation
{
    public class ValidationMessage
    {
        public ValidationSeverity Severity { get; set; }
        public ValidationType Type { get; set; }
        public string Message { get; set; }
        public string Details { get; set; }
        public int LineNumber { get; set; }
        public Exception Exception { get; set; }
    }
}
