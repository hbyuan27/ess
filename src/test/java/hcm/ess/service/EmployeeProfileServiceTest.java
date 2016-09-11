package hcm.ess.service;

import hcm.ess.bean.BasicProfile;
import hcm.ess.exception.ServiceApplicationException;
import hcm.ess.test.spring.TestBase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EmployeeProfileServiceTest extends TestBase {

	private EmployeeProfileService epService;

	@Before
	public void setup() {
		epService = ctx.getBean(EmployeeProfileService.class);
	}

	@Test
	public void testGetEmployeeProfile() throws ServiceApplicationException {
		BasicProfile result = epService.getBasicProfile("testId", null);
		Assert.assertNotNull(result);
	}
}
