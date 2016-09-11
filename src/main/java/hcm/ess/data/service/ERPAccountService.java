package hcm.ess.data.service;

import hcm.ess.data.entity.ERPAccount;
import hcm.ess.util.CompanySession;
import hcm.ess.util.CryptoUtil;
import hcm.ess.util.ERPSystem;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ERPAccountService {

	@Autowired
	private CompanySession companySession;

	@Autowired
	CryptoUtil cryptoUtil;

	@PersistenceContext
	private EntityManager entityManager;

	private String companyId = null;

	private ERPAccount instance = null;

	public String getAccount() {
		return getInstance().getAccount();
	}

	public String getUser() {
		return getInstance().getUser();
	}

	public String getPwd() {
		return cryptoUtil.decrypt(getInstance().getPwd());
	}

	public String getServiceUri() {
		return getInstance().getUri();
	}

	private ERPAccount getInstance() {
		// retrieve account at initialization or if the companyId is updated
		if (companyId == null || !companyId.equals(companySession.getCompanyId())) {
			companyId = companySession.getCompanyId();
			instance = entityManager.find(ERPAccount.class, companyId);
		}
		return instance;
	}

	public ERPSystem getERPSystem() {
		// TODO
		return ERPSystem.SF;
	}

	@Transactional
	public void saveAccount(ERPAccount account) {
		account.setPwd(cryptoUtil.encrypt(account.getPwd()));
		if (entityManager.find(ERPAccount.class, account) == null) {
			entityManager.persist(account);
			entityManager.flush();
		} else {
			entityManager.merge(account);
		}
	}
}
