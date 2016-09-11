package hcm.ess.erp.service;

import hcm.ess.exception.ServiceApplicationException;

import java.util.List;
import java.util.Map;

public interface ERPProfileService extends ERPService {
	Map<String, Object> getProfileContent(String employeeId, List<String> fields) throws ServiceApplicationException;
	List<Map<String, String>> getSocialAccounts(String userId) throws ServiceApplicationException;
}
