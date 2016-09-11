package hcm.ess.data.service;

import hcm.ess.bean.BasicProfile;
import hcm.ess.data.entity.Company;
import hcm.ess.util.CompanySession;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {

	@Autowired
	private CompanySession companySession;

	@PersistenceContext
	private EntityManager entityManager;

	public Company getCompanyInfo() {
		return entityManager.find(Company.class, companySession.getCompanyId());
	}

	// TODO configure unique field list for each company
	public List<String> getBasicProfileFields(String companyId) {
		List<String> result = new ArrayList<String>();

		// use reflection to get list from BasicProfile bean
		Class<BasicProfile> basicProfileClass = BasicProfile.class;

		Field[] fields = basicProfileClass.getDeclaredFields();
		for (Field field : fields) {
			if (field.getName().equals("serialVersionUID")) {
				continue;
			}
			result.add(field.getName());
		}

		return result;
	}

}
