# User's Instructions

## Generating Reports

_Bugzilla for Java_ was written to generate reports out of Issue Mmanagement systems such as Bugzilla and Jira. The focus of development was as much modularization as possible so you can plugin your own Management System, write your own reports generators and extend the systems according to your special needs.

That’s why the configuration for running a report is a major topic in B4J. The section Configuration explains the details of this task.

[Example report configurations can be found here.](https://github.com/technicalguru/b4j/tree/b4j-3.0.0/site/examples)

### Running a Report

It’s pretty simple to run it then:

```
java -cp $b4jlibs b4j.GenerateReports --xml-file report-config.xml
```

The system will answer with an output like:

```
2013-06-22 06:33:16,612 [INFO ] b4j.GenerateReports - GenerateReports started
2013-06-22 06:33:18,207 [INFO ] b4j.core.session.BugzillaHttpSession - Session opened: http://your.bugzilla.home
2013-06-22 06:33:21,579 [INFO ] b4j.core.session.BugzillaHttpSession - Session closed
2013-06-22 06:33:21,580 [INFO ] b4j.GenerateReports - GenerateReports finished
```

You will find the generated reports at the locations that you defined in your configuration file. There are abstract report configurations that can send the report via e-mail to your mailbox. The common reports will write their result as a file to your disk.

__Notice:__ The generated reports are listed in the default output since version 2.0.0.

### Output Details

The level of details can be controlled through `log4j.properties` which must be present in your classpath. B4J delivers a [default configuration](https://github.com/technicalguru/b4j/blob/b4j-3.0.0/src/main/resources/log4j.properties) which will show `INFO` and `ERROR` level only. You can provide your own `log4j.properties` file, but make sure it appears in the classpath before the B4J libraries.

## Configuration

The configuration is separated into four sections:

1. The location of your Issue Management System (Jira or Bugzilla)
1. The authentication information
1. The selection of bugs to be included in your report
1. The reports that you want to generate

Once you configured your report, you are able to generate them fast.

### Location of Bugzilla/Jira

The location configuration requires two aspects: the type of communication that B4J shall apply (session type) and the net address of your Issue Management system is available at.

The type of communication is selected through the Session class that you configure:

* [BugzillaHttpSession](https://download.ralph-schuster.eu/eu.ralph-schuster.b4j/3.0.0/apidocs/b4j/core/session/BugzillaHttpSession.html) will use plain HTTP(S) protocol to talk to your Bugzilla instance
* [BugzillaRpcSession](https://download.ralph-schuster.eu/eu.ralph-schuster.b4j/3.0.0/apidocs/b4j/core/session/BugzillaRpcSession.html) will use XML-RPC/JSON protocol – via HTTP(S) – to talk to your Bugzilla instance
* [JiraRpcSession](https://download.ralph-schuster.eu/eu.ralph-schuster.b4j/3.0.0/apidocs/b4j/core/session/JiraRpcSession.html) uses XML-RPC cmmunication – via HTTP(S) – to access your Jira instance (No proxy support yet)

The Session is configured by the `<bugzilla-session>` tag:

```
<?xml version="1.0" encoding="ISO-8859-1"?>
<bugzilla-report>
   <bugzilla-session class="b4j.core.session.BugzillaHttpSession">
      ...
   </bugzilla-session>
   ...
</bugzilla-report>
```

The various Session classes can be found in package [b4j.core.session](https://download.ralph-schuster.eu/eu.ralph-schuster.b4j/3.0.0/apidocs/b4j/core/session/package-summary.html).

The network location of your system is part of the session configuration:

```
<?xml version="1.0" encoding="ISO-8859-1"?>
<bugzilla-report>
   <bugzilla-session class="b4j.core.session.BugzillaHttpSession">
      <bugzilla-home>http://your-bugzilla.your-domain.com/</bugzilla-home>
      ...
   </bugzilla-session>
   ...
</bugzilla-report>
```

Detailed information about which tags are required within your `<bugzilla-session>` tag can be found in the Javadoc of your selected session class.

### Authentication Information

Most Bugzilla and Jira instances are protected and can be accessed with valid user credentials only. B4J provides a flexible but still secure way to provide this authentication information. The session classes require an `<AuthorizationCallback>` for that purpose. Various implementations of such a callback exist and, of course, you can write your own callback class if the default ones do not fit your needs.

* [DefaultAuthorizationCallback](https://download.ralph-schuster.eu/eu.ralph-schuster.libs/STABLE/baselib/apidocs/rs/baselib/security/DefaultAuthorizationCallback.html) provides the information directly in your configuration file.
* [PropertiesFileAuthorizationCallback](https://download.ralph-schuster.eu/eu.ralph-schuster.libs/STABLE/baselib/apidocs/rs/baselib/security/PropertiesFileAuthorizationCallback.html) reads the credentials from a Java properties file. ([Example](https://github.com/technicalguru/b4j/blob/b4j-3.0.0/site/conf/properties-authorization-example.properties))
* [TextFileAuthorizationCallback](https://download.ralph-schuster.eu/eu.ralph-schuster.libs/STABLE/baselib/apidocs/rs/baselib/security/TextFileAuthorizationCallback.html) reads the credentials from a plain text file. ([Example](https://github.com/technicalguru/b4j/blob/b4j-3.0.0/site/conf/text-authorization-example.txt))
* [XmlFileAuthorizationCallback](https://download.ralph-schuster.eu/eu.ralph-schuster.libs/STABLE/baselib/apidocs/rs/baselib/security/XmlFileAuthorizationCallback.html) reads the credentials from a XML file. ([Example](https://github.com/technicalguru/b4j/blob/b4j-3.0.0/site/conf/xml-authorization-example.xml))
* [CommandLineAuthorizationCallback](https://download.ralph-schuster.eu/eu.ralph-schuster.libs/STABLE/baselib/apidocs/rs/baselib/security/CommandLineFileAuthorizationCallback.html) prompts you on command line to provide the credentials.
* [GuiAuthorizationCallback](https://download.ralph-schuster.eu/eu.ralph-schuster.libs/STABLE/baselib/apidocs/rs/baselib/security/GuiAuthorizationCallback.html) opens a dialog on your desktop and prompts you for the credentials.

And this is how you define the type of authorization:

```
<bugzilla-session class="...">
   ...
   <AuthorizationCallback class="rs.baselib.security.CommandLineAuthorizationCallback"/>
   ...
</bugzilla-session>
```

Again, detailed information about the specific way of configuring your authorization callback class can be found in the [Javadoc](https://download.ralph-schuster.eu/eu.ralph-schuster.b4j/3.0.0/apidocs/b4j/core/session/package-summary.html).

### Selection of Bugs for your Report

The next step of configuration is the selection of issue records you want to include in your report. For Bugzilla this looks like:

```
<?xml version="1.0" encoding="ISO-8859-1"?>
<bugzilla-report>
   ...
   <search>
       <classification>Your Classification</classification>
       <product>Your Product 1</product>
       <component>ComponentName</component>
       <bug_status>NEW</bug_status>
       <bug_status>ASSIGNED</bug_status>
       <bug_status>REOPENED</bug_status>
       <bug_severity>blocker</bug_severity>
       <bug_severity>critical</bug_severity>
       <bug_severity>major</bug_severity>
   </search>
   ...
</bugzilla-report>
```

The specific tags to be used can be looked up in your Bugzilla system. They correspond to the URL parameters Bugzilla uses while searching. The above example includes all records of Your Product 1’s component ComponentName that are major, critical or blocker and are in status `NEW`, `ASSIGNED` or `REOPENED`. You can add or remove as many tags as you like. Be aware that different tags are logically AND’d while same tag names mean a logical OR.

Jira searches are a bit different. Two types of selection are possible: a search based on a defined filter and a search based on JQL, Jira’s own query language.

(Examples to be done after 1.5 version release)

### Report Configuration

The fourth section is the configuration of your reports. You can define multiple reports to be generated in one execution. Each report is defined as:

```
<?xml version="1.0" encoding="ISO-8859-1"?>
<bugzilla-report>
   ...
   <report class="your-report-class">
      ...
   </report>
   ...
</bugzilla-report>
```

The report class must implement the [BugzillaReportGenerator](https://download.ralph-schuster.eu/eu.ralph-schuster.b4j/3.0.0/apidocs/b4j/report/BugzillaReportGenerator.html) interface.

## Other Configuration
### BugzillaBug implementation class
```
<bugzilla-session class="...">
   ...
   <BugzillaBug class="b4j.core.DefaultIssue"/>
   ...
</bugzilla-session>
```

### Proxy configuration
```
<bugzilla-session class="...">
   ...
   <proxy-host>10.10.10.250:8080</proxy-host>
   <ProxyAuthorizationCallback class="...">
      <!-- same as AuthorizationCallback --->
   </ProxyAuthorizationCallback>
   ...
</bugzilla-session>
```

## Ready-to-use Reports

The B4J Project comes with three ready-to-use reports which will be explained in the following sections:

* The [ChangeLogReport](https://download.ralph-schuster.eu/eu.ralph-schuster.b4j/3.0.0/apidocs/b4j/report/ChangeLogReport.html( creates a Change Log file that you can attach to your product for delivery – see [example output](https://github.com/technicalguru/b4j/blob/b4j-3.0.0/site/examples/test-ChangeLog1.txt).
* The [DetailedBugReport](https://download.ralph-schuster.eu/eu.ralph-schuster.b4j/3.0.0/apidocs/b4j/report/DetailedBugReport.html) creates a file listing all bugs according to your template – see [example output](https://github.com/technicalguru/b4j/blob/b4j-3.0.0/site/examples/test-DetailedBugReport.txt).
* The [ManagementTrackingReport](https://download.ralph-schuster.eu/eu.ralph-schuster.b4j/3.0.0/apidocs/b4j/report/ManagementTrackingReport.html) creates a CSV file with important statistics of your team performance – see [example output](https://github.com/technicalguru/b4j/blob/b4j-3.0.0/site/examples/test-ManagementTrackingReport.txt).

An example of how to configure all reports can be found in the [test-reports.xml](https://github.com/technicalguru/b4j/blob/b4j-3.0.0/src/test/resources/test-reports.xml) file

### Change Log Report

This report creates a classic Change Log text file listing all bugs that have been closed for each respective release. Additional lines can be added in case the records of your system are incomplete.

Here is an example of such a report:

```
Change Log Release 1.4.0
========================
 
  * Fixed Bug #33 - Attachment filename not set
  * Fixed Bug #34 - Timestamps are not parse correctly
  * Fixed Bug #35 - Add saving of attachments
 
Change Log Release 1.3
======================
 
  * Fixed Bug #3 - Create abstract class for BugzillaReportGenerator
  * Fixed Bug #5 - java.lang.IllegalArgumentException: Passed in key must select exactly one node: ProxyAuthorizationCallback(0)
  * Fixed Bug #6 - Add abstract Email Report class
  * Fixed Bug #18 - Filter searches on HttpJiraSession does not return when no issue was found
  * Fixed Bug #31 - Bugzilla 4.2 uses attachments in long descriptions
  * Fixed Bug #32 - Java 6 doesn't know HttpOnly Cookie attribute
```

The report has two parts of configuration – the definition of releases and – if applicable – additional entries to be listed in your Change Log. So a minimum setup looks like this:

```
<?xml version="1.0" encoding="ISO-8859-1"?>
<bugzilla-report>
   ...
   <report class="b4j.report.ChangeLogReport">
      <outputFile>test-ChangeLog1.txt</outputFile>
 
      <ReleaseProvider class="b4j.report.DefaultReleaseProvider">
         <Release timestamp="2012-09-29 00:00">
            <Name>Release 1.3</Name>
         </Release>
         <Release timestamp="2012-12-25 00:00">
            <Name>Release 1.4.0</Name>
         </Release>
      </ReleaseProvider>
   </report>
</bugzilla-report>
```

A Release Provider is required as Bugzilla does not manage fix versions by default like Jira does. A release is defined as a name and the time of its release. Each bug closed before that date will be regarded to be part of this release.

Additional `ReleaseProvider` implementations exist:

* [TextFileReleaseProvider](https://download.ralph-schuster.eu/eu.ralph-schuster.b4j/3.0.0/apidocs/b4j/report/TextFileReleaseProvider.html) reads the definition from a text file
* [XmlFileReleaseProvider](https://download.ralph-schuster.eu/eu.ralph-schuster.b4j/3.0.0/apidocs/b4j/report/XmlFileReleaseProvider.html) reads the definition from a XML file

Details of both implementations and how to configure them can be taken from their Javadoc.

It is possible to add additional Change Log entries:

```
<report class="b4j.report.ChangeLogReport">
   ...
   <ChangeLogEntryProvider class="b4j.report.DefaultChangeLogEntryProvider">
      <Release name="Release 1.4.0">
         This is an additional entry defined in the report configuration for 1.0
         And this is the 2nd entry defined in the report configuration
      </Release>
   </ChangeLogEntryProvider>
</report>
```
The resulting section for release 1.4.0 would look like this then:

```
Change Log Release 1.4.0
========================
 
  * Fixed Bug #33 - Attachment filename not set
  * Fixed Bug #34 - Timestamps are not parse correctly
  * Fixed Bug #35 - Add saving of attachments
  * This is an additional entry defined in the report configuration for 1.0
  * And this is the 2nd entry defined in the report configuration
```

### Detailed Bug Report

This report is basically an export of all selected bugs into a text file according to a defined template. So you can create files of any type, e.g. HTML files. The template file follows the rules that are established for Typo3. Here is a simple example of such a template:

```
<!-- ###REPORT### begin -->
This is a detailed report created from a template.
 
We simply list all bugs reported.
----------
###BUGS###
----------
That's it. End of report.
<!-- ###REPORT### end -->
 
<!-- ###BUG### begin -->
Bug ###ID###: ###STATUS### - ###SUMMARY### (###VERSION###)
<!-- ###BUG### end -->
```

Two sections need to be present: `###REPORT###` defines the main template, `###BUG###` defines the template to be applied for each individual bug. The list of bugs itself is inserted into the `###REPORT###` template at the position of the `###BUGS###` marker.

The configuration itself is pretty simple:

```
<?xml version="1.0" encoding="ISO-8859-1"?>
<bugzilla-report>
   ...
   <report class="b4j.report.DetailedBugReport">
      <outputFile>test-DetailedBugReport.txt</outputFile>
      <templateFile>detailedBugReport.tmpl</templateFile>
   </report>
</bugzilla-report>
```

### Management Tracking Report

This report aims at the statistics of workload and performance of your team. It produces a CSV file that you can import into Excel to further examine the results. The output lists the number of bugs opened and fixed per calendar week and severity. Additional information such as the average fixing time completes the analysis.

You can group severities in order to simplify analysis. The following configuration will combine `BLOCKER`, `CRITICAL` and `MAJOR` bugs into one group called `URGENT`, and severities `MINOR` and `TRIVIAL` into group `MINORS`.

```
<?xml version="1.0" encoding="ISO-8859-1"?>
<bugzilla-report>
   ...
   <report class="b4j.report.ManagementTrackingReport">
      <outputFile>test-ManagementTrackingReport.csv</outputFile>
 
      <!-- Sev groups are optional. All sevs not listed in a group will be tracked individually -->
      <severityGroup name="URGENT">
         <severity>blocker</severity>
         <severity>critical</severity>
         <severity>major</severity>
      </severityGroup>
      <severityGroup name="MINORS">
         <severity>minor</severity>
         <severity>trivial</severity>
      </severityGroup>
   </report>
</bugzilla-report>
```

You can directly produce simple Excel sheets since version 2.0.0 by just stating:

```
   <report class="b4j.report.ManagementTrackingReport">
      <outputFile>test-ManagementTrackingReport.xls</outputFile>
      ...
   </report>
```