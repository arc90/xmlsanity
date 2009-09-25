using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace arc90.Xml.Validation
{
    public class ValidationResults
    {
        public bool IsValid
        {
            get
            {
                return _messages.Count(var => var.Severity == ValidationSeverity.Error) == 0;
            }
        }

        private List<ValidationMessage> _messages = new List<ValidationMessage>(0);

        public List<ValidationMessage> AllMessages
        {
            get
            {
                return _messages;
            }
        }

        public List<ValidationMessage> Errors
        {
            get
            {
                return IEnumerableToList(from m in _messages where m.Severity == ValidationSeverity.Error select m);
            }
        }

        public List<ValidationMessage> Warnings
        {
            get
            {
                return IEnumerableToList(from m in _messages where m.Severity == ValidationSeverity.Warning select m);
            }
        }

        public List<ValidationMessage> Infos
        {
            get
            {
                return IEnumerableToList(from m in _messages where m.Severity == ValidationSeverity.Info select m);
            }
        }

        private List<ValidationMessage> IEnumerableToList(IEnumerable<ValidationMessage> queryInput)
        {
            return queryInput.ToList<ValidationMessage>();
        }

        public void AddViolation(ValidationMessage violation)
        {
            _messages.Add(violation);
        }

        public override string ToString()
        {
            StringBuilder sb = new StringBuilder(_messages.Count * 50);
            for (int x = 0; x < _messages.Count; x++)
            {
                ValidationMessage v = (ValidationMessage)_messages[x];
                sb.AppendFormat("Msg: {0}, Severity: {1}, Type: {2}, Line: {3}, Details: {4}\n",
                    v.Message,
                    v.Severity.ToString(),
                    v.Type.ToString(),
                    v.LineNumber,
                    v.Details);
            }
            return sb.ToString();
        }

        public void Add(ValidationResults results)
        {
            _messages.AddRange(results.AllMessages);
        }
    }
}
