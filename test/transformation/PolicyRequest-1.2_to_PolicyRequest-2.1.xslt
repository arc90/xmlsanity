<?xml version="1.0" encoding="UTF-8"?>
<!--
	~~~ This document is ©2009 Insight Catastrophe Group. ~~~
	~~~ ALL	RIGHTS RESERVED. DO NOT DISTRIBUTE WITHOUT WRITTEN AUTHORIZATION. ~~~
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
    
	<xsl:template match="PolicyRequest">
        
        <PolicyRequest schemaVersion="2.1">
        	<UserName><xsl:value-of select="@username"/></UserName>
            <LocationId><xsl:value-of select="@locationid"/></LocationId>
        	<SubmitTimestamp><xsl:value-of select="current-dateTime()"/></SubmitTimestamp>
        	
        	<PolicyState>
            	<xsl:choose>
            		<xsl:when test="number(@percentagecomplete) = 1">
            			<xsl:text>ACTIVEPOLICY</xsl:text>
            		</xsl:when>
            		<xsl:otherwise>
            			<xsl:text>ACTIVEQUOTE</xsl:text>
            		</xsl:otherwise>
            	</xsl:choose>
            </PolicyState>

        	<ProducerCode><xsl:value-of select="@producercode"/></ProducerCode>
            <UserAgent><xsl:value-of select="@useragent"/></UserAgent>
        	
            <BaseCriteria>
                <xsl:for-each select="basecriteria/*">
                	<BaseCriterion name="{name()}" value="{text()}"/>
                </xsl:for-each>
            </BaseCriteria>

        	<xsl:if test="flags">
                <Flags>
                    <xsl:for-each select="flags/flag">
                        <Flag>
                            <xsl:copy-of select="@*"/>
                        </Flag>
                    </xsl:for-each>
                </Flags>
            </xsl:if>
            
            <!-- TODO: We should really sort all the DataItems after creating them. -->
            
        	<xsl:for-each select="quoteform/inputs/input">
        		<DataItem name="{@name}" value="{@value}" type="quote"/>
            </xsl:for-each>
            
        	<xsl:for-each select="appform/inputs/input">
        		<DataItem name="{@name}" value="{@value}" type="app"/>
        	</xsl:for-each>

        	<xsl:for-each select="Underwriting/DataItem">
        		<DataItem name="{@name}" value="{@value}" type="underwriting"/>
        	</xsl:for-each>

			<xsl:if test="payments/policyPaymentRequest">
				
				<PaymentRequests>
					
					<xsl:for-each select="payments/policyPaymentRequest">
			
						<PaymentRequest amount="{normalize-space(payment/creditcard/value[@name='amt']/text())}" method="creditcard" policyID="{@policyID}" business="{@business}">
							
							<CreditCardInfo
								cardholderFirstName="{normalize-space(payment/customer/value[@name='firstname']/text())}"
								cardholderLastName="{normalize-space(payment/customer/value[@name='lastname']/text())}"
								cardType="{normalize-space(paymentCardType/text())}"
								cardNumber="{normalize-space(payment/creditcard/value[@name='acct']/text())}"
								expirationMonth="{normalize-space(payment/creditcard/value[@name='expmon']/text())}"
								expirationYear="{normalize-space(payment/creditcard/value[@name='expyear']/text())}"
								verificationNumber="{normalize-space(payment/creditcard/value[@name='cvv2']/text())}">
								
								<CardBillingAddress>
									<xsl:attribute name="streetLineOne">
										<xsl:value-of select="normalize-space(payment/customer/value[@name='street']/text())"/>
									</xsl:attribute>
									
									<xsl:attribute name="postalCode">
										<xsl:value-of select="normalize-space(payment/customer/value[@name='zip']/text())"/>
									</xsl:attribute>
								</CardBillingAddress>
								
							</CreditCardInfo>	
							
						</PaymentRequest>
			
					</xsl:for-each>
					
				</PaymentRequests>
			
			</xsl:if>

        </PolicyRequest>
        
	</xsl:template>

</xsl:stylesheet>