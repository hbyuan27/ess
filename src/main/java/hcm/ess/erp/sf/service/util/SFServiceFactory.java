package hcm.ess.erp.sf.service.util;

import hcm.ess.erp.service.ERPService;
import hcm.ess.erp.service.util.ERPServiceFactory;
import hcm.ess.erp.service.util.ServiceType;
import hcm.ess.erp.sf.service.SFEmployeeProfileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class SFServiceFactory implements ERPServiceFactory {

  @Autowired
  @Lazy
  SFEmployeeProfileService sfEPService;

  @Override
  public ERPService create(ServiceType type) {
    ERPService result = null;
    switch (type) {
    case EMPLOYEEPROFILE:
      result = sfEPService;
      break;
    default:
      break;
    }
    return result;
  }

}
