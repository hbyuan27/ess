package hcm.ess.erp.service.util;

import hcm.ess.data.service.ERPAccountService;
import hcm.ess.erp.sf.service.util.SFServiceFactory;
import hcm.ess.util.ERPSystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class ERPServiceFactoryManager {

	@Autowired
	ERPAccountService accountService;

	@Autowired
	@Lazy
	SFServiceFactory sfFactory;

	private static final Logger logger = LoggerFactory.getLogger(ERPServiceFactoryManager.class);

	public ERPServiceFactory getFactory() {
		ERPServiceFactory result = null;

		ERPSystem target = accountService.getERPSystem();
		switch (target) {
		case SF:
			result = sfFactory;
			break;
		case NONE:
		default:
			break;
		}

		if (result == null) {
			String msg = "The implementation of IntegrationServiceFactory was not found for target system: "
					+ target.getName();
			logger.error(msg);
		}

		return result;
	}
}
