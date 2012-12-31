/**
 * 
 */
package b4j.core.session.jira;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import b4j.core.DefaultIssue.DefaultLink;
import b4j.core.Issue;
import b4j.core.LongDescription;
import b4j.core.session.AbstractAuthorizedSession;



/**
 * @author Ralph Schuster
 *
 */
public class JiraXmlHandler extends DefaultHandler {

	private static Logger log = LoggerFactory.getLogger(JiraXmlHandler.class);
	private static SimpleDateFormat JIRA_DATE_FORMATTER = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.US);
	
	private int startIndex;
	private int endIndex;
	private int total;
	private Stack<StringBuffer> currentCharacters;
	private List<Issue> availableIssues;
	private int issuesParsed;
	private int deliveredIssues;
	private Issue currentIssue;
	private Map<String, Object> customInformation;
	private AbstractAuthorizedSession session;
	
	/**
	 * Default Constructor
	 */
	public JiraXmlHandler(AbstractAuthorizedSession session) {
		this.session = session;
		init();
	}

	/**
	 * Initialize the object.
	 */
	private void init() {
		// Initialize all response specific data
		startIndex = -1;
		endIndex = -1;
		total = -1;
		currentCharacters = new Stack<StringBuffer>();
		availableIssues = new ArrayList<Issue>();
		issuesParsed = 0;
		deliveredIssues = 0;
		currentIssue = null;
		customInformation = new HashMap<String, Object>();
	}
	
	/**
	 * @see org.xml.sax.helpers.DefaultHandler#startDocument()
	 */
	@Override
	public void startDocument() throws SAXException {
		init();
	}

	/**
	 * @see org.xml.sax.helpers.DefaultHandler#endDocument()
	 */
	@Override
	public void endDocument() throws SAXException {
		// Nothing to do
		super.endDocument();
	}

	/**
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equals("issue")) {
			startIndex = Integer.parseInt(attributes.getValue("start"));
			endIndex = Integer.parseInt(attributes.getValue("end"));
			total = Integer.parseInt(attributes.getValue("total"));
			if (log.isDebugEnabled()) log.debug("Receiving "+startIndex+"-"+endIndex+" out of "+total+" issues");
		} else if (qName.equals("item")) {
			// Create a new Issue object
			currentIssue = createIssue();
		} else if (currentIssue != null) {
			if (qName.equals("issuelinktype")) {
				customInformation.put("linkTypeId", Integer.parseInt(attributes.getValue("id")));
			} else if (qName.equals("outwardlinks")) {
				customInformation.put("linkTypeDescription", attributes.getValue("description"));
				customInformation.put("linkTypeInward", false);
			} else if (qName.equals("inwardlinks")) {
				customInformation.put("linkTypeDescription", attributes.getValue("description"));
				customInformation.put("linkTypeInward", true);
			} else if (qName.equals("issuekey") && customInformation.containsKey("linkTypeId")) {
				customInformation.put("linkIssueId", Long.parseLong(attributes.getValue("id")));
			} else if (qName.equals("customfield")) {
				customInformation.put("customFieldId", attributes.getValue("id"));
			} else if (qName.equals("assignee")) {
				customInformation.put("userid", attributes.getValue("username"));
			} else if (qName.equals("reporter")) {
				customInformation.put("userid", attributes.getValue("username"));
			} else if (qName.equals("timeoriginalestimate")) {
				currentIssue.setEstimatedTime(Double.parseDouble(attributes.getValue("seconds")));
			} else if (qName.equals("timeoriginalestimate")) {
				currentIssue.setEstimatedTime(Double.parseDouble(attributes.getValue("seconds")));
			} else if (qName.equals("timeestimate")) {
				currentIssue.setRemainingTime(Double.parseDouble(attributes.getValue("seconds")));
			} else if (qName.equals("timespent")) {
				currentIssue.setActualTime(Double.parseDouble(attributes.getValue("seconds")));
			}
		}
		
		// Add a string buffer on the stack
		currentCharacters.push(new StringBuffer());
	}

	/**
	 * Returns the correct issue instance.
	 * @return the issue being created
	 */
	protected Issue createIssue() {
		return session.createIssue();
	}
	
	/**
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		StringBuffer characters = currentCharacters.pop();
		//log.debug(qName+" ended: "+characters);
		if (qName.equals("item")) {
			// If there is no issue size info yet, there is only one issue in the list
			if (getSize() < 0) {
				startIndex = 0;
				endIndex = 1;
				total = 1;
				if (log.isDebugEnabled()) log.debug("Receiving 1 out of 1 issues");
			}
			
			// Add the created Issue to the list of available items
			addIssue(currentIssue);
			currentIssue = null;
		} else if (currentIssue != null) {
			if (qName.equals("summary")) {
				currentIssue.setShortDescription(characters.toString());
			} else if (qName.equals("description")) {
				LongDescription desc = currentIssue.addLongDescription();
				desc.setTheText(characters.toString());
			} else if (qName.equals("project")) {
				currentIssue.setCustomField(qName, characters.toString());
			} else if (qName.equals("key")) {
				currentIssue.setId(characters.toString());
			} else if (qName.equals("type")) {
				currentIssue.setTypeName(characters.toString());
			} else if (qName.equals("link")) {
				currentIssue.setLink(characters.toString());
			} else if (qName.equals("priority")) {
				currentIssue.setPriority(characters.toString());
				currentIssue.setSeverity(characters.toString());
			} else if (qName.equals("parent")) {
				currentIssue.setParentId(characters.toString());
			} else if (qName.equals("status")) {
				currentIssue.setStatus(characters.toString());
			} else if (qName.equals("resolution")) {
				currentIssue.setResolution(characters.toString());
			} else if (qName.equals("assignee")) {
				currentIssue.setAssigneeName(characters.toString());
				String s = (String)customInformation.get("userid");
				if (s == null) s = (String)customInformation.get("username");
				currentIssue.setAssignee(s);
			} else if (qName.equals("reporter")) {
				currentIssue.setReporterName(characters.toString());
				String s = (String)customInformation.get("userid");
				if (s == null) s = (String)customInformation.get("username");
				currentIssue.setReporter(s);
			} else if (qName.equals("created")) {
				currentIssue.setCreationTimestamp(parseDate(characters.toString()));
			} else if (qName.equals("updated")) {
				currentIssue.setDeltaTimestamp(parseDate(characters.toString()));
			} else if (qName.equals("fixVersion")) {
				currentIssue.setVersion(characters.toString());
			} else if (qName.equals("component")) {
				String s = currentIssue.getComponent();
				if (s != null) s += ","+characters.toString();
				else s = characters.toString();
				currentIssue.setComponent(s);
			} else if (qName.equals("subtask")) {
				DefaultLink l = new DefaultLink();
				l.setInward(false);
				l.setLinkType(-1);
				l.setLinkTypeDescription("subtask");
				l.setLinkTypeName("subtask");
				l.setIssueId(characters.toString());
				currentIssue.addChild(l);
			} else if (qName.equals("due")) {
				currentIssue.setDeadline(parseDate(characters.toString()));
			} else if (qName.equals("name") && customInformation.containsKey("linkTypeId")) {
				customInformation.put("linkTypeName", characters.toString());
			} else if (qName.equals("issuelinktype")) {
				customInformation.remove("linkTypeId");
			} else if (qName.equals("issuekey") && customInformation.containsKey("linkTypeId")) {
				DefaultLink l = new DefaultLink();
				l.setLinkType((Integer)customInformation.get("linkTypeId"));
				l.setLinkTypeName((String)customInformation.get("linkTypeName"));
				l.setLinkTypeDescription((String)customInformation.get("linkTypeDescription"));
				l.setInward((Boolean)customInformation.get("linkTypeInward"));
				l.setIssueId(characters.toString());
				currentIssue.addLink(l);
			} else if (qName.equals("issuelinks")) {
				customInformation.remove("linkTypeId");
				customInformation.remove("linkTypeName");
				customInformation.remove("linkTypeDescription");
				customInformation.remove("linkTypeInward");
				customInformation.remove("linkIssueId");
			} else if (qName.equals("customfieldname")) {
				customInformation.put("customFieldName", characters.toString());
			} else if (qName.equals("customfieldvalue")) {
				currentIssue.setCustomField((String)customInformation.get("customFieldId"), characters.toString());
			} else if (qName.equals("customfield")) {
				customInformation.remove("customFieldId");
				customInformation.remove("customFieldName");
			} else {
				currentIssue.setCustomField(qName, characters.toString());
			}
		} else if (qName.equals("rss")) {
			// The stream did end now
			if (getSize() < 0) {
				startIndex = 0;
				endIndex = 0;
				total = 0;
				if (log.isDebugEnabled()) log.debug("Receiving 0 out of 0 issues");
			}
			
		} else {
			// 
		}
		if (characters != null) {
			//log.debug("<"+qName+">"+characters.toString()+"</"+qName+">");
		}
	}

	/**
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		// Add the characters to the current string collected
		StringBuffer buf = currentCharacters.peek();
		if (buf != null) buf.append(ch, start, length); 
	}

	/**
	 * @return the startIndex
	 */
	public int getStartIndex() {
		return startIndex;
	}

	/**
	 * @return the endIndex
	 */
	public int getEndIndex() {
		return endIndex;
	}

	/**
	 * @return the total
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * Returns the size of items returned.
	 * This information is read from the &lt;issue&gt; tag of the RSS feed.
	 * @return size of current response
	 */
	public int getSize() {
		if (getStartIndex() < 0) return -1;
		return getEndIndex() - getStartIndex();
	}
	
	/**
	 * @return the issuesParsed
	 */
	public int getIssuesParsed() {
		return issuesParsed;
	}

	/**
	 * @return the deliveredIssues
	 */
	public int getDeliveredIssues() {
		return deliveredIssues;
	}

	public void startParsing(HttpURLConnection con) throws IOException {
		ParsingThread t = new ParsingThread(con);
		t.start();
	}
	
	/**
	 * Adds a new issue to the list of available issues.
	 * Used by the parsing thread.
	 */
	public synchronized void addIssue(Issue o) {
		while (availableIssues.size() >= 20) {
			try {
				wait();
			} catch (InterruptedException e) {}
		}
		availableIssues.add(o);
		issuesParsed++;
		notify();
	}
	
	/**
	 * Delivers the next issue.
	 * Used by the iterator returning the issues.
	 */
	public synchronized Issue next() {
		Issue rc;
	 
		while (availableIssues.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) { }
		}
		rc = availableIssues.remove(0);
		deliveredIssues++;
		notify();
	 
		return rc;
	}

	/**
	 * Returns true when there are issues available.
	 * ATTENTION: This method will return false before the actual response size is unknown.
	 * So you need to wait for {@link #getSize()} to be non-negative.
	 * @return true if issues are available.
	 */
	public boolean hasNext() {
		return getDeliveredIssues() < getSize();
	}
	
	public Date parseDate(String s) {
		try {
			if ((s == null) || (s.trim().length() == 0)) return null;
			int pos = s.indexOf('(');
			if (pos > 0) {
				s = s.substring(0, pos).trim();
			}
			return JIRA_DATE_FORMATTER.parse(s);
		} catch (Exception e) {
			if (currentIssue != null) {
				String id = currentIssue.getId();
				if (id != null) s += ", issue="+id;
			}
			log.error("Cannot parse Date: "+s, e);
		}
		return null;
	}
	
	protected class ParsingThread extends Thread {
		
		private InputStream in;
		
		public ParsingThread(HttpURLConnection con) throws IOException {
			this.in = con.getInputStream();
		}
		
		public void run() {
			try {
				SAXParserFactory factory   = SAXParserFactory.newInstance();
				SAXParser        saxParser = factory.newSAXParser();
				saxParser.parse(in, JiraXmlHandler.this, null);
			} catch (Exception e) {
				log.error("Error while parsing stream: ", e);
			}
		}
	}
}
