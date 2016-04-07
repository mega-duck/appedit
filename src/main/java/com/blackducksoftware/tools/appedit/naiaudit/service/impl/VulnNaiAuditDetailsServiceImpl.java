package com.blackducksoftware.tools.appedit.naiaudit.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.blackducksoftware.tools.appedit.naiaudit.dao.AppCompVulnDetailsDao;
import com.blackducksoftware.tools.appedit.naiaudit.dao.VulnNaiAuditDetailsDao;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnComposite;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnDetails;
import com.blackducksoftware.tools.appedit.naiaudit.model.AppCompVulnKey;
import com.blackducksoftware.tools.appedit.naiaudit.model.VulnNaiAuditDetails;
import com.blackducksoftware.tools.appedit.naiaudit.service.VulnNaiAuditDetailsService;

public class VulnNaiAuditDetailsServiceImpl implements VulnNaiAuditDetailsService {

    // DAO objects (wired)
    private VulnNaiAuditDetailsDao vulnNaiAuditDetailsDao;

    public void setVulnNaiAuditDetailsDao(VulnNaiAuditDetailsDao vulnNaiAuditDetailsDao) {
        this.vulnNaiAuditDetailsDao = vulnNaiAuditDetailsDao;
    }

    private AppCompVulnDetailsDao appCompVulnDetailsDao;

    public void setAppCompVulnDetailsDao(AppCompVulnDetailsDao appCompVulnDetailsDao) {
        this.appCompVulnDetailsDao = appCompVulnDetailsDao;
    }

    @Override
    public List<AppCompVulnComposite> getAppCompVulnCompositeList(String applicationId) {
        List<AppCompVulnComposite> result = new ArrayList<>();
        Map<AppCompVulnKey, AppCompVulnDetails> ccParts = appCompVulnDetailsDao.getAppCompVulnDetailsList(applicationId);
        Map<AppCompVulnKey, VulnNaiAuditDetails> auditParts = vulnNaiAuditDetailsDao.getVulnNaiAuditDetailsList(applicationId);
        for (AppCompVulnKey key : ccParts.keySet()) {
            VulnNaiAuditDetails auditDetails;
            if (auditParts.containsKey(key)) {
                auditDetails = auditParts.get(key);
            } else {
                auditDetails = new VulnNaiAuditDetails(key, "", "");
            }
            AppCompVulnComposite appCompVulnComposite = new AppCompVulnComposite(key, ccParts.get(key), auditDetails);
            result.add(appCompVulnComposite);
        }
        return result;
    }

    @Override
    public AppCompVulnComposite updateVulnNaiAuditDetails(AppCompVulnComposite appCompVulnComposite) {
        AppCompVulnDetails ccPart = appCompVulnDetailsDao.updateAppCompVulnDetails(appCompVulnComposite.getCcPart());
        VulnNaiAuditDetails auditPart = vulnNaiAuditDetailsDao.updateVulnNaiAuditDetails(appCompVulnComposite.getAuditPart());

        return new AppCompVulnComposite(ccPart.getAppCompVulnKey(), ccPart, auditPart);
    }

}
