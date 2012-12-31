package b4j.core.session;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.codec.binary.Base64;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcSun15HttpTransport;

/**
 * A transport class for setting proxy authorization.
 * @author Ralph Schuster
 *
 */
public class XmlRpcSun15ProxyAuthTransport extends XmlRpcSun15HttpTransport {

	private String proxyUser;
	private String proxyPassword;
	
	public XmlRpcSun15ProxyAuthTransport(XmlRpcClient pClient) {
		super(pClient);
	}
	
	public void setProxyAuthorization(String proxyUser, String proxyPassword) {
		this.proxyUser = proxyUser;
		this.proxyPassword = proxyPassword;
	}
	
	/**
	 * @return the proxyUser
	 */
	public String getProxyUser() {
		return proxyUser;
	}

	/**
	 * @return the proxyPassword
	 */
	public String getProxyPassword() {
		return proxyPassword;
	}

	protected URLConnection newURLConnection(URL pURL) throws IOException {
		URLConnection rc = super.newURLConnection(pURL);
		if ((getProxy() != null) && (getProxyUser() != null) && (getProxyPassword() != null)) {
			String base64 = getProxyUser()+":"+getProxyPassword();
			rc.setRequestProperty("Proxy-Authorization", "Basic "+Base64.encodeBase64(base64.getBytes()));
		}
		return rc;
	}
}

