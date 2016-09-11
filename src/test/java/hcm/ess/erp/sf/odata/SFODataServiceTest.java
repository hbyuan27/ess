package hcm.ess.erp.sf.odata;

import hcm.ess.erp.sf.bean.QueryInfo;
import hcm.ess.erp.sf.bean.QueryOptionEnum;
import hcm.ess.erp.sf.odata.SFODataService;
import hcm.ess.exception.ServiceApplicationException;
import hcm.ess.test.spring.TestBase;

import org.apache.olingo.odata2.api.edm.Edm;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.ep.feed.ODataFeed;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SFODataServiceTest extends TestBase {

	private SFODataService sfODataService;
	private static final String APPLICATION_JSON = "application/json";
	private static final String USER_ENTITYSET_NAME = "User";

	@Before
	public void setup() {
		sfODataService = ctx.getBean(SFODataService.class);
	}

	@Test
	public void testGetEdm() throws ServiceApplicationException {
		long startTime = System.currentTimeMillis();
		Edm edm = sfODataService.getEdm();
		long endTime = System.currentTimeMillis();
		Assert.assertNotNull(edm);
		System.out.println(String.valueOf(endTime - startTime));

		startTime = System.currentTimeMillis();
		Edm cachedEdm = sfODataService.getEdm();
		endTime = System.currentTimeMillis();
		Assert.assertEquals(edm, cachedEdm);
		System.out.println(String.valueOf(endTime - startTime));
	}

	@Test
	public void testReadEntry() throws ServiceApplicationException {
		QueryInfo queryInfo = new QueryInfo(USER_ENTITYSET_NAME, "admin3");
		ODataEntry result = sfODataService.readEntry(APPLICATION_JSON, queryInfo);
		Assert.assertNotNull(result);
	}

	@Test
	public void testReadFeed() throws ServiceApplicationException {
		QueryInfo queryInfo = new QueryInfo(USER_ENTITYSET_NAME);
		queryInfo.addQueryOption(QueryOptionEnum.SELECT, "userId, username, businessPhone, email, cellPhone");
		queryInfo.addQueryOption(QueryOptionEnum.FILTER, "userId eq 'admin3'");
		ODataFeed result = sfODataService.readFeed(APPLICATION_JSON, queryInfo);
		Assert.assertNotNull(result);
	}
}
