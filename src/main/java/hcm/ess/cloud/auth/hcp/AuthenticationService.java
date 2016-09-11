//package hcm.ess.cloud.auth.hcp;
//
//import hcm.ess.exception.ServiceApplicationException;
//
//import javax.security.auth.login.LoginContext;
//import javax.security.auth.login.LoginException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.util.StringUtils;
//
//import com.sap.cloud.account.Tenant;
//import com.sap.cloud.account.TenantContext;
//import com.sap.security.auth.login.LoginContextFactory;
//import com.sap.security.um.user.PersistenceException;
//import com.sap.security.um.user.User;
//import com.sap.security.um.user.UserProvider;
//
//@Service
//public class AuthenticationService {
//
//	private static final String HC_ACCOUNT = "HC_ACCOUNT";
//
//	@Autowired
//	private UserProvider userProvider;
//
//	@Autowired
//	protected TenantContext tanantContext;
//
//	@Autowired
//	HcpAuthentication authentication;
//
//	private static Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
//
//	public static final String LOGIN_USER = "LOGIN_USER";
//
//	// TODO - remove principal?
//	public boolean logout(HttpServletRequest request) {
//		LoginContext loginContext = null;
//		if (request.getRemoteUser() != null) {
//			try {
//				loginContext = LoginContextFactory.createLoginContext();
//				loginContext.logout();
//			} catch (LoginException e) {
//				return false;
//			}
//		}
//		return true;
//	}
//
//	/**
//	 * Get user name of current hcp single-sign-on login session
//	 * 
//	 * @param request
//	 * @return
//	 * @throws ServiceApplicationException
//	 */
//	public String getLoginUserName(HttpServletRequest request) throws ServiceApplicationException {
//		String result = null;
//
//		// check the user from session
//		HttpSession session = request.getSession();
//		User user = (User) session.getAttribute(LOGIN_USER);
//		if (user == null) {
//			if (request.getUserPrincipal() != null) {
//				try {
//					// get current login user via userProvider which autowired
//					// from JNDI and provides access to user storage
//					user = userProvider.getUser(request.getUserPrincipal().getName());
//					session.setAttribute(LOGIN_USER, user);
//					result = user.getName();
//				} catch (PersistenceException e) {
//					String message = "Failed to get hcp login user";
//					logger.error(message);
//					throw new ServiceApplicationException(message, e);
//				}
//			}
//		}
//		return result;
//	}
//
//	/**
//	 * @param userName
//	 * @return
//	 * @throws ServiceApplicationException
//	 */
//	public String getCompanyId(String userName) throws ServiceApplicationException {
//		String companyId = null;
//
//		// get the account which current tenant belongs to
//		Tenant tenant = tanantContext.getTenant();
//		String currentAccount = tenant.getAccount().getId();
//		if (StringUtils.isEmpty(currentAccount)) {
//			String errMsg = "HCP account not found for current tenant: " + tenant.getId();
//			logger.error(errMsg);
//			throw new ServiceApplicationException(errMsg);
//		}
//
//		// get the account who owns & deployed the application
//		String hcAccount = System.getenv(HC_ACCOUNT);
//		if (StringUtils.isEmpty(hcAccount)) {
//			String errMsg = "HCP account which owns the application was not found";
//			logger.error(errMsg);
//			throw new ServiceApplicationException(errMsg);
//		}
//
//		if (hcAccount.equalsIgnoreCase(currentAccount)) {
//			// This is only for stand-alone deployment.
//			// TODO companyId = ?; debug to decide the value
//		} else {
//			// For production environment, all hcp users access
//			// the application by subscription
//			// in this case, we use the account id of current tenant as
//			// the companyId
//			companyId = currentAccount;
//		}
//
//		return companyId;
//	}
//
//	/**
//	 * save authentication info into a Spring session based bean for future
//	 * usage
//	 * 
//	 * @param userName
//	 * @param companyId
//	 */
//	public void saveAuthentication(String userName, String companyId) {
//		authentication.setCompanyId(companyId);
//		authentication.setUserName(userName);
//	}
//}
