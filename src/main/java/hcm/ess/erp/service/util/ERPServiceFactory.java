package hcm.ess.erp.service.util;

import hcm.ess.erp.service.ERPService;

public interface ERPServiceFactory {
	ERPService create(ServiceType type);
}
