package com.blackducksoftware.tools.appedit.naiaudit.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackducksoftware.tools.appedit.exception.AppEditException;
import com.blackducksoftware.tools.appedit.naiaudit.dao.AppCompVulnDetailsDao;
import com.blackducksoftware.tools.appedit.naiaudit.dao.VulnNaiAuditChangeHistoryDao;
import com.blackducksoftware.tools.appedit.naiaudit.dao.VulnNaiAuditDetailsDao;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnComposite;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnDetails;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnKey;
import com.blackducksoftware.tools.appedit.naiaudit.model.VulnNaiAuditChange;
import com.blackducksoftware.tools.appedit.naiaudit.model.VulnNaiAuditDetails;
import com.blackducksoftware.tools.appedit.naiaudit.service.VulnNaiAuditDetailsService;
import com.blackducksoftware.tools.connector.codecenter.application.ApplicationPojo;

public class VulnNaiAuditDetailsServiceImpl implements
	VulnNaiAuditDetailsService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass()
	    .getName());

    // DAO objects (wired)
    private VulnNaiAuditDetailsDao vulnNaiAuditDetailsDao;

    public void setVulnNaiAuditDetailsDao(
	    VulnNaiAuditDetailsDao vulnNaiAuditDetailsDao) {
	this.vulnNaiAuditDetailsDao = vulnNaiAuditDetailsDao;
    }

    private AppCompVulnDetailsDao appCompVulnDetailsDao;

    public void setAppCompVulnDetailsDao(
	    AppCompVulnDetailsDao appCompVulnDetailsDao) {
	this.appCompVulnDetailsDao = appCompVulnDetailsDao;
    }

    private VulnNaiAuditChangeHistoryDao vulnNaiAuditChangeHistoryDao;

    public void setVulnNaiAuditChangeHistoryDao(
	    VulnNaiAuditChangeHistoryDao vulnNaiAuditChangeHistoryDao) {
	this.vulnNaiAuditChangeHistoryDao = vulnNaiAuditChangeHistoryDao;
    }

    @Override
    public List<AppCompVulnComposite> getAppCompVulnCompositeList(
	    String applicationId) throws AppEditException {
	List<AppCompVulnComposite> result = new ArrayList<>();
	Map<AppCompVulnKey, AppCompVulnDetails> ccParts = appCompVulnDetailsDao
		.getAppCompVulnDetailsMap(applicationId);
	Map<AppCompVulnKey, VulnNaiAuditDetails> auditParts = vulnNaiAuditDetailsDao
		.getVulnNaiAuditDetailsMap(applicationId);
	for (AppCompVulnKey key : ccParts.keySet()) {
	    VulnNaiAuditDetails auditDetails;
	    if (auditParts.containsKey(key)) {
		auditDetails = auditParts.get(key);
	    } else {
		auditDetails = new VulnNaiAuditDetails(key, "", "");
		vulnNaiAuditDetailsDao.insertVulnNaiAuditDetails(auditDetails);
	    }
	    AppCompVulnComposite appCompVulnComposite = new AppCompVulnComposite(
		    key, ccParts.get(key), auditDetails);
	    result.add(appCompVulnComposite);
	}
	return result;
    }

    @Override
    public AppCompVulnComposite updateVulnNaiAuditDetails(
	    AppCompVulnComposite appCompVulnComposite) throws AppEditException {

	// Prepend the NAI Audit comment to the Remediation Comment
	Date now = new Date();
	String origRemediationComment = appCompVulnComposite.getCcPart()
		.getVulnerabilityRemediationComments();
	String incomingNaiAuditComment = appCompVulnComposite.getAuditPart()
		.getVulnerabilityNaiAuditComment();
	String newRemediationComment = "[" + now.toString()
		+ ": NAI Audit Comment: " + incomingNaiAuditComment + "] "
		+ origRemediationComment;

	// append nai comment to previous nai comment
	String newNaiAuditComment = "[" + now.toString() + ": "
		+ incomingNaiAuditComment + "] ";

	appCompVulnComposite.getCcPart().setVulnerabilityRemediationComments(
		newRemediationComment);

	// append nai comment to previous nai comment
	appCompVulnComposite.getAuditPart().setVulnerabilityNaiAuditComment(
		newNaiAuditComment);

	AppCompVulnDetails ccPart = appCompVulnDetailsDao
		.updateAppCompVulnDetails(appCompVulnComposite.getCcPart());
	VulnNaiAuditDetails auditPart = vulnNaiAuditDetailsDao
		.updateVulnNaiAuditDetails(appCompVulnComposite.getAuditPart());

	VulnNaiAuditChange vulnNaiAuditChange = new VulnNaiAuditChange(
		new Date(), appCompVulnComposite.getKey(),
		auditPart.getUsername(), auditPart.getOrigNaiAuditStatus(),
		auditPart.getOrigNaiAuditComment(), appCompVulnComposite
			.getAuditPart().getVulnerabilityNaiAuditStatus(),
		appCompVulnComposite.getAuditPart()
			.getVulnerabilityNaiAuditComment());
	insertVulnNaiAuditChange(vulnNaiAuditChange);

	return new AppCompVulnComposite(ccPart.getAppCompVulnKey(), ccPart,
		auditPart);
    }

    @Override
    public ApplicationPojo getApplicationByNameVersion(String appName,
	    String appVersion) throws AppEditException {
	ApplicationPojo app = appCompVulnDetailsDao
		.getApplicationByNameVersion(appName, appVersion);
	return app;
    }

    @Override
    public ApplicationPojo getApplicationById(String appId)
	    throws AppEditException {
	ApplicationPojo app = appCompVulnDetailsDao.getApplicationById(appId);
	return app;
    }

    private void insertVulnNaiAuditChange(VulnNaiAuditChange vulnNaiAuditChange)
	    throws AppEditException {
	vulnNaiAuditChangeHistoryDao
		.insertVulnNaiAuditChange(vulnNaiAuditChange);
    }

}
