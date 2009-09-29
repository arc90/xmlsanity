using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Xml.Schema;
using arc90.Xml.Validation;
using System.Xml;

namespace Schematron_Tester
{
    public partial class _Default : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            if (Request.HttpMethod == "GET")
            {
                txtXml.Value = System.IO.File.ReadAllText(Server.MapPath("~/App_Data/default.xml"));
                txtXsd.Value = System.IO.File.ReadAllText(Server.MapPath("~/App_Data/default.xsd"));
                txtSchematron.Value = System.IO.File.ReadAllText(Server.MapPath("~/App_Data/default.sch.xml"));
                typeXsd.Checked = true;
                severityError.Checked = true;
            }
            else
            {
                arc90.Xml.Validation.IValidator validator = null;

                XmlDocument testXml = new XmlDocument();
                XmlDocument xsd = new XmlDocument();
                XmlDocument schematron = new XmlDocument();
                XmlDocument ruleset = null;

                testXml.LoadXml(txtXml.Value);
                xsd.LoadXml(txtXsd.Value);
                schematron.LoadXml(txtSchematron.Value);

                ValidationResults validationResults = null;

                ValidationSeverity minSeverity = ValidationSeverity.Error;
                try
                {
                    minSeverity = (ValidationSeverity)Enum.Parse(typeof(ValidationSeverity), Request.Form["minSeverity"], true);
                }
                catch { }

                switch (Request.Form["validationType"])
                {
                    case "xsd":
                        validator = new XmlValidator(arc90.Xml.Validation.ValidationType.Xsd);
                        ruleset = xsd;
                        break;
                    case "schematron":
                        ShowSchematronInternals(testXml, schematron);

                        validator = new XmlValidator(arc90.Xml.Validation.ValidationType.Schematron);
                        ruleset = schematron;
                        break;
                    case "both":
                        ShowSchematronInternals(testXml, schematron);

                        arc90.Xml.Validation.IValidator schematronValidator = new XmlValidator(arc90.Xml.Validation.ValidationType.Schematron);
                        arc90.Xml.Validation.IValidator xsdValidator = new XmlValidator(arc90.Xml.Validation.ValidationType.Xsd);
                        validationResults = schematronValidator.Validate(testXml, schematron, minSeverity);
                        validationResults.Add(xsdValidator.Validate(testXml, xsd, minSeverity));
                        break;
                    default:
                        Response.Write("Please select a validation type");
                        Response.End();
                        return;
                }

                if (validationResults == null)
                    validationResults = validator.Validate(testXml, ruleset, minSeverity);

                tbodyValidationMessages.InnerHtml = string.Empty;
                if (validationResults.IsValid)
                    tbodyValidationMessages.InnerHtml = "<td colspan=\"4\" align=\"center\" style=\"background-color:#ccffcc\">Document is valid (zero errors)</td>";

                var allMessages = from v in validationResults.AllMessages where v.Severity >= minSeverity orderby v.Severity descending, v.Type select v;

                foreach (ValidationMessage v in allMessages)
                {
                    tbodyValidationMessages.InnerHtml += string.Format(
                        @"<tr style=""background-color:{0}"">
                            <td>{1}</td>
                            <td>{2}</td>
                            <td>{3}</td>
                            <td>{4}</td>
                        </tr>",
                              GetColor(v.Severity),
                              v.Message,
                              v.Severity.ToString(),
                              v.Type.ToString(),
                              v.Details
                              );
                }
            }
        }

        private string GetColor(ValidationSeverity severity)
        {
            switch (severity)
            {
                case ValidationSeverity.Info:
                    return "#ccffcc";
                case ValidationSeverity.Warning:
                    return "#ffff33";
                default:
                    return "#ff6666";
            }
        }

        private void ShowSchematronInternals(XmlDocument testXml, XmlDocument ruleset)
        {
            arc90.Xml.Validation.IValidator validator = new SchematronValidator();
            txtXslt.Value = PrettyPrint(((SchematronValidator)validator).GetValidationXslt(ruleset));
            txtSvrl.Value = PrettyPrint(((SchematronValidator)validator).GetSVRL(testXml, ruleset));
        }

        public string PrettyPrint(XmlDocument xmlDoc)
        {
            if (xmlDoc == null)
                return null;

            try
            {
                using (System.IO.StringWriter sw = new System.IO.StringWriter())
                {
                    System.Xml.XmlNodeReader xmlReader = new System.Xml.XmlNodeReader(xmlDoc);
                    System.Xml.XmlTextWriter xmlWriter = new System.Xml.XmlTextWriter(sw);
                    xmlWriter.Formatting = System.Xml.Formatting.Indented;
                    xmlWriter.Indentation = 4;
                    xmlWriter.IndentChar = ' ';
                    xmlWriter.WriteNode(xmlReader, true);
                    return sw.ToString();
                }
            }
            catch (Exception)
            {
                // just return as-is
                return xmlDoc.OuterXml;
            }
        }

    }
}