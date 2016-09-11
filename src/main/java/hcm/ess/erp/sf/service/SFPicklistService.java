package hcm.ess.erp.sf.service;

import static hcm.ess.erp.sf.odata.SFODataConstants.APPLICATION_JSON;
import static hcm.ess.erp.sf.odata.SFODataConstants.PICKLIST_LABEL_ENTITYSET_NAME;
import hcm.ess.erp.sf.bean.QueryInfo;
import hcm.ess.erp.sf.odata.SFODataService;
import hcm.ess.exception.ServiceApplicationException;

import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SFPicklistService {

	@Autowired
	SFODataService sfODataService;

	public String getPicklistLabel(String locale, String optionId) throws ServiceApplicationException {
		String result = null;
		String keyValue = "locale='" + locale + "'," + "optionId=" + optionId + "L";
		QueryInfo queryInfo = new QueryInfo(PICKLIST_LABEL_ENTITYSET_NAME, keyValue);

		ODataEntry entry = sfODataService.readEntry(APPLICATION_JSON, queryInfo);
		if (entry != null) {
			result = (String) entry.getProperties().get("label");
		}

		return result;
	}

}
