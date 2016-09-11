package hcm.ess.erp.sf.bean;

import java.util.HashMap;
import java.util.Map;

public class QueryInfo {
	private final String entitySetName;

	private final String keyValue;

	private Map<String, String> queryOptions = new HashMap<String, String>();

	/**
	 * For read OData feed
	 * @param entitySetName
	 */
	public QueryInfo(String entitySetName) {
		this(entitySetName, null);
	}

	/**
	 * For read OData entry
	 * @param entitySetName
	 */
	public QueryInfo(String entitySetName, String keyValue) {
		this.entitySetName = entitySetName;
		this.keyValue = keyValue;
	}

	public String getEntitySetName() {
		return entitySetName;
	}

	public String getKeyValue() {
		return keyValue;
	}

	public Map<String, String> getQueryOptions() {
		return queryOptions;
	}

	public void addQueryOption(QueryOptionEnum queryOption, String value) {
		this.queryOptions.put(queryOption.getName(), value);
	}
}
