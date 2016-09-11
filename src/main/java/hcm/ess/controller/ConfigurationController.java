package hcm.ess.controller;

import hcm.ess.data.entity.ERPAccount;
import hcm.ess.data.service.CompanyService;
import hcm.ess.data.service.ERPAccountService;
import hcm.ess.exception.ServiceApplicationException;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "config")
public class ConfigurationController {

	private static final Logger logger = LoggerFactory.getLogger(ConfigurationController.class);

	@Autowired
	ERPAccountService erpAccountService;

	@Autowired
	CompanyService companyService;

	@RequestMapping(value = "erp_account/{companyId}/save", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public ERPAccount saveERPAccount(HttpServletRequest request, @RequestBody ERPAccount account,
			@PathVariable(value = "companyId") String companyId) throws ServiceApplicationException {
		// TODO verify account & update audit info
		if (!account.getCompanyId().equals(companyId)) {
			String msg = "Invalid company id: " + account.getCompanyId();
			logger.error(msg);
			throw new ServiceApplicationException(msg);
		}
		erpAccountService.saveAccount(account);
		return account;
	}

	@RequestMapping(value = "profile_fields/{companyId}/save", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public void saveProfileFields(HttpServletRequest request, @RequestBody List<String> fields,
			@PathVariable(value = "companyId") String companyId) {
		// TODO verify companyId & update audit info
		// TODO use reflect or other mechanism to automatically generate
		// ProfileField bean
		// companyService.saveBasicProfileFields(fields);
	}
}
