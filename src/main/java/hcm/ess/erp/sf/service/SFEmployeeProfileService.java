package hcm.ess.erp.sf.service;

import static hcm.ess.erp.sf.odata.SFODataConstants.APPLICATION_JSON;
import static hcm.ess.erp.sf.odata.SFODataConstants.SOCIAL_ACCOUNT_ENTITYSET_NAME;
import static hcm.ess.erp.sf.odata.SFODataConstants.USER_ENTITYSET_NAME;
import hcm.ess.erp.service.ERPProfileService;
import hcm.ess.erp.sf.bean.QueryInfo;
import hcm.ess.erp.sf.bean.QueryOptionEnum;
import hcm.ess.erp.sf.odata.SFODataService;
import hcm.ess.exception.ServiceApplicationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.ep.feed.ODataFeed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SFEmployeeProfileService implements ERPProfileService {

	@Autowired
	SFODataService sfODataService;

	@Autowired
	SFPicklistService picklistService;

	@Override
	public Map<String, Object> getProfileContent(String userId, List<String> propertyNames)
			throws ServiceApplicationException {
		// build selection criteria
		QueryInfo queryInfo = new QueryInfo(USER_ENTITYSET_NAME, "'" + userId + "'");
		StringBuilder sb = new StringBuilder();
		Iterator<String> iterator = propertyNames.iterator();
		sb.append(iterator.next());
		while (iterator.hasNext()) {
			sb.append(",").append(iterator.next());
		}
		queryInfo.addQueryOption(QueryOptionEnum.SELECT, sb.toString());

		ODataEntry entry = sfODataService.readEntry(APPLICATION_JSON, queryInfo);
		if (entry == null) {
			throw new ServiceApplicationException("Failed to read entry " + USER_ENTITYSET_NAME);
		}
		// parse entry to get profile fields
		return entry.getProperties();
	}

	@Override
	public List<Map<String, String>> getSocialAccounts(String userId) throws ServiceApplicationException {
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		/**
		 * step 1: query PerSocialAccount entity to get imId, url and domain
		 * Note: domain is actually the optionalId of picklist label
		 */
		QueryInfo queryInfo = new QueryInfo(SOCIAL_ACCOUNT_ENTITYSET_NAME);
		// build selection criteria
		queryInfo.addQueryOption(QueryOptionEnum.SELECT, "imId,url,domain");
		// build filter criteria
		queryInfo.addQueryOption(QueryOptionEnum.FILTER, "personIdExternal eq '" + userId + "'");

		ODataFeed feed = sfODataService.readFeed(APPLICATION_JSON, queryInfo);
		if (feed != null) {
			List<ODataEntry> entries = feed.getEntries();
			for (ODataEntry entry : entries) {
				/**
				 * step 2: use picklist optinalId to get the label which is
				 * required by social account
				 */
				result.add(convertDomain2Label(entry.getProperties()));
			}
		}
		return result;
	}

	private Map<String, String> convertDomain2Label(Map<String, Object> source) throws ServiceApplicationException {
		Map<String, String> result = new HashMap<String, String>();

		Set<String> keySet = source.keySet();
		for (String key : keySet) {
			switch (key) {
			case "imId":
			case "url":
				result.put(key, (String) source.get(key));
				break;
			case "domain":
				// TODO - get locale value
				String locale = "zh_CN";
				String optionId = (String) source.get(key);
				String label = picklistService.getPicklistLabel(locale, optionId);
				result.put("label", label);
				break;
			default:
				break;
			}
		}

		return result;
	}

}
