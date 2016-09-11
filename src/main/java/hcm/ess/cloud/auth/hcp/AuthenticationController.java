//package hcm.ess.cloud.auth.hcp;
//
//import hcm.ess.exception.ServiceApplicationException;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.sap.hcm.resume.collection.service.CompanyInfoService;
//
//@RestController
//@RequestMapping(value = "user")
//public class AuthenticationController {
//
//	private static final String SYSTEM_ADMIN_ROLE = "SYSTEMADMIN";
//
//	@Autowired
//	AuthenticationService authService;
//
//	// TODO - refactoring the service
//	@Autowired
//	protected CompanyInfoService companyInfoService;
//
//	/**
//	 * the method is called when the application launches
//	 * 
//	 * @param request
//	 * @return
//	 * @throws ServiceApplicationException
//	 */
//	@RequestMapping(value = "/auth", method = RequestMethod.GET)
//	public Map<String, Object> authenticate(HttpServletRequest request) throws ServiceApplicationException {
//		Map<String, Object> authentication = new HashMap<String, Object>();
//		// get user name of current hcp sso login session
//		String userName = authService.getLoginUserName(request);
//		authentication.put("name", userName);
//		// get companyId
//		String companyId = authService.getCompanyId(userName);
//		if (companyId != null) {
//			companyInfoService.saveCompanyInfoIfNotExist(companyId, userName);
//		}
//		Map<String, Object> companyInfo = authService.getSimplifiedCompanyInfo(companyId);
//		authentication.put("company", companyInfo);
//		// check if user is authorized
//		boolean isUserInRole = request.isUserInRole(SYSTEM_ADMIN_ROLE);
//		authentication.put("isUserInRole", isUserInRole);
//		// update the authentication bean which has session scope life-cycle
//		authService.saveAuthentication(userName, companyId);
//		return authentication;
//	}
//
//	@RequestMapping(value = "/logout", method = RequestMethod.GET)
//	public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		boolean result = authService.logout(request);
//		response.getWriter().write(Boolean.toString(result));
//	}
//}
