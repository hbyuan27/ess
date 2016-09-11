package hcm.ess.erp.service.proxy;

import hcm.ess.erp.service.ERPProfileService;
import hcm.ess.erp.service.util.ERPServiceFactory;
import hcm.ess.erp.service.util.ERPServiceFactoryManager;
import hcm.ess.erp.service.util.ServiceType;
import hcm.ess.exception.ServiceApplicationException;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileServiceProxy implements ERPProfileService {

	@Autowired
	ERPServiceFactoryManager factoryManager;

	private ERPProfileService integrationService;

	@PostConstruct
	private void init() {
		ERPServiceFactory factory = factoryManager.getFactory();
		integrationService = (ERPProfileService) factory.create(ServiceType.EMPLOYEEPROFILE);
	}

	@Override
	public Map<String, Object> getProfileContent(String employeeId, List<String> fields) throws ServiceApplicationException {
		return integrationService.getProfileContent(employeeId, fields);
	}

	@Override
	public List<Map<String, String>> getSocialAccounts(String userId) throws ServiceApplicationException {
		return integrationService.getSocialAccounts(userId);
	}

}
