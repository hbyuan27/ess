package hcm.ess.erp.service.util;

public enum ServiceType {
	EMPLOYEEPROFILE("EmployeeProfile"), NONE("NONE");

	private String name;

	private ServiceType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
