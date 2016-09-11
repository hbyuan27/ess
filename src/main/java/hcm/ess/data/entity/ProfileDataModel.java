package hcm.ess.data.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PROFILE_DATA_MODEL")
public class ProfileDataModel implements Serializable {
	
	@Id
	@Column(name = "COMPANYID", length = 32)
	private String companyId;
	
	
}
