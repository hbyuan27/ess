package hcm.ess.erp.sf.bean;

public enum QueryOptionEnum {
	SELECT("$select"), FILTER("$filter"), EXPAND("$expand"), COUNT("$count"), ORDERBY("$orderby"), TOP("$top"), SKIP(
			"$skip");

	private final String name;

	QueryOptionEnum(String name) {
		this.name = name;
	};

	public String getName() {
		return this.name;
	}
}
