package hcm.ess.service;

import hcm.ess.bean.BasicProfile;
import hcm.ess.bean.SocialAccount;
import hcm.ess.data.service.CompanyService;
import hcm.ess.erp.service.ERPProfileService;
import hcm.ess.exception.ServiceApplicationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class EmployeeProfileService {

	@Autowired
	@Qualifier(value = "profileServiceProxy")
	private ERPProfileService serviceProxy;

	@Autowired
	private CompanyService companyService;

	public BasicProfile getBasicProfile(String companyId, String employeeId) throws ServiceApplicationException {
		BasicProfile result = null;

		// get profile fields from company setting
		List<String> fields = companyService.getBasicProfileFields(companyId);

		Map<String, Object> content = serviceProxy.getProfileContent(employeeId, fields);
		if (content != null && content.size() > 0) {
			result = convertProfileContent(content);
		}

		return result;
	}

	public List<SocialAccount> getSocialAccounts(String companyId, String employeeId) throws ServiceApplicationException {
		List<SocialAccount> result = null;

		List<Map<String, String>> content = serviceProxy.getSocialAccounts(employeeId);
		if (content != null && content.size() > 0) {
			result = convertSocialAccountContent(content);
		}

		return result;
	}

	// TODO refactoring to use one convert function
	private List<SocialAccount> convertSocialAccountContent(List<Map<String, String>> content) {
		List<SocialAccount> result = new ArrayList<SocialAccount>();
		for (Map<String, String> account : content) {
			SocialAccount accountBean = new SocialAccount();
			accountBean.setId(account.get("imId"));
			accountBean.setUrl(account.get("url"));
			accountBean.setLabel(account.get("label"));
			result.add(accountBean);
		}
		return result;
	}

	private BasicProfile convertProfileContent(Map<String, Object> content) {
		// TODO use mapping or reflection to fulfill the BasicProfile bean
		BasicProfile result = new BasicProfile();
		Set<String> keys = content.keySet();
		for (String key : keys) {
			String value = (String) content.get(key);
			switch (key) {
			case "username":
				result.setUsername(value);
				break;
			case "firstName":
				result.setFirstName(value);
				break;
			case "lastName":
				result.setLastName(value);
				break;
			case "email":
				result.setEmail(value);
				break;
			case "title":
				result.setTitle(value);
				break;
			case "department":
				result.setDepartment(value);
				break;
			case "businessPhone":
				result.setBusinessPhone(value);
				break;
			case "manager":
				result.setManager(value);
				break;
			case "jobCode":
				result.setJobCode(value);
				break;
			case "location":
				result.setLocation(value);
				break;
			case "city":
				result.setCity(value);
				break;
			case "state":
				result.setState(value);
				break;
			case "zipCode":
				result.setZipCode(value);
				break;
			case "hireDate":
				result.setHireDate(value);
				break;
			default:
				break;
			}
		}
		return result;
	}

}
