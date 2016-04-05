package com.blackducksoftware.tools.appedit.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.blackducksoftware.tools.appedit.naiaudit.model.Items;

/**
 * Controller for requests for and form submissions from the Edit NAI Audit Details
 * screen.
 *
 * @author sbillings
 *
 */
@Controller
@SessionAttributes({ "app", "dataSource" })
public class EditNaiAuditDetailsController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass()
            .getName());

    /**
     * Handles Edit NAI Audit Details form submissions. Updates app in Code Center.
     */
    @RequestMapping(value = "/editnaiauditdetails", method = RequestMethod.POST)
    public String saveNaiAuditDetails(@ModelAttribute("selectedVulnerabilities") Items selectedVulnerabilities) {

        logger.info("EditNaiAuditDetailsController.saveNaiAuditDetails(): selectedVulnerabilities: " + selectedVulnerabilities);
        /*
         * // Load config
         * String configFilename = System.getProperty("user.home") + "/"
         * + AppEditConstants.CONFIG_FILENAME;
         * AppEditConfigManager config = null;
         * try {
         * config = new AppEditConfigManager(configFilename);
         * } catch (Exception e) {
         * String msg = "Error constructing configuration manager: "
         * + e.getMessage();
         * logger.error(msg);
         * model.addAttribute("message", msg);
         * return "error/programError";
         * }
         * 
         * // Validate input
         * int i = 0;
         * for (String attrLabel : app.getAttrNames()) {
         * InputValidatorEditAppDetails inputValidator = new InputValidatorEditAppDetails(
         * config);
         * if (!inputValidator.validateAttributeValue(attrLabel, app
         * .getAttrValues().get(i++).getValue())) {
         * String msg = "The value of " + attrLabel + " is invalid.";
         * logger.error(msg);
         * model.addAttribute("message", msg);
         * return "error/programError";
         * }
         * }
         * 
         * // Convert the View-friendly appDetails to a generic appDetails object
         * AppDetailsBeanConverter converter = new AppDetailsBeanConverter(config);
         * AppDetails appDetails = converter.createAppDetails(app);
         * 
         * // Get the logged-in user's details
         * String username = (String) SecurityContextHolder.getContext()
         * .getAuthentication().getPrincipal();
         * 
         * // Make sure they are on this app's team (list of users that can access
         * // it)
         * boolean isAuthorized = dataSource.authorizeUser(appDetails.getAppId(),
         * username);
         * if (!isAuthorized) {
         * String msg = "You are not authorized to access this application";
         * logger.error(msg);
         * model.addAttribute("message", msg);
         * return "error/programError";
         * }
         * 
         * try {
         * dataSource.update(appDetails); // update app in Code Center
         * } catch (Exception e) {
         * String msg = "Error updating application " + app.getAppName()
         * + ": " + e.getMessage();
         * logger.error(msg);
         * model.addAttribute("message", msg);
         * return "error/programError";
         * }
         */
        return "editNaiAuditDetailsForm";
    }
}