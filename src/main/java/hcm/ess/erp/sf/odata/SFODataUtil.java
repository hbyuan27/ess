package hcm.ess.erp.sf.odata;

import hcm.ess.data.service.ERPAccountService;

import java.net.Proxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

@Component
public class SFODataUtil {

	@Autowired
	ERPAccountService erpAccountService;

	/**
	 * the basic authentication form is user@companyId:password
	 * 
	 * @return basic authentication with base64 encoded
	 */
	public String getBasicAuth() {
		StringBuilder sb = new StringBuilder();
		sb.append(erpAccountService.getUser()).append("@");
		sb.append(erpAccountService.getAccount()).append(":");
		sb.append(erpAccountService.getPwd());
		String encodedAuth = Base64Utils.encodeToString(sb.toString().getBytes());
		return "Basic " + encodedAuth;
	}

	public String getServiceUri() {
		return erpAccountService.getServiceUri();
	}

	public Proxy getProxy() {
		Proxy result = null;
//		Integer port = sysConfig.getHttpProxyPort();
//		String host = sysConfig.getHttpProxyHost();
//		if (host != null && port != null) {
//			result = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
//		}
		return result;
	}
}
