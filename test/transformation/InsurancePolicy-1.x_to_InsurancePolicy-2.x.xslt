<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsd="http://www.w3.org/2001/XMLSchema-datatypes" xmlns:ics="http://www.ics360.com/ns" version="1.0">

	<xsl:param name="InsightPolicyId" select="''"/>
	<xsl:param name="OrganizationId"/>
	<xsl:param name="Program"/>
	<xsl:param name="SystemOfRecord"/>
	<xsl:param name="DesiredVersion" select="'2.2'"/>
	<xsl:param name="DesiredVariant" select="'multiterm'"/>
	<xsl:param name="TimeZoneOffset" select="'-04:00'"/>

	<xsl:include href="PolicyRequest-1.2_to_PolicyRequest-2.1.xslt"/>

	<xsl:template match="/">

		<xsl:variable name="OriginalPolicy" select="/InsurancePolicy"/>

		<xsl:variable name="DataItemsFromOriginalPolicy"
			select="
			$OriginalPolicy/Quote/Inputs/Input |
			$OriginalPolicy/Application/Inputs/Input |
			$OriginalPolicy/Quote/Calculations/Calculation |
			$OriginalPolicy/Application/Calculations/Calculation |
			$OriginalPolicy/PolicyRequest/Underwriting/DataItem"/>

		<xsl:if test="name(/*) != 'InsurancePolicy'">
			<Error code="INVALID_DOCUMENT">
				<Message>This XSLT can only transform an InsurancePolicy document, not a {name(/*[0])} document.</Message>
			</Error>
			<xsl:message terminate="yes"/>
		</xsl:if>

		<xsl:if test="number($OriginalPolicy/@schemaVersion) >= 2">
			<Error code="INVALID_DOCUMENT">
				<Message>This XSLT can only transform a 1.X InsurancePolicy document.</Message>
			</Error>
			<xsl:message terminate="yes"/>
		</xsl:if>

		<xsl:if test="string-length($OrganizationId) = 0">
			<Error code="INVALID_PARAMETER_VALUE">
				<Message>The parameter 'OrganizationId' must be provided.</Message>
			</Error>
			<xsl:message terminate="yes"/>
		</xsl:if>

		<xsl:if test="string-length($Program) = 0">
			<Error code="INVALID_PARAMETER_VALUE">
				<Message>The parameter 'Program' must be provided.</Message>
			</Error>
			<xsl:message terminate="yes"/>
		</xsl:if>

		<xsl:if test="$DesiredVariant != 'multiterm' and $DesiredVariant != 'singleterm'">
			<Error code="INVALID_PARAMETER_VALUE">
				<Message>The parameter 'DesiredVariant' may only have the value 'multiterm' or 'singleterm'. The value supplied, '<xsl:value-of select="$DesiredVariant"/>', is not supported.</Message>
			</Error>
			<xsl:message terminate="yes"/>
		</xsl:if>

		<xsl:if test="number($DesiredVersion) &lt; 2.2 or number($DesiredVersion) > 2.3">
			<Error code="INVALID_PARAMETER_VALUE">
				<Message>The parameter 'DesiredVersion' may only have the values 2.2 or 2.3. The value supplied, '<xsl:value-of select="$DesiredVersion"/>', is not supported.</Message>
			</Error>
			<xsl:message terminate="yes"/>
		</xsl:if>
	
		<InsurancePolicy schemaVersion="{$DesiredVersion}" variant="{$DesiredVariant}">

			<Identifiers>
				<xsl:if test="string-length($InsightPolicyId) > 0">
					<Identifier name="InsightPolicyId" value="{$InsightPolicyId}"/>
				</xsl:if>

				<Identifier name="QuoteNumber" value="{$OriginalPolicy/@id}"/>
				<Identifier name="pxServerIndex" value="{$OriginalPolicy/@index}"/>

				<xsl:for-each select="$OriginalPolicy/AlternateIDs/AlternateID">
					<xsl:if test="contains('InsightPolicyId', @name) = false()">
						<xsl:if test="text()">
							<Identifier name="{@name}" value="{text()}"/>
						</xsl:if>
					</xsl:if>
				</xsl:for-each>
			</Identifiers>

			<Management>
				<PolicyState>
					<xsl:choose>
						<xsl:when test="$OriginalPolicy/@percentageComplete = 1">ACTIVEPOLICY</xsl:when>
						<xsl:otherwise>ACTIVEQUOTE</xsl:otherwise>
					</xsl:choose>
				</PolicyState>

				<ProgramAdministrator>
					<xsl:value-of select="$OrganizationId"/>
				</ProgramAdministrator>

				<Carrier>
					<xsl:value-of select="$OriginalPolicy/PolicyRequest/basecriteria/Carrier"/>
				</Carrier>

				<xsl:if test="string-length($Program) > 0">
					<Program>
						<xsl:value-of select="$Program"/>
					</Program>
				</xsl:if>

				<xsl:if test="string-length($SystemOfRecord) > 0">
					<SystemOfRecord><xsl:value-of select="$SystemOfRecord"/></SystemOfRecord>
				</xsl:if>

				<Version>
					<xsl:value-of select="$OriginalPolicy/@version"/>
				</Version>
				<Archived>
					<xsl:value-of select="$OriginalPolicy/@archived"/>
				</Archived>
				
				<xsl:if test="$DesiredVersion = '2.2'">
					<xsl:comment>Please note that the Representing elements are preserved here for backwards compatibility, and the AgentId, AgencyId and AgencyLocationId elements are preferred.</xsl:comment>

					<RepresentingAgent>
						<xsl:value-of select="$OriginalPolicy/@createUser"/>
					</RepresentingAgent>
	
					<RepresentingAgencyLocation>
						<xsl:value-of select="$OriginalPolicy/@locationId"/>
					</RepresentingAgencyLocation>
				</xsl:if>

				<AgentId>
					<xsl:value-of select="$OriginalPolicy/@createUser"/>
				</AgentId>

				<AgencyLocationId>
					<xsl:value-of select="$OriginalPolicy/@locationId"/>
				</AgencyLocationId>

				<xsl:copy-of select="$OriginalPolicy/Flags"/>
			</Management>

			<xsl:variable name="InsuredDataItems" select="$DataItemsFromOriginalPolicy[starts-with(@name, 'OpInsured') or starts-with(@name, 'Insured') or starts-with(@name, 'OpAdditionalInsured') or starts-with(@name, 'AdditionalInsured')]"/>
			<xsl:variable name="MortgageeDataItems" select="$DataItemsFromOriginalPolicy[starts-with(@name, 'OpMortgagee') or starts-with(@name, 'Mortgagee') or starts-with(@name, 'OpLoanNumber') or starts-with(@name, 'LoanNumber')]"/>
			<xsl:variable name="AdditionalInterestDataItems" select="$DataItemsFromOriginalPolicy[starts-with(@name, 'OpAdditionalInterest') or starts-with(@name, 'AdditionalInterest')]"/>

			<xsl:if test="$DesiredVersion = '2.2' or count($InsuredDataItems) + count($MortgageeDataItems) + count($AdditionalInterestDataItems) > 0">

				<Customers>
					
					<xsl:if test="count($InsuredDataItems) > 0">
						<Customer type="Insured">
							<xsl:for-each select="$InsuredDataItems">
								<xsl:sort select="@name"/>
		
								<DataItem name="{@name}">
									<xsl:attribute name="value">
										<xsl:choose>
											<xsl:when test="@value">
												<xsl:value-of select="@value"/>
											</xsl:when>
											<xsl:when test="@result">
												<xsl:value-of select="@result"/>
											</xsl:when>
										</xsl:choose>								
									</xsl:attribute>
								</DataItem>
							</xsl:for-each>
						</Customer>
					</xsl:if>
					
					<xsl:if test="count($MortgageeDataItems) > 0">
						<Customer type="Mortgagee">
							<xsl:for-each select="$MortgageeDataItems">
								<xsl:sort select="@name"/>
		
								<DataItem name="{@name}">
									<xsl:attribute name="value">
										<xsl:choose>
											<xsl:when test="@value">
												<xsl:value-of select="@value"/>
											</xsl:when>
											<xsl:when test="@result">
												<xsl:value-of select="@result"/>
											</xsl:when>
										</xsl:choose>								
									</xsl:attribute>
								</DataItem>
							</xsl:for-each>
						</Customer>
					</xsl:if>
					
					<xsl:if test="count($AdditionalInterestDataItems) > 0">
						<Customer type="AdditionalInterest">
							<xsl:for-each select="$AdditionalInterestDataItems">
								<xsl:sort select="@name"/>
		
								<DataItem name="{@name}">
									<xsl:attribute name="value">
										<xsl:choose>
											<xsl:when test="@value">
												<xsl:value-of select="@value"/>
											</xsl:when>
											<xsl:when test="@result">
												<xsl:value-of select="@result"/>
											</xsl:when>
										</xsl:choose>								
									</xsl:attribute>
								</DataItem>
							</xsl:for-each>
						</Customer>
					</xsl:if>
					
				</Customers>
				
			</xsl:if>

			<xsl:variable name="IncludeQuotingContents" select="number($OriginalPolicy/@percentageComplete) &lt; 1"/>
				
			<xsl:if test="$DesiredVersion = '2.2' or $IncludeQuotingContents">
			
				<Quoting>
	
					<xsl:if test="$IncludeQuotingContents">
	
						<CurrentQuote>
	
							<IssuedDate>
								<xsl:value-of select="$OriginalPolicy/Quote/@issueTimeStamp"/>
							</IssuedDate>
							<ExpirationDate>
								<xsl:value-of select="$OriginalPolicy/Quote/@expirationTimeStamp"/>
							</ExpirationDate>
	
							<DataItem name="percentageComplete" value="{$OriginalPolicy/@percentageComplete}"/>
	
							<ProtoTerm>
								<xsl:variable name="effectiveDate" select="$OriginalPolicy//EffectiveDateRange"/>
								<xsl:variable name="expirationDate" select="$OriginalPolicy//Calculation[@name='ExpirationDate']/@result"/>
	
								<xsl:variable name="effectiveDateFormatted">
									<xsl:choose>
										<xsl:when test="contains($effectiveDate, '/')">
											<xsl:call-template name="FormatDate">
												<xsl:with-param name="DateWithSlashes" select="$effectiveDate"/>
											</xsl:call-template>
										</xsl:when>
										<xsl:when test="not(contains($effectiveDate, 'T'))">
											<xsl:value-of select="concat($effectiveDate, 'T00:00:00', $TimeZoneOffset)"/>
										</xsl:when>
										<xsl:when test="not(string-length($effectiveDate) > 19)">
											<xsl:value-of select="concat($effectiveDate, $TimeZoneOffset)"/>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="$effectiveDate"/>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:variable>
	
								<xsl:variable name="expirationDateFormatted">
									<xsl:choose>
										<xsl:when test="$expirationDate">
											<xsl:choose>
												<xsl:when test="contains($expirationDate, '/')">
													<xsl:call-template name="FormatDate">
														<xsl:with-param name="DateWithSlashes" select="$expirationDate"/>
													</xsl:call-template>
												</xsl:when>
												<xsl:when test="not(contains($expirationDate, 'T'))">
													<xsl:value-of select="concat($expirationDate, 'T00:00:00', $TimeZoneOffset)"/>
												</xsl:when>
												<xsl:when test="not(string-length($expirationDate) > 19)">
													<xsl:value-of select="concat($expirationDate, $TimeZoneOffset)"/>
												</xsl:when>
												<xsl:otherwise>
													<xsl:value-of select="$expirationDate"/>
												</xsl:otherwise>
											</xsl:choose>
										</xsl:when>
										<xsl:otherwise/>
									</xsl:choose>
								</xsl:variable>
	
								<EffectiveDate>
									<xsl:value-of select="$effectiveDateFormatted"/>
								</EffectiveDate>
								
								<xsl:if test="string-length($expirationDateFormatted) > 0">
									<ExpirationDate>
										<xsl:value-of select="$expirationDateFormatted"/>
									</ExpirationDate>
								</xsl:if>
	
								<ProductRef idref="{$OriginalPolicy/@productId}" version="{$OriginalPolicy/@productVersionId}" href="products/{$OriginalPolicy/@productId}">
									<!-- This href needs to be pre-pended with a valid URL to the products server for it to be correct. -->
									<CachedItem name="Label" value="{$OriginalPolicy//basecriteria/Product}"/>
								</ProductRef>
	
								<ProtoInterval>
	
									<StartDate>
										<xsl:value-of select="$effectiveDateFormatted"/>
									</StartDate>
									<xsl:choose>
										<xsl:when test="string-length($expirationDateFormatted) > 0">
											<EndDate>
												<xsl:value-of select="$expirationDateFormatted"/>
											</EndDate>
										</xsl:when>
									</xsl:choose>
	
									<xsl:apply-templates select="$DataItemsFromOriginalPolicy">
										<xsl:sort select="@name"/>
									</xsl:apply-templates>
	
								</ProtoInterval>
	
							</ProtoTerm>
	
							<Rating>
								<DataItem name="ratingLastStatusChangeTimestamp" value="{$OriginalPolicy/@ratingLastStatusChangeTimestamp}"/>
								<DataItem name="ratingPriority" value="{$OriginalPolicy/@ratingPriority}"/>
								<DataItem name="ratingProfileId" value="{$OriginalPolicy/@ratingProfileId}"/>
								<DataItem name="ratingRetryCount" value="{$OriginalPolicy/@ratingRetryCount}"/>
								<DataItem name="ratingStatus" value="{$OriginalPolicy/@ratingStatus}"/>
							</Rating>
	
							<RulesetResults>
								<xsl:for-each select="$OriginalPolicy/Quote/Rulesets/Ruleset | $OriginalPolicy/Application/Rulesets/Ruleset">
									<RulesetResult rulesetId="{@uuid}" result="{@result}" trueResult="{@trueresult}" context="quote">
										<CachedItem name="rulesetName" value="{@name}"/>
										<CachedItem name="rulesetMessage" value="{@message}"/>
									</RulesetResult>
								</xsl:for-each>
							</RulesetResults>
	
							<ExtraData>
								<xsl:apply-templates select="$OriginalPolicy/ExtraData"/>
							</ExtraData>
	
							<Errors>
								<xsl:for-each select="$OriginalPolicy/Errors/Error">
									<Error>
										<xsl:copy-of select="Message"/>
										<xsl:for-each select="@*">
											<DataItem name="{local-name(.)}" value="{.}"/>
										</xsl:for-each>
									</Error>
								</xsl:for-each>
							</Errors>

							<xsl:choose>
								<xsl:when test="$DesiredVersion = '2.2'">
									<xsl:copy-of select="$OriginalPolicy/PolicyRequest"/>									
								</xsl:when>
								<xsl:otherwise>
									<!-- Use the included transformation -->
									<xsl:apply-templates select="$OriginalPolicy/PolicyRequest"/>
								</xsl:otherwise>
							</xsl:choose>
	
						</CurrentQuote>
	
					</xsl:if>
	
				</Quoting>
				
			</xsl:if>

			<xsl:variable name="effectiveDate" select="$OriginalPolicy//EffectiveDateRange"/>
			<xsl:variable name="expirationDate" select="$OriginalPolicy//Calculation[@name='ExpirationDate']/@result"/>
			
			<xsl:variable name="effectiveDateFormatted">
				<xsl:choose>
					<xsl:when test="contains($effectiveDate, '/')">
						<xsl:call-template name="FormatDate">
							<xsl:with-param name="DateWithSlashes" select="$effectiveDate"/>
						</xsl:call-template>
					</xsl:when>
					<xsl:when test="not(contains($effectiveDate, 'T'))">
						<xsl:value-of select="concat($effectiveDate, 'T00:00:00', $TimeZoneOffset)"/>
					</xsl:when>
					<xsl:when test="not(string-length($effectiveDate) > 19)">
						<xsl:value-of select="concat($effectiveDate, $TimeZoneOffset)"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$effectiveDate"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			
			<xsl:variable name="expirationDateFormatted">
				<xsl:choose>
					<xsl:when test="$expirationDate">
						<xsl:choose>
							<xsl:when test="contains($expirationDate, '/')">
								<xsl:call-template name="FormatDate">
									<xsl:with-param name="DateWithSlashes" select="$expirationDate"/>
								</xsl:call-template>
							</xsl:when>
							<xsl:when test="not(contains($expirationDate, 'T'))">
								<xsl:value-of select="concat($expirationDate, 'T00:00:00', $TimeZoneOffset)"/>
							</xsl:when>
							<xsl:when test="not(string-length($expirationDate) > 19)">
								<xsl:value-of select="concat($expirationDate, $TimeZoneOffset)"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$expirationDate"/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="concat(string(number(substring($effectiveDateFormatted, 1, 4)) + 1), substring($effectiveDateFormatted, 5))"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>

			<xsl:if test="$DesiredVariant = 'multiterm'">
							
				<Terms>
	
					<xsl:if test="number($OriginalPolicy/@percentageComplete) = 1">
	
						<Term>
							
							<EffectiveDate>
								<xsl:value-of select="$effectiveDateFormatted"/>
							</EffectiveDate>
	
							<xsl:if test="string-length($expirationDateFormatted) > 0">
								<ExpirationDate>
									<xsl:value-of select="$expirationDateFormatted"/>
								</ExpirationDate>
							</xsl:if>
	
							<ProductRef idref="{$OriginalPolicy/@productId}" version="{$OriginalPolicy/@productVersionId}" href="products/{$OriginalPolicy/@productId}">
								<xsl:comment>This href needs to be pre-pended with a valid URL to the products server for it to be correct.</xsl:comment>
								<CachedItem name="Label" value="{$OriginalPolicy//basecriteria/Product}"/>
							</ProductRef>
	
							<Intervals>
								<Interval>
									<StartDate>
										<xsl:value-of select="$effectiveDateFormatted"/>
									</StartDate>
									<xsl:choose>
										<xsl:when test="string-length($expirationDateFormatted) > 0">
											<EndDate>
												<xsl:value-of select="$expirationDateFormatted"/>
											</EndDate>
										</xsl:when>
									</xsl:choose>
	
									<xsl:apply-templates select="$DataItemsFromOriginalPolicy">
										<xsl:sort select="@name"/>
									</xsl:apply-templates>
	
								</Interval>
	
							</Intervals>
	
						</Term>
	
					</xsl:if>
	
				</Terms>
				
			</xsl:if>
			
			<xsl:if test="$DesiredVariant = 'singleterm'">
				<EffectiveDate>
					<xsl:value-of select="$effectiveDateFormatted"/>
				</EffectiveDate>
				
				<xsl:if test="string-length($expirationDateFormatted) > 0">
					<ExpirationDate>
						<xsl:value-of select="$expirationDateFormatted"/>
					</ExpirationDate>
				</xsl:if>
				
				<ProductRef idref="{$OriginalPolicy/@productId}" version="{$OriginalPolicy/@productVersionId}" href="products/{$OriginalPolicy/@productId}">
					<xsl:comment>This href needs to be pre-pended with a valid URL to the products server for it to be correct.</xsl:comment>
					<CachedItem name="Label" value="{$OriginalPolicy//basecriteria/Product}"/>
				</ProductRef>
				
						
				<xsl:apply-templates select="$DataItemsFromOriginalPolicy">
					<xsl:sort select="@name"/>
				</xsl:apply-templates>
			</xsl:if>

			<xsl:if test="$DesiredVersion = '2.2'">
				<!-- These were never supported by 1.x, and 2.3 makes them optional. -->
				<Accounting>
					<PaymentPlan/>
					<Payor/>
					<Ledger/>
				</Accounting>
				<Meta/>
				<RelatedItems>
					<Notes/>
					<Attachments/>
					<Tasks/>
				</RelatedItems>
			</xsl:if>

			<Documents>

				<xsl:for-each select="$OriginalPolicy/AvailableDocuments/AvailableDocument">

					<xsl:variable name="documentName" select="@templateid"/>
					<xsl:variable name="documentLabel" select="@label"/>

					<Reference type="document" id="{$documentName}" location="documents/{$documentName}">
						
						<!-- Work around the pxServer datetime bug -->
						<xsl:variable name="lastUpdatedFromPxServer">
							<xsl:choose>
								<xsl:when test="$OriginalPolicy/@updateTimestamp">
									<xsl:value-of select="$OriginalPolicy/@updateTimestamp"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$OriginalPolicy/@createTimestamp"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
						
						<xsl:attribute name="lastUpdated">
							<xsl:choose>
								<xsl:when test="substring(string($lastUpdatedFromPxServer), string-length(string($lastUpdatedFromPxServer))) = 'Z'">
									<xsl:value-of select="$lastUpdatedFromPxServer"/>
								</xsl:when>
								<xsl:otherwise><xsl:value-of select="$lastUpdatedFromPxServer"/>Z</xsl:otherwise>
							</xsl:choose>
						</xsl:attribute>
						
						<CachedItem name="label" value="{$documentLabel}"/>
						<CachedItem name="templateID" value="{$documentName}"/>
					</Reference>

				</xsl:for-each>

				<!-- Some filters sometimes inject Document references into the ExtraData element as nested Data elements. -->
				<xsl:for-each select="$OriginalPolicy/ExtraData/Data[@name='references']/Data[@name='documents']/Data">
					<Reference type="document" id="{Data[@name='id']/@value}" location="{Data[@name='location']/@value}">
						<CachedItem name="label" value="{Data[@name='label']/@value}"/>
					</Reference>
				</xsl:for-each>
				
			</Documents>
			
			<xsl:if test="$DesiredVersion = '2.2'">
				<!-- These were never supported by 1.x, and 2.3 makes them optional. -->
				<Claims/>
			</xsl:if>
			
			<xsl:variable name="RiskAnalysisReferencesExist" select="exists($OriginalPolicy/ExtraData/Data[@name='references']/Data[@name='riskAnalyses']/Data)"/>
			
			<xsl:if test="$DesiredVersion = '2.2' or $RiskAnalysisReferencesExist">
				<!-- When it runs, the Risk Analysis filter injects references to Risk Analyses into the ExtraData element as nested Data elements. -->
				<RiskAnalyses>
					<xsl:for-each select="$OriginalPolicy/ExtraData/Data[@name='references']/Data[@name='riskAnalyses']/Data">
						<Reference type="RiskAnalysis" id="{Data[@name='id']/@value}" location="{Data[@name='location']/@value}">
							<CachedItem name="AnalysisPackageId" value="{Data[@name='AnalysisPackageId']/@value}"/>
							<CachedItem name="ScoringScenarioId" value="{Data[@name='ScoringScenarioId']/@value}"/>
							<CachedItem name="AnalysisRunDate" value="{Data[@name='AnalysisRunDate']/@value}"/>
							<CachedItem name="AnalysisRunUser" value="{Data[@name='AnalysisRunUser']/@value}"/>
						</Reference>
					</xsl:for-each>
				</RiskAnalyses>
			</xsl:if>
			
			<xsl:if test="$DesiredVersion = '2.2'">
				<!-- These were never supported by 1.x, and 2.3 makes them optional. -->
				<Inspections/>
				<Profiles/>
				<WorkflowJobs/>
				<OtherReferences/>
			</xsl:if>

			<History>

				<xsl:variable name="producerCode" select="$OriginalPolicy/@producerCode"/>

				<xsl:for-each select="$OriginalPolicy/History/Event[@type != 'Payment']">
					
					<xsl:sort select="@timeStamp"/>
					
					<Event type="{@type}">
						<xsl:attribute name="timeStamp">
							<xsl:choose>
								<xsl:when test="substring(string(@timeStamp), string-length(string(@timeStamp))) = 'Z'">
									<xsl:value-of select="concat(substring(@timeStamp, 1, string-length(@timeStamp)-1), $TimeZoneOffset)"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="concat(@timeStamp, $TimeZoneOffset)"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:attribute>
						<Initiator type="user">
							<xsl:value-of select="@userName"/>
						</Initiator>
						<Handler type="system">pxserver</Handler>

						<xsl:if test="string-length(@description) > 0">
							<Description>
								<xsl:value-of select="@description"/>
							</Description>
						</xsl:if>

						<xsl:if test="string-length($producerCode) > 0">
							<DataItem name="producerCode" value="{$producerCode}"/>
						</xsl:if>
						
						<xsl:choose>
							<xsl:when test="@percentageComplete">
								<DataItem name="percentageComplete" value="{@percentageComplete}"/>
							</xsl:when>
						</xsl:choose>
					</Event>
				</xsl:for-each>
				
				<xsl:for-each select="$OriginalPolicy/PaymentTransactions/payment">					
					<Event type="Payment">
						
						<xsl:attribute name="timeStamp">
							<xsl:choose>
								<xsl:when test="substring(string(@datetime), string-length(string(@datetime))) = 'Z'">
									<xsl:value-of select="concat(substring(@datetime, 1, string-length(@datetime)-1), $TimeZoneOffset)"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="concat(@datetime, $TimeZoneOffset)"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:attribute>
						
						<Initiator type="user">
							<xsl:value-of select="$OriginalPolicy/PolicyRequest/@username"/>
						</Initiator>
						<Handler type="system">pxPay</Handler>
						
						<DataItem name="AmountPaid" value="{@amount}"/>
						<DataItem name="PaymentMethod" value="{@method}"/>
						
						<xsl:choose>
							<xsl:when test="@method = 'check'">
								<DataItem name="PayorName" value="{checkinfo/@nameoncheck}"/>
								<DataItem name="CheckNumber" value="{checkinfo/@checknumber}"/>		
							</xsl:when>
							
							<xsl:when test="@method = 'creditcard'">
								<DataItem name="PayorName" value="{creditcardinfo/@cardholdername}"/>
								<DataItem name="CardType" value="{creditcardinfo/@cardtype}"/>
								<DataItem name="CardExpirationMonth" value="{creditcardinfo/@expirationmonth}"/>
								<DataItem name="CardExpirationYear" value="{creditcardinfo/@expirationyear}"/>
								<DataItem name="CardNumberMask" value="{creditcardinfo/@cardnumbermask}"/>	
								
								<xsl:if test="creditcardinfo/processorresponse/data/@transactionResult">
									<DataItem name="TransactionId" value="{creditcardinfo/processorresponse/data/@transactionResult}"/>
								</xsl:if>
							</xsl:when>
							
							<xsl:when test="@method = 'eft'">
								<DataItem name="PayorName" value="{eftinfo/@accountholderfullname}"/>
								<DataItem name="PayorLicenseNumber" value="{eftinfo/@accountholderlicensenumber}"/>
								<DataItem name="PayorLicenseState" value="{eftinfo/@accountholderlicensestate}"/>
								<DataItem name="RoutingNumber" value="{eftinfo/@routingnumber}"/>
								<DataItem name="AccountNumber" value="{eftinfo/@accountnumber}"/>
								
								<xsl:if test="eftinfo/processorresponse/data/@transactionResult">
									<DataItem name="TransactionId" value="{eftinfo/processorresponse/data/@transactionResult}"/>
								</xsl:if>
							</xsl:when>
						</xsl:choose>
						
					</Event>
				</xsl:for-each>

			</History>

		</InsurancePolicy>

	</xsl:template>

	<xsl:template match="Data">
		<xsl:choose>
			<xsl:when test="@value">
				<DataItem name="{@name}" value="{@value}"/>
			</xsl:when>
			<xsl:otherwise>
				<DataItem name="{@name}">
					<xsl:apply-templates/>
				</DataItem>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="Input|Calculation|DataItem">

		<xsl:choose>
			<xsl:when test="not(starts-with(@name, 'Insured') or starts-with(@name, 'AdditionalInsured') or starts-with(@name, 'Mortgagee') or starts-with(@name, 'AdditionalInterest'))">

				<xsl:variable name="value">
					<xsl:choose>
						<xsl:when test="@value">
							<xsl:value-of select="@value"/>
						</xsl:when>
						<xsl:when test="@result">
							<xsl:value-of select="@result"/>
						</xsl:when>
					</xsl:choose>
				</xsl:variable>

				<DataItem name="{@name}" value="{$value}"/>

			</xsl:when>
		</xsl:choose>

	</xsl:template>

	<xsl:template name="FormatDate">

		<xsl:param name="DateWithSlashes"/>

		<xsl:variable name="mo">
			<xsl:value-of select="substring-before($DateWithSlashes, '/')"/>
		</xsl:variable>
		<xsl:variable name="day-temp">
			<xsl:value-of select="substring-after($DateWithSlashes, '/')"/>
		</xsl:variable>
		<xsl:variable name="day">
			<xsl:value-of select="substring-before($day-temp, '/')"/>
		</xsl:variable>
		<xsl:variable name="year-temp">
			<xsl:value-of select="substring-after($day-temp, '/')"/>
		</xsl:variable>
		<xsl:variable name="year">
			<xsl:value-of select="substring($year-temp, 1, 4)"/>
		</xsl:variable>
		<xsl:variable name="time">
			<xsl:value-of select="'00:00:00'"/>
		</xsl:variable>
		<xsl:variable name="hh">
			<xsl:value-of select="substring($time, 1, 2)"/>
		</xsl:variable>
		<xsl:variable name="mm">
			<xsl:value-of select="substring($time, 4, 2)"/>
		</xsl:variable>
		<xsl:variable name="ss">
			<xsl:value-of select="substring($time, 7, 2)"/>
		</xsl:variable>

		<xsl:value-of select="$year"/>
		<xsl:value-of select="'-'"/>
		<xsl:if test="(string-length($mo) &lt; 2)">
			<xsl:value-of select="0"/>
		</xsl:if>
		<xsl:value-of select="$mo"/>
		<xsl:value-of select="'-'"/>
		<xsl:if test="(string-length($day) &lt; 2)">
			<xsl:value-of select="0"/>
		</xsl:if>
		<xsl:value-of select="$day"/>
		<xsl:value-of select="'T'"/>
		<xsl:value-of select="$hh"/>
		<xsl:value-of select="':'"/>
		<xsl:value-of select="$mm"/>
		<xsl:value-of select="':'"/>
		<xsl:value-of select="$ss"/>
		<xsl:value-of select="$TimeZoneOffset"/>
	</xsl:template>

</xsl:stylesheet>
