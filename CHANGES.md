# ChangeLog

## v3.0.0

* Bugs
    * [BFJ-94](https://jira.ralph-schuster.eu/browse/BFJ-94) - Fix CoverityScan findings
* New Features
    * [BFJ-92](https://jira.ralph-schuster.eu/browse/BFJ-92) - Upgrade all date and times to RsDate instances
* Other
    * [BFJ-93](https://jira.ralph-schuster.eu/browse/BFJ-93) - Remove warnings due to Java update

## v2.0.3
* Bugs
    * [BFJ-86](https://jira.ralph-schuster.eu/browse/BFJ-86) - JiraRpcSession does not allow inheritance
    * [BFJ-87](https://jira.ralph-schuster.eu/browse/BFJ-87) - BugzillaHttpSession does not allow inheritance
    * [BFJ-88](https://jira.ralph-schuster.eu/browse/BFJ-88) - BugzillaRpcSession does not allow inheritance
    * [BFJ-89](https://jira.ralph-schuster.eu/browse/BFJ-89) - AbstractLazyRetriever uses Long.getLong() instead of LangUtils.getLong()
    * [BFJ-90](https://jira.ralph-schuster.eu/browse/BFJ-90) - Untrusted Authentication Request
* New Features
    * [BFJ-91](https://jira.ralph-schuster.eu/browse/BFJ-91) - Add login token usage for RPC usage
* Other
    * [BFJ-85](https://jira.ralph-schuster.eu/browse/BFJ-85) - Overcome Jira's search result limitation
    
## v2.0.2
* Bugs
    * [BFJ-84](https://jira.ralph-schuster.eu/browse/BFJ-84) - NPE in JiraRpcSession.createIssue()
* Others
    * [BFJ-82](https://jira.ralph-schuster.eu/browse/BFJ-82) - Update SVN Repository and Doc Server information
    
## v2.0.1

* Bugs
    * [BFJ-33](https://jira.ralph-schuster.eu/browse/BFJ-33) - BugzillaHttpSession still uses old proxy configuration
    * [BFJ-34](https://jira.ralph-schuster.eu/browse/BFJ-34) - Logically dead code
    * [BFJ-35](https://jira.ralph-schuster.eu/browse/BFJ-35) - Dereference after null check
    * [BFJ-36](https://jira.ralph-schuster.eu/browse/BFJ-36) - Dereference after null check
    * [BFJ-37](https://jira.ralph-schuster.eu/browse/BFJ-37) - Dereference null return value
    * [BFJ-38](https://jira.ralph-schuster.eu/browse/BFJ-38) - Dereference null return value
    * [BFJ-39](https://jira.ralph-schuster.eu/browse/BFJ-39) - Dereference null return value
    * [BFJ-40](https://jira.ralph-schuster.eu/browse/BFJ-40) - Dereference null return value
    * [BFJ-41](https://jira.ralph-schuster.eu/browse/BFJ-41) - Dereference null return value
    * [BFJ-42](https://jira.ralph-schuster.eu/browse/BFJ-42) - Dereference null return value
    * [BFJ-43](https://jira.ralph-schuster.eu/browse/BFJ-43) - Resource leak on an exceptional path
    * [BFJ-44](https://jira.ralph-schuster.eu/browse/BFJ-44) - Dm: Dubious method used
    * [BFJ-45](https://jira.ralph-schuster.eu/browse/BFJ-45) - Dm: Dubious method used
    * [BFJ-46](https://jira.ralph-schuster.eu/browse/BFJ-46) - Dm: Dubious method used
    * [BFJ-47](https://jira.ralph-schuster.eu/browse/BFJ-47) - WMI: Inefficient Map Iterator
    * [BFJ-48](https://jira.ralph-schuster.eu/browse/BFJ-48) - WMI: Inefficient Map Iterator
    * [BFJ-49](https://jira.ralph-schuster.eu/browse/BFJ-49) - Dm: Dubious method used
    * [BFJ-50](https://jira.ralph-schuster.eu/browse/BFJ-50) - Dm: Dubious method used
    * [BFJ-51](https://jira.ralph-schuster.eu/browse/BFJ-51) - LI: Unsynchronized Lazy Initialization
    * [BFJ-52](https://jira.ralph-schuster.eu/browse/BFJ-52) - Dm: Dubious method used
    * [BFJ-53](https://jira.ralph-schuster.eu/browse/BFJ-53) - Dm: Dubious method used
    * [BFJ-54](https://jira.ralph-schuster.eu/browse/BFJ-54) - USELESS_STRING: Useless/non-informative string generated
    * [BFJ-55](https://jira.ralph-schuster.eu/browse/BFJ-55) - Dm: Dubious method used
    * [BFJ-56](https://jira.ralph-schuster.eu/browse/BFJ-56) - USELESS_STRING: Useless/non-informative string generated
    * [BFJ-57](https://jira.ralph-schuster.eu/browse/BFJ-57) - Dm: Dubious method used
    * [BFJ-58](https://jira.ralph-schuster.eu/browse/BFJ-58) - USELESS_STRING: Useless/non-informative string generated
    * [BFJ-59](https://jira.ralph-schuster.eu/browse/BFJ-59) - Dm: Dubious method used
    * [BFJ-60](https://jira.ralph-schuster.eu/browse/BFJ-60) - USELESS_STRING: Useless/non-informative string generated
    * [BFJ-61](https://jira.ralph-schuster.eu/browse/BFJ-61) - Dm: Dubious method used
    * [BFJ-62](https://jira.ralph-schuster.eu/browse/BFJ-62) - Dm: Dubious method used
    * [BFJ-63](https://jira.ralph-schuster.eu/browse/BFJ-63) - Dm: Dubious method used
    * [BFJ-64](https://jira.ralph-schuster.eu/browse/BFJ-64) - Dm: Dubious method used
    * [BFJ-66](https://jira.ralph-schuster.eu/browse/BFJ-66) - SIC: Inner class could be made static
    * [BFJ-67](https://jira.ralph-schuster.eu/browse/BFJ-67) - HE: Equal objects must have equal hashcodes
    * [BFJ-68](https://jira.ralph-schuster.eu/browse/BFJ-68) - STCAL: Static use of type Calendar or DateFormat
    * [BFJ-69](https://jira.ralph-schuster.eu/browse/BFJ-69) - Dm: Dubious method used
    * [BFJ-70](https://jira.ralph-schuster.eu/browse/BFJ-70) - SBSC: String concatenation in loop using + operator
    * [BFJ-71](https://jira.ralph-schuster.eu/browse/BFJ-71) - SIC: Inner class could be made static
    * [BFJ-72](https://jira.ralph-schuster.eu/browse/BFJ-72) - Dm: Dubious method used
    * [BFJ-73](https://jira.ralph-schuster.eu/browse/BFJ-73) - STCAL: Static use of type Calendar or DateFormat
    * [BFJ-74](https://jira.ralph-schuster.eu/browse/BFJ-74) - STCAL: Static use of type Calendar or DateFormat
    * [BFJ-75](https://jira.ralph-schuster.eu/browse/BFJ-75) - SBSC: String concatenation in loop using + operator
    * [BFJ-76](https://jira.ralph-schuster.eu/browse/BFJ-76) - STCAL: Static use of type Calendar or DateFormat
    * [BFJ-77](https://jira.ralph-schuster.eu/browse/BFJ-77) - DLS: Dead local store
    * [BFJ-78](https://jira.ralph-schuster.eu/browse/BFJ-78) - Dm: Dubious method used
    * [BFJ-79](https://jira.ralph-schuster.eu/browse/BFJ-79) - WMI: Inefficient Map Iterator
    * [BFJ-80](https://jira.ralph-schuster.eu/browse/BFJ-80) - WMI: Inefficient Map Iterator
* Improvements
    * [BFJ-81](https://jira.ralph-schuster.eu/browse/BFJ-81) - Allow default AuthorizationCallback definition

## v2.0.0
* Bugs
    * [BFJ-4](https://jira.ralph-schuster.eu/browse/BFJ-4) - JIRA session
    * [BFJ-27](https://jira.ralph-schuster.eu/browse/BFJ-27) - NPE in AbstractAuthorizedSession#createIssue
* New Features
    * [BFJ-21](https://jira.ralph-schuster.eu/browse/BFJ-21) - Provide as OSGI bundle
* Improvements
    * [BFJ-19](https://jira.ralph-schuster.eu/browse/BFJ-19) - UTF-8 Cleanup of XML response
    * [BFJ-20](https://jira.ralph-schuster.eu/browse/BFJ-20) - Support build date in build.properties
    * [BFJ-22](https://jira.ralph-schuster.eu/browse/BFJ-22) - Use CSV/Excel Utitlity
    * [BFJ-23](https://jira.ralph-schuster.eu/browse/BFJ-23) - Use Configurable interface
    * [BFJ-24](https://jira.ralph-schuster.eu/browse/BFJ-24) - Re-implement BugzillaSession for XML-RPC (JSON) usage
    * [BFJ-28](https://jira.ralph-schuster.eu/browse/BFJ-28) - Add fields for author name, assignee name and reporter name
    * [BFJ-32](https://jira.ralph-schuster.eu/browse/BFJ-32) - Allow local test configuration
* Others
    * [BFJ-29](https://jira.ralph-schuster.eu/browse/BFJ-29) - Refactor Custom Fields
    * [BFJ-30](https://jira.ralph-schuster.eu/browse/BFJ-30) - hashcode() and equals() methods
    * [BFJ-31](https://jira.ralph-schuster.eu/browse/BFJ-31) - Resolve loading of associated components

## v1.4.0
* Bugs
    * [BFJ-16](https://jira.ralph-schuster.eu/browse/BFJ-16) - No error when Bugzilla version does not match
    * [BFJ-18](https://jira.ralph-schuster.eu/browse/BFJ-18) - ClassCastException: XMLConfiguration cannot be casted to SubnodeConfiguration
* Improvements
    * [BFJ-13](https://jira.ralph-schuster.eu/browse/BFJ-13) - Add useful comments in BugzillaUtils
    * [BFJ-14](https://jira.ralph-schuster.eu/browse/BFJ-14) - Check occurance of SubnodeConfiguration
    * [BFJ-15](https://jira.ralph-schuster.eu/browse/BFJ-15) - Remove Non-Javadoc
    * [BFJ-17](https://jira.ralph-schuster.eu/browse/BFJ-17) - Base on baselib
* Other
    * [BFJ-12](https://jira.ralph-schuster.eu/browse/BFJ-12) - Update POM for JIRA

## v1.3
* Bugs
    * [BFJ-6](https://jira.ralph-schuster.eu/browse/BFJ-6) - Filter searches on HttpJiraSession does not return when no issue was found
    * [BFJ-9](https://jira.ralph-schuster.eu/browse/BFJ-9) - Attachment filename not set
    * [BFJ-10](https://jira.ralph-schuster.eu/browse/BFJ-10) - Timestamps are not parse correctly
* Improvements
    * [BFJ-11](https://jira.ralph-schuster.eu/browse/BFJ-11) - Add saving of attachments

## v1.1
* Bugs
    * [BFJ-2](https://jira.ralph-schuster.eu/browse/BFJ-2) - java.lang.IllegalArgumentException: Passed in key must select exactly one node: ProxyAuthorizationCallback(0)
    * [BFJ-5](https://jira.ralph-schuster.eu/browse/BFJ-5) - Writing # as first column does not escape it
    * [BFJ-7](https://jira.ralph-schuster.eu/browse/BFJ-7) - Bugzilla 4.2 uses attachments in long descriptions
    * [BFJ-8](https://jira.ralph-schuster.eu/browse/BFJ-8) - Java 6 doesn't know HttpOnly Cookie attribute
* Improvements
    * [BFJ-1](https://jira.ralph-schuster.eu/browse/BFJ-1) - Create abstract class for BugzillaReportGenerator
    * [BFJ-3](https://jira.ralph-schuster.eu/browse/BFJ-3) - Add abstract Email Report class
