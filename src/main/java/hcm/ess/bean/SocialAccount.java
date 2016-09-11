package hcm.ess.bean;

import java.io.Serializable;

public class SocialAccount implements Serializable {

	private static final long serialVersionUID = 9113939327310346415L;

	private String id;
	private String label;
	private String url;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
