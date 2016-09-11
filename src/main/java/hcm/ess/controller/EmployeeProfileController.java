package hcm.ess.controller;

import hcm.ess.bean.BasicProfile;
import hcm.ess.bean.SocialAccount;
import hcm.ess.exception.ServiceApplicationException;
import hcm.ess.service.EmployeeProfileService;
import hcm.ess.util.CompanySession;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "profile")
public class EmployeeProfileController {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeProfileController.class);

	@Autowired
	protected CompanySession companySession;

	@Autowired
	EmployeeProfileService profileService;

	@RequestMapping(value = "basic/{companyId}/{employeeId}", method = RequestMethod.GET)
	public BasicProfile getBasicProfile(HttpServletRequest request,
			@PathVariable(value = "companyId") String companyId, @PathVariable(value = "employeeId") String employeeId) {

		companySession.setCompanyId(companyId);

		if (!isEmployeeIdValid()) {
			logger.error("Employee Id not valid: " + employeeId);
			return null;
		}

		BasicProfile basicProfile = null;
		try {
			basicProfile = profileService.getBasicProfile(companyId, employeeId);
		} catch (ServiceApplicationException e) {
			// TODO simplify the message
			logger.error(e.getMessage());
		}

		return basicProfile;
	}

	@RequestMapping(value = "social_account/{companyId}/{employeeId}", method = RequestMethod.GET)
	public List<SocialAccount> getSocialAccounts(HttpServletRequest request,
			@PathVariable(value = "companyId") String companyId, @PathVariable(value = "employeeId") String employeeId) {

		companySession.setCompanyId(companyId);

		if (!isEmployeeIdValid()) {
			logger.error("Employee Id not valid: " + employeeId);
			return null;
		}

		List<SocialAccount> result = null;
		try {
			result = profileService.getSocialAccounts(companyId, employeeId);
		} catch (ServiceApplicationException e) {
			// TODO simplify the message
			logger.error(e.getMessage());
		}

		return result;
	}

	/**
	 * verify existence of employee id and its status
	 * 
	 * @return true if valid
	 */
	private boolean isEmployeeIdValid() {
		// TODO
		// service.getBusinessKey(id);
		// if employee not valid (resigned, none-regular employee, etc)
		// service.verifyEmployeeStatus();
		return true;
	}
}
