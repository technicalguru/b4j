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
package b4j.report;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;

/**
 * Abstract implementation for reports that generate a file on a 
 * local disk.
 * <p>
 * This class already implements default methods for most of
 * the interface's methods. So you usually just need to implement
 * {@link BugzillaReportGenerator#closeReport()}.
 * </p>
 * @author Ralph Schuster
 *
 */
public abstract class AbstractEmailReport extends AbstractReportGenerator {

	private Session emailSession = null;
	private String fromName = null;
	private String fromAddress = null;
	private String emailHost = null;

	/**
	 * Default constructor.
	 */
	public AbstractEmailReport() {
	}

	
	/**
	 * Configures the file report.
	 * This implementation retrieves the value for "outputFile" or throws
	 * an exception when no such element was defined.
	 * @param config - the configuration object
	 * @throws ConfigurationException - when a configuration problem occurs
	 */
	@Override
	public void configure(Configuration config) throws ConfigurationException {
		fromName = config.getString("emailFromName");
		fromAddress = config.getString("emailFromAddress");
		emailHost = config.getString("emailHost");
		if (fromName == null) throw new ConfigurationException("No emailFromName element found");
		if (fromAddress == null) throw new ConfigurationException("No emailFromAddress element found");
		if (emailHost == null) throw new ConfigurationException("No emailHost element found");
	}

	protected void sendEmail(InternetAddress address, String htmlBody) throws Exception {
		sendEmail(address, htmlBody, htmlBody);
	}
	
	protected void sendEmail(InternetAddress address, String htmlBody, String textBody) throws Exception {
		MimeMessage msg = new MimeMessage(getSession());
		msg.setFrom(new InternetAddress(fromAddress, fromName));
		msg.addRecipient(Message.RecipientType.TO, address);
		msg.setSubject("[LidoNG Bugzilla] Bug Information");
		Multipart b = createAlternate(textBody, htmlBody);
		msg.setContent(b);
		Transport.send(msg);
	}
	
	private Multipart createAlternate(String plainText, String htmlText) throws Exception {
		Multipart rc = new MimeMultipart("alternative");
		MimeBodyPart text = new MimeBodyPart();
		MimeBodyPart html = new MimeBodyPart();
		text.setText(plainText);
		html.setContent(htmlText, "text/html");
		rc.addBodyPart(text);
		rc.addBodyPart(html);
		return rc;
	}
	
	private Session getSession() throws Exception {
		if (emailSession == null) {
			Properties props = System.getProperties();
			props.put("mail.smtp.host", emailHost);
			emailSession = Session.getDefaultInstance(props, null);
		}
		return emailSession;
	}
	

}
