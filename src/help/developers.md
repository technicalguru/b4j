# Developer's Instructions

## How to create a Bugzilla session (XML configuration)

You basically need to instantiate BugzillaHttpSession and feed it with configuration data:

```
import org.apache.commons.configuration.XMLConfiguration;
import java.io.File;
import java.util.Iterable;
import b4j.core.session.BugzillaHttpSession;
import b4j.core.DefaultSearchData;
import b4j.core.Issue;
 
// Configure from file
XMLConfiguration myConfig = new XMLConfiguration(new File("myConfig.xml"));
 
// Create the session
BugzillaHttpSession session = new BugzillaHttpSession();
session.configure(myConfig);
 
// Open the session
if (session.open()) {
    // Search abug
    DefaultSearchData searchData = new DefaultSearchData();
    searchData.add("classification", "Java Projects");
    searchData.add("product", "Bugzilla for Java");
 
    // Perform the search
    Iterable i = session.searchBugs(searchData, null);
    for (Issue issue : i) {
       System.out.println("Bug found: "+issue.getId()+" - "+issue.getShortDescription());
    }
 
    // Close the session
    session.close();
}
```

## How to configure your session

### General configuration

```
<?xml version="1.0" encoding="ISO-8859-1"?>
<bugzilla-session class="b4j.core.session.BugzillaHttpSession">
   <bugzilla-home>http://your-bugzilla.your-domain.com/<bugzilla-home>
   <AuthorizationCallback>
      <login>your-name</login>
      <password>your-password</password>
   </AuthorizationCallback>
   <Issue class="b4j.core.DefaultIssue"</Issue>
<bugzilla-session>
```

### Configuring a Proxy

In case you need a proxy, please add:

```
<?xml version="1.0" encoding="ISO-8859-1"?>
<bugzilla-session class="b4j.core.session.BugzillaHttpSession">
   ...
   <proxy-host>192.168.0.250:8080</proxy-host>
   ...
<bugzilla-session>
```

If your proxy requires authorization, add the following snippet:

```
<?xml version="1.0" encoding="ISO-8859-1"?>
<bugzilla-session class="b4j.core.session.BugzillaHttpSession">
   ...
   <ProxyAuthorizationCallback>
      <login>your-proxy-name</login>
      <password>your-proxy-password</password>
   </ProxyAuthorizationCallback>
   ...
<bugzilla-session>
```

## How to create a Bugzilla session (configuration by coding)

An alternative option for above configuration is done via programming only. We begin with configuring the net address where Bugzilla is available and the class we want to use for Bug instances:

```
// Create the session
BugzillaHttpSession session = new BugzillaHttpSession();
session.setBaseUrl(new URL("http://www.mybugzilla.com"));
session.setBugzillaBugClass(DefaultIssue.class);
```

The next steps depend on your specific Bugzilla instance and the network routing. If you require a login, create the appropriate callback.

```
AuthorizationCallback authCallback = new SimpleAuthorizationCallback("username", "password");
session.setAuthorizationCallback(authCallback);
```

If you sit behind a proxy, then you will need this snippet:

```
session.getHttpSessionParams().setProxyHost("192.168.0.250");
session.getHttpSessionParams().setProxyPort(8080);
```

If your proxy requires authorization:

```
AuthorizationCallback proxyAuthCallback = new SimpleAuthorizationCallback("username", "password");
session.getHttpSessionParams().setProxyAuthorizationCallback(proxyAuthCallback);
```

We have successfully configured how B4J connects to your Bugzilla instance. The remaining steps are identical to the first (XML) option.

```
// Open the session
if (session.open()) {
    // Search abug
    DefaultSearchData searchData = new DefaultSearchData();
    searchData.add("classification", "Java Projects");
    searchData.add("product", "Bugzilla for Java");
 
    // Perform the search
    Iterable i = session.searchBugs(searchData, null);
    for (Issue issue : i) {
       System.out.println("Bug found: "+issue.getId()+" - "+issue.getShortDescription());
    }
 
    // Close the session
    session.close();
}
```

You should check out the various [Issue API methods](https://download.ralph-schuster.eu/eu.ralph-schuster.b4j/3.0.0/apidocs/b4j/core/Issue.html) to understand how to retrieve various information from a specific issue.
