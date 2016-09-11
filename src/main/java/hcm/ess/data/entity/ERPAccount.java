package hcm.ess.data.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ERP_ACCOUNT")
public class ERPAccount implements Serializable {

	private static final long serialVersionUID = 2144417418029357059L;

	@Id
	@Column(name = "COMPANYID", length = 32)
	private String companyId;

	@Column(name = "VENDOR", length = 32)
	private String vendor;

	@Column(name = "ACCOUNT", length = 32)
	private String account;

	@Column(name = "USER", length = 32)
	private String user;

	@Column(name = "PWD", length = 512)
	private String pwd;

	@Column(name = "URI", length = 128)
	private String uri;

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

}
