package com.blackducksoftware.tools.appedit.naiaudit.dao.cc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackducksoftware.tools.appedit.AppEditException;
import com.blackducksoftware.tools.appedit.naiaudit.dao.AppCompVulnDetailsDao;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnDetails;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnKey;
import com.blackducksoftware.tools.commonframework.core.exception.CommonFrameworkException;
import com.blackducksoftware.tools.connector.codecenter.ICodeCenterServerWrapper;
import com.blackducksoftware.tools.connector.codecenter.application.ApplicationPojo;
import com.blackducksoftware.tools.connector.codecenter.common.CodeCenterComponentPojo;
import com.blackducksoftware.tools.connector.codecenter.common.RequestPojo;
import com.blackducksoftware.tools.connector.codecenter.common.RequestVulnerabilityPojo;

public class CcAppCompVulnDetailsDao implements AppCompVulnDetailsDao {
    private final Logger logger = LoggerFactory.getLogger(this.getClass()
	    .getName());

    private final ICodeCenterServerWrapper ccsw;

    public CcAppCompVulnDetailsDao(ICodeCenterServerWrapper ccsw) {
	this.ccsw = ccsw;
    }

    @Override
    public AppCompVulnDetails updateAppCompVulnDetails(
	    AppCompVulnDetails appCompVulnDetails) throws AppEditException {
	logger.debug("updateAppCompVulnDetails(); called with: "
		+ appCompVulnDetails);

	RequestVulnerabilityPojo updatedRequestVulnerability = new RequestVulnerabilityPojo(
		appCompVulnDetails.getAppCompVulnKey().getVulnerabilityId(),
		appCompVulnDetails.getVulnerabilityName(),
		appCompVulnDetails.getVulnerabilityDescription(),
		appCompVulnDetails.getVulnerabilitySeverity(),
		appCompVulnDetails.getVulnerabilityBaseScore(),
		appCompVulnDetails.getVulnerabilityExploitableScore(),
		appCompVulnDetails.getVulnerabilityImpactScore(),
		appCompVulnDetails.getVulnerabilityDateCreated(),
		appCompVulnDetails.getVulnerabilityDateModified(),
		appCompVulnDetails.getVulnerabilityDatePublished(),
		appCompVulnDetails.getAppCompVulnKey().getRequestId(),
		appCompVulnDetails.getVulnerabilityRemediationComments(),
		appCompVulnDetails.getVulnerabilityRemediationStatus(),
		appCompVulnDetails.getVulnerabilityTargetRemediationDate(),
		appCompVulnDetails.getVulnerabilityActualRemediationDate());
	logger.debug("updatedRequestVulnerability: "
		+ updatedRequestVulnerability);
	try {
	    ccsw.getRequestManager().updateRequestVulnerability(
		    updatedRequestVulnerability);
	} catch (CommonFrameworkException e) {
	    throw new AppEditException(
		    "Error updating Vulnerability metadata for "
			    + appCompVulnDetails.getAppCompVulnKey() + ": "
			    + e.getMessage(), e);
	}

	return appCompVulnDetails;
    }

    @Override
    public Map<AppCompVulnKey, AppCompVulnDetails> getAppCompVulnDetailsMap(
	    String applicationId) throws AppEditException {
	logger.debug("getAppCompVulnDetailsMap() called with appId: "
		+ applicationId);
	Map<AppCompVulnKey, AppCompVulnDetails> result = new HashMap<>();

	List<RequestPojo> requests;
	try {
	    requests = ccsw.getApplicationManager().getRequestsByAppId(
		    applicationId);
	} catch (CommonFrameworkException e) {
	    throw new AppEditException("Error getting application with ID "
		    + applicationId + ": " + e.getMessage(), e);
	}
	for (RequestPojo request : requests) {
	    String requestId = request.getRequestId();

	    CodeCenterComponentPojo comp;
	    try {
		comp = ccsw.getComponentManager()
			.getComponentById(CodeCenterComponentPojo.class,
				request.getComponentId());
	    } catch (CommonFrameworkException e1) {
		throw new AppEditException("Error getting component with ID "
			+ request.getComponentId() + ": " + e1.getMessage(), e1);
	    }
	    List<RequestVulnerabilityPojo> requestVulnerabilities;
	    try {
		requestVulnerabilities = ccsw.getRequestManager()
			.getVulnerabilitiesByRequestId(requestId);
	    } catch (CommonFrameworkException e) {
		throw new AppEditException(
			"Error getting vulnerabilities for request ID "
				+ requestId + ": " + e.getMessage(), e);
	    }
	    for (RequestVulnerabilityPojo requestVulnerability : requestVulnerabilities) {

		logger.debug("Processing: Comp: " + comp.getName() + " / "
			+ comp.getVersion() + ": Vuln: "
			+ requestVulnerability.getVulnerabilityName());
		AppCompVulnKey key = new AppCompVulnKey(applicationId,
			requestVulnerability.getRequestId(), comp.getId(),
			requestVulnerability.getVulnerabilityId());

		AppCompVulnDetails appCompVulnDetails = new AppCompVulnDetails(
			key, comp.getName(), comp.getVersion(),
			requestVulnerability.getVulnerabilityName(),
			requestVulnerability.getSeverity(),

			requestVulnerability.getBaseScore(),
			requestVulnerability.getExploitabilityScore(),
			requestVulnerability.getImpactScore(),
			requestVulnerability.getDateCreated(),
			requestVulnerability.getDateModified(),

			requestVulnerability.getDatePublished(),
			requestVulnerability.getDescription(),
			requestVulnerability.getTargetRemediationDate(),
			requestVulnerability.getActualRemediationDate(),
			requestVulnerability.getReviewStatusName(),
			requestVulnerability.getComments());
		result.put(key, appCompVulnDetails);
	    }
	}

	return result;
    }

    @Override
    public ApplicationPojo getApplicationByNameVersion(String appName,
	    String appVersion) throws AppEditException {
	ApplicationPojo app;
	try {
	    app = ccsw.getApplicationManager().getApplicationByNameVersion(
		    appName, appVersion);
	} catch (CommonFrameworkException e) {
	    throw new AppEditException("Error getting application " + appName
		    + " / " + appVersion + ": " + e.getMessage(), e);
	}
	return app;
    }

    @Override
    public ApplicationPojo getApplicationById(String appId)
	    throws AppEditException {
	ApplicationPojo app;
	try {
	    app = ccsw.getApplicationManager().getApplicationById(appId);
	} catch (CommonFrameworkException e) {
	    throw new AppEditException("Error getting application with ID "
		    + appId + ": " + e.getMessage(), e);
	}
	return app;
    }

}