<?xml version="1.0" encoding="utf-8" ?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >
  <title>Content Rules for People Documents</title>

  <pattern id="firstNameTest" is-a="requiredField">
    <param name="fieldName" value="/Person/Name/First"/>
  </pattern>
  <pattern id="lastNameTest" is-a="requiredField">
    <param name="fieldName" value="/Person/Name/Last"/>
  </pattern>
  <pattern id="schoolNameTest" is-a="requiredField">
    <param name="fieldName" value="/Person/Schooling/School/Name"/>
  </pattern>

  <pattern id="personDates" is-a="orderedDates">
    <param name="context" value="/Person/Dates"/>
    <param name="firstDate" value="Born"/>
    <param name="secondDate" value="Died"/>
  </pattern>

  <pattern id="schoolDates" is-a="orderedDates">
    <param name="context" value="/Person/Schooling/School/Dates"/>
    <param name="firstDate" value="Start"/>
    <param name="secondDate" value="End"/>
  </pattern>

  <pattern abstract="true" id="orderedDates">
    <rule context="$context">
      <assert test="translate($firstDate, '-', '') &lt;= translate($secondDate, '-', '')">In $context, $secondDate cannot come before $firstDate.</assert>
    </rule>
  </pattern>

  <pattern abstract="true" id="requiredField">
    <rule context="$fieldName">
      <assert test=". != ''">The field '$fieldName' cannot be blank.</assert>
    </rule>
  </pattern>

</schema>