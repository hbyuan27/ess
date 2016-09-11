package hcm.ess.erp.sf.service;

import hcm.ess.exception.ServiceApplicationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;



import org.junit.Assert;
import org.junit.Test;

public class SFEmployeeProfileServiceTest {

	@Test
	public void testGetProfileContent() throws ServiceApplicationException {
		SFEmployeeProfileService sfEPService = new SFEmployeeProfileService();
		
		List<String> propNames = new ArrayList<String>();
		propNames.add("userId");
		propNames.add("firstName");
		propNames.add("lastName");
		propNames.add("email");
		Map<String, Object> result = sfEPService.getProfileContent("testUserId", propNames);
		
		Assert.assertNull(result);
	}
}
