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
package b4j.util;

import b4j.core.Attachment;
import b4j.core.Classification;
import b4j.core.Comment;
import b4j.core.Component;
import b4j.core.IssueType;
import b4j.core.Priority;
import b4j.core.Project;
import b4j.core.Resolution;
import b4j.core.Severity;
import b4j.core.Status;
import b4j.core.User;
import b4j.core.Version;

/**
 * Retrieves object lazily when they are needed.
 * The idea is to register objects and bulk-load them later when one of them is retreved.
 * @author ralph
 *
 */
public interface LazyRetriever {

	public void registerClassification(String name);
	public void registerClassification(long id);
	public void registerClassification(Classification classification);
	
	public void registerProject(String name);
	public void registerProject(long id);
	public void registerProject(Project project);

	public void registerComponent(String projectName, String name);
	public void registerComponent(Component component);

	public void registerUser(String name);
	public void registerUser(long id);
	public void registerUser(User user);

	public void registerComment(long id);
	public void registerComment(Comment comment);

	public void registerAttachment(long id);
	public void registerAttachment(Attachment attachment);

	public void registerPriority(String name);
	public void registerPriority(Priority priority);

	public void registerSeverity(String name);
	public void registerSeverity(Severity severity);

	public void registerStatus(String name);
	public void registerStatus(Status status);

	public void registerResolution(String name);
	public void registerResolution(Resolution resolution);

	public void registerIssueType(String name);
	public void registerIssueType(IssueType issueType);
	
	public void registerVersion(String projectName, String name);
	public void registerVersion(Version version);
	
	public Classification getClassification(String name);
	public Classification getClassification(long id);
	public Project getProject(long id);
	public Project getProject(String name);
	public Component getComponent(String projectName, String name);
	public User getUser(String name);
	public User getUser(long id);
	public Comment getComment(long id);
	public Attachment getAttachment(long id);
	public Priority getPriority(String name);
	public Severity getSeverity(String name);
	public Status getStatus(String name);
	public Resolution getResolution(String name);
	public IssueType getIssueType(String name);
	public Version getVersion(String projectName, String name);
}
