<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="Default.aspx.cs" Inherits="Schematron_Tester._Default" ValidateRequest="false"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" >
<head runat="server">
    <title>XmlSanity Demo</title>
    <style type="text/css">
        #TextArea1
        {
            width: 674px;
        }
    </style>
</head>
<body>
    <form id="form1" method="post" runat="server">
    <table width="100%" cellpadding="2" cellspacing="0" border="1">
        <tr>
            <td style="width:100px"><label for="txtXml">XML to Validate:</label></td>
            <td><textarea id="txtXml" name="txtXml" rows="8" cols="100" width="100%" runat="server" style="width:100%"></textarea></td>
        </tr>
        <tr>
            <td style="width:100px"><label for="txtXsd">XSD:</label></td>
            <td><textarea id="txtXsd" name="txtXsd" rows="8" cols="100" width="100%" runat="server" style="width:100%"></textarea></td>
        </tr>
        <tr>
            <td style="width:100px"><label for="txtXml">Schematron:</label></td>
            <td><textarea id="txtSchematron" name="txtSchematron" rows="8" cols="100" width="100%" runat="server" style="width:100%"></textarea></td>
        </tr>
        <tr>
            <td style="width:100px"><label>Options:</label></td>
            <td>
                <table cellpadding="2" cellspacing="0" border="0" width="100%">
                <tr>
                <td align="right"><label for="validationType">Validation Type:</label></td>
                <td><input type="radio" name="validationType" id="typeXsd" value="xsd" runat="server"/> <label for="typeXsd">XSD</label><br />
                    <input type="radio" name="validationType" id="typeSchematron" value="schematron" runat="server"/> <label for="typeSchematron">Schematron</label><br />
                    <input type="radio" name="validationType" id="typeBoth" value="both" runat="server"/> <label for="typeBoth">Both</label>
                </td>
                <td align="right"><label for="minSeverity">Validation Level:</label></td>
                <td><input type="radio" name="minSeverity" id="severityInfo" value="Info" runat="server"/> <label for="severityInfo">Info (returns all validation messages)</label><br />
                    <input type="radio" name="minSeverity" id="severityWarning" value="Warning" runat="server"/> <label for="severityWarning">Warning (returns Warnings and Errors)</label><br />
                    <input type="radio" name="minSeverity" id="severityError" value="Error" runat="server"/> <label for="severityError">Error (returns Errors only)</label>
                </td>
                </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td style="width:100px" colspan="2" align="center"><input type="submit" value="Validate!" /></td>
        </tr>
        <tr>
            <td style="width:100px"><label for="txtXml">Validation messages:</label><br /></td>
            <td>
                <table style="width:100%" id="tblValidationMessages" border="1" cellpadding="2" cellspacing="0">
                    <tr>
                        <th>Message</th>
                        <th>Severity</th>
                        <th>Type</th>
                        <th>Details</th>
                     </tr>
                    <tbody runat="server" id="tbodyValidationMessages">
                    </tbody>
                </table>
                </td>
        </tr>
        <tr>
            <td style="width:100px"><label for="txtXslt">SVRL Validation Report (Schematron only):</label></td>
            <td><textarea id="txtSvrl" name="txtSvrl" rows="15" cols="150" style="width:99%" runat="server"></textarea></td>
        </tr>
        <tr>
            <td style="width:100px"><label for="txtXslt">Validation XSLT (Schematron only):</label></td>
            <td><textarea id="txtXslt" name="txtXslt" rows="15" cols="150" style="width:99%" runat="server"></textarea></td>
        </tr>
    </table>

    </form>
</body>
</html>
