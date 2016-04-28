package com.blackducksoftware.tools.appedit.naiaudit.model;

public class FullTextViewData {
    private String applicationName;
    private String applicationVersion;
    private String componentName;
    private String componentVersion;
    private String vulnerabilityName;
    private String itemName;
    private String fullText;

    public FullTextViewData() {
    }

    public FullTextViewData(String applicationName, String applicationVersion,
	    String componentName, String componentVersion,
	    String vulnerabilityName, String itemName, String fullText) {

	this.applicationName = applicationName;
	this.applicationVersion = applicationVersion;
	this.componentName = componentName;
	this.componentVersion = componentVersion;
	this.vulnerabilityName = vulnerabilityName;
	this.itemName = itemName;
	this.fullText = fullText;
    }

    public String getApplicationName() {
	return applicationName;
    }

    public void setApplicationName(String applicationName) {
	this.applicationName = applicationName;
    }

    public String getApplicationVersion() {
	return applicationVersion;
    }

    public void setApplicationVersion(String applicationVersion) {
	this.applicationVersion = applicationVersion;
    }

    public String getComponentName() {
	return componentName;
    }

    public void setComponentName(String componentName) {
	this.componentName = componentName;
    }

    public String getComponentVersion() {
	return componentVersion;
    }

    public void setComponentVersion(String componentVersion) {
	this.componentVersion = componentVersion;
    }

    public String getVulnerabilityName() {
	return vulnerabilityName;
    }

    public void setVulnerabilityName(String vulnerabilityName) {
	this.vulnerabilityName = vulnerabilityName;
    }

    public String getItemName() {
	return itemName;
    }

    public void setItemName(String itemName) {
	this.itemName = itemName;
    }

    public String getFullText() {
	return fullText;
    }

    public void setFullText(String fullText) {
	this.fullText = fullText;
    }

    @Override
    public String toString() {
	return "FullTextViewData [applicationName=" + applicationName
		+ ", applicationVersion=" + applicationVersion
		+ ", componentName=" + componentName + ", componentVersion="
		+ componentVersion + ", vulnerabilityName=" + vulnerabilityName
		+ ", itemName=" + itemName + ", fullText=" + fullText + "]";
    }

}
