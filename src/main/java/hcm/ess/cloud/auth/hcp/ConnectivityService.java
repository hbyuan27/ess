//package hcm.ess.cloud.auth.hcp;
//
//import hcm.ess.exception.ServiceApplicationException;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.sap.cloud.account.TenantContext;
//import com.sap.core.connectivity.api.configuration.ConnectivityConfiguration;
//import com.sap.core.connectivity.api.configuration.DestinationConfiguration;
//
//@Service
//public class ConnectivityService {
//
//	@Autowired
//	private TenantContext tenantContext;
//
//	@Autowired
//	private ConnectivityConfiguration connectivityConfiguration;
//
//	// TODO get destination name from configuration file
//	private static final String DESTINATION_SF = "SUCCESSFACTORS";
//	
//	public String getAuthentication() {
//	    if (connectivityConfiguration == null) {
//	      throw new ServiceApplicationException("java:comp/env/connectivityConfiguration not found");
//	    }
//	    
//	    String currentTenantId = tenantContext.getTenant().getAccount().getId();
//	    
//	    if(currentTenantId != null && !currentTenantId.equals(tenantId) && !currentTenantId.equals("dev_default")){
//	    	currentTenantId = tenantId;
//	    }
//	    
//	    DestinationConfiguration destConfiguration;
//	    if (currentTenantId.equals("dev_default")) {
//	      // local testing
//	      destConfiguration = connectivityConfiguration.getConfiguration(SF_ODATA_DESTINATION_NAME);
//	    } else {
//	      destConfiguration = connectivityConfiguration.getConfiguration(currentTenantId, SF_ODATA_DESTINATION_NAME);
//	    }
//	    
//	    if (destConfiguration == null) {
//	      throw new ServiceApplicationException("destination config: SF_ODATA is not found, pls contact your admin");
//	    }
//	    if (destConfiguration != null && "HTTP".equals(destConfiguration.getProperty("Type"))) {
//	      integrationBean.setUrl(destConfiguration.getProperty("URL"));
//	      integrationBean.setCompanyId(destConfiguration.getProperty("CompanyId"));
//	      integrationBean.setUser(destConfiguration.getProperty("User"));
//	      String user = destConfiguration.getProperty("User");
//	      if (user.indexOf("@") > 0) {
//	        integrationBean.setUser(user.split("@")[0]);
//	        integrationBean.setCompanyId(user.split("@")[1]);
//	      } else {
//	        throw new ServiceApplicationException("invalid connection config, missting company Id");
//	      }
//	      integrationBean.setPassword(destConfiguration.getProperty("Password"));
//	    }
//
//	    return integrationBean;
//	}
//}
