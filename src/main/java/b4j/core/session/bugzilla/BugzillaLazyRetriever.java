/*
 * This file is part of Bugzilla for Java.
 *
 *  Bugzilla for Java is free software: you can redistribute it 
 *  and/or modify it under the terms of version 3 of the GNU 
 *  Lesser General Public  License as published by the Free Software 
 *  Foundation.
 *  
 *  Bugzilla for Java is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public 
 *  License along with Bugzilla for Java.  If not, see 
 *  <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package b4j.core.session.bugzilla;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import b4j.core.Attachment;
import b4j.core.Classification;
import b4j.core.Comment;
import b4j.core.Project;
import b4j.core.User;
import b4j.util.AbstractLazyRetriever;

/**
 * Retrieves registered objects lazily from Bugzilla.
 * @author ralph
 *
 */
public class BugzillaLazyRetriever extends AbstractLazyRetriever {

	private BugzillaClient client;
	
	/**
	 * Constructor.
	 * @param client - the client to be used for retrieval
	 */
	public BugzillaLazyRetriever(BugzillaClient client) {
		this.client = client;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadClassifications() throws Exception {
		for (Classification c : client.getClassificationClient().getClassifications(getClassificationIds()).get()) {
			registerClassification(c);
		}
		for (Classification c : client.getClassificationClient().getClassificationsByName(getClassificationNames()).get()) {
			registerClassification(c);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadProjects() throws Exception {
		for (Project p : client.getProductClient().getProducts(getProjectIds()).get()) {
			registerProject(p);
		}
		for (Project p : client.getProductClient().getProductsByName(getProjectNames()).get()) {
			registerProject(p);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadComponents() throws Exception {
		loadProjects();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadUsers() throws Exception {
		for (User u : client.getUserClient().getUsers(getUserIds()).get()) {
			registerUser(u);
		}
		for (User u : client.getUserClient().getUsersByName(getUserNames()).get()) {
			registerUser(u);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadComments() throws Exception {
		Map<String,Set<Comment>> loaded = new HashMap<String, Set<Comment>>();
		Iterable<Comment> i = client.getBugClient().getComments(getCommentIssues()).get();
		for (Comment c : i) {
			String issueId = c.getIssueId();
			Set<Comment> comments = loaded.get(issueId);
			if (comments == null) {
				comments = new HashSet<Comment>();
				loaded.put(issueId, comments);
			}
			comments.add(c);
		}
		for (Map.Entry<String,Set<Comment>> entry : loaded.entrySet()) {
			registerComments(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadAttachments() throws Exception {
		Map<String,Set<Attachment>> loaded = new HashMap<String, Set<Attachment>>();
		Iterable<Attachment> i = client.getBugClient().getAttachments(getAttachmentIssues()).get();
		for (Attachment a : i) {
			String issueId = a.getIssueId();
			Set<Attachment> attachments = loaded.get(issueId);
			if (attachments == null) {
				attachments = new HashSet<Attachment>();
				loaded.put(issueId, attachments);
			}
			attachments.add(a);
		}
		for (Map.Entry<String,Set<Attachment>> entry : loaded.entrySet()) {
			registerAttachments(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadPriorities() throws Exception {
		registerPriority(new BugzillaPriority("P1"));
		registerPriority(new BugzillaPriority("P2"));
		registerPriority(new BugzillaPriority("P3"));
		registerPriority(new BugzillaPriority("P4"));
		registerPriority(new BugzillaPriority("P5"));
		for (String s : getPriorityNames()) {
			registerPriority(new BugzillaPriority(s));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadSeverities() throws Exception {
		registerSeverity(new BugzillaSeverity("blocker"));
		registerSeverity(new BugzillaSeverity("critical"));
		registerSeverity(new BugzillaSeverity("major"));
		registerSeverity(new BugzillaSeverity("blocker"));
		registerSeverity(new BugzillaSeverity("normal"));
		registerSeverity(new BugzillaSeverity("minor"));
		registerSeverity(new BugzillaSeverity("trivial"));
		registerSeverity(new BugzillaSeverity("enhancement"));
		for (String s : getSeverityNames()) {
			registerSeverity(new BugzillaSeverity(s));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadStatus() throws Exception {
		registerStatus(new BugzillaStatus("NEW"));
		registerStatus(new BugzillaStatus("ASSIGNED"));
		registerStatus(new BugzillaStatus("UNCONFIRMED"));
		registerStatus(new BugzillaStatus("CONFIRMED"));
		registerStatus(new BugzillaStatus("VERIFIED"));
		registerStatus(new BugzillaStatus("IN_PROGRESS"));
		registerStatus(new BugzillaStatus("RESOLVED"));
		registerStatus(new BugzillaStatus("CLOSED"));		
		for (String s : getStatusNames()) {
			registerStatus(new BugzillaStatus(s));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadResolutions() throws Exception {
		registerResolution(new BugzillaResolution("FIXED"));
		registerResolution(new BugzillaResolution("INVALID"));
		registerResolution(new BugzillaResolution("WONTFIX"));
		registerResolution(new BugzillaResolution("DUPLICATE"));
		registerResolution(new BugzillaResolution("WORKSFORME"));
		for (String s : getResolutionNames()) {
			registerResolution(new BugzillaResolution(s));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadIssueTypes() throws Exception {
		registerIssueType(new BugzillaIssueType("bug"));
		for (String s : getIssueTypeNames()) {
			registerIssueType(new BugzillaIssueType(s));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadVersions() throws Exception {
		loadProjects();
	}

	
}
