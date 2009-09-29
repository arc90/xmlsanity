﻿//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated by a tool.
//     Runtime Version:2.0.50727.4016
//
//     Changes to this file may cause incorrect behavior and will be lost if
//     the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

namespace XmlSanity {
    using System;
    
    
    /// <summary>
    ///   A strongly-typed resource class, for looking up localized strings, etc.
    /// </summary>
    // This class was auto-generated by the StronglyTypedResourceBuilder
    // class via a tool like ResGen or Visual Studio.
    // To add or remove a member, edit your .ResX file then rerun ResGen
    // with the /str option, or rebuild your VS project.
    [global::System.CodeDom.Compiler.GeneratedCodeAttribute("System.Resources.Tools.StronglyTypedResourceBuilder", "2.0.0.0")]
    [global::System.Diagnostics.DebuggerNonUserCodeAttribute()]
    [global::System.Runtime.CompilerServices.CompilerGeneratedAttribute()]
    internal class Schematron {
        
        private static global::System.Resources.ResourceManager resourceMan;
        
        private static global::System.Globalization.CultureInfo resourceCulture;
        
        [global::System.Diagnostics.CodeAnalysis.SuppressMessageAttribute("Microsoft.Performance", "CA1811:AvoidUncalledPrivateCode")]
        internal Schematron() {
        }
        
        /// <summary>
        ///   Returns the cached ResourceManager instance used by this class.
        /// </summary>
        [global::System.ComponentModel.EditorBrowsableAttribute(global::System.ComponentModel.EditorBrowsableState.Advanced)]
        internal static global::System.Resources.ResourceManager ResourceManager {
            get {
                if (object.ReferenceEquals(resourceMan, null)) {
                    global::System.Resources.ResourceManager temp = new global::System.Resources.ResourceManager("XmlSanity.Schematron", typeof(Schematron).Assembly);
                    resourceMan = temp;
                }
                return resourceMan;
            }
        }
        
        /// <summary>
        ///   Overrides the current thread's CurrentUICulture property for all
        ///   resource lookups using this strongly typed resource class.
        /// </summary>
        [global::System.ComponentModel.EditorBrowsableAttribute(global::System.ComponentModel.EditorBrowsableState.Advanced)]
        internal static global::System.Globalization.CultureInfo Culture {
            get {
                return resourceCulture;
            }
            set {
                resourceCulture = value;
            }
        }
        
        /// <summary>
        ///   Looks up a localized string similar to &lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot;?&gt;&lt;?xar XSLT?&gt;
        ///
        ///&lt;!-- 
        ///     OVERVIEW - iso_abstract_expand.xsl
        ///     
        ///	    This is a preprocessor for ISO Schematron, which implements abstract patterns. 
        ///	    It also 
        ///	       	* extracts a particular schema using an ID, where there are multiple 
        ///	    schemas, such as when they are embedded in the same NVDL script 
        ///	    	* experimentally, allows parameter recognition and substitution inside
        ///	    	text as well as @context, @test, &amp; @select.
        ///		
        ///		
        ///		This should  [rest of string was truncated]&quot;;.
        /// </summary>
        internal static string schematron_xslt_abstracts {
            get {
                return ResourceManager.GetString("schematron_xslt_abstracts", resourceCulture);
            }
        }
        
        /// <summary>
        ///   Looks up a localized string similar to &lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot;?&gt;&lt;?xar XSLT?&gt;
        ///
        ///&lt;!-- 
        ///     OVERVIEW : iso_dsdl_include.xsl
        ///     
        ///	    This is an inclusion preprocessor for the non-smart text inclusions
        ///	    of ISO DSDL. It handles 
        ///	    	&lt;relax:extRef&gt; for ISO RELAX NG
        ///	    	&lt;sch:include&gt;  for ISO Schematron and Schematron 1.n
        ///	    	&lt;sch:extends&gt;  for 2009 draft ISO Schematron
        ///	    	&lt;xi:xinclude&gt;  simple W3C XIncludes for ISO NVRL and DSRL 
        ///	    	&lt;crdl:ref&gt;     for draft ISO CRDL
        ///	    	&lt;dtll:include&gt; for draft ISO DTLL
        ///	  [rest of string was truncated]&quot;;.
        /// </summary>
        internal static string schematron_xslt_includes {
            get {
                return ResourceManager.GetString("schematron_xslt_includes", resourceCulture);
            }
        }
        
        /// <summary>
        ///   Looks up a localized string similar to &lt;?xml version=&quot;1.0&quot; ?&gt;
        ///&lt;!-- 
        ///   ISO_SVRL.xsl   
        ///
        ///   Implementation of Schematron Validation Report Language from ISO Schematron
        ///   ISO/IEC 19757 Document Schema Definition Languages (DSDL) 
        ///     Part 3: Rule-based validation  Schematron 
        ///     Annex D: Schematron Validation Report Language 
        ///
        ///  This ISO Standard is available free as a Publicly Available Specification in PDF from ISO.
        ///  Also see www.schematron.com for drafts and other information.
        ///
        ///  This implementation of SVRL is designed to run with the &quot;Ske [rest of string was truncated]&quot;;.
        /// </summary>
        internal static string schematron_xslt_svrl {
            get {
                return ResourceManager.GetString("schematron_xslt_svrl", resourceCulture);
            }
        }
    }
}