package org.wso2.custom.pip.attribute.finder;

import org.wso2.carbon.user.core.UserCoreConstants;
import org.wso2.custom.pip.attribute.finder.internal.AttributeFinderDataHolder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.entitlement.pip.DefaultAttributeFinder;
import org.wso2.carbon.user.core.UserStoreManager;
import org.wso2.carbon.utils.multitenancy.MultitenantUtils;

import java.util.Arrays;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomAttributeFinder extends DefaultAttributeFinder {

    private static final Log log = LogFactory.getLog(CustomAttributeFinder.class);

    private static final String ROLE = "http://wso2.org/claims/role";

    public void init(Properties properties) throws Exception {
        if (log.isDebugEnabled()) {
            log.info("CustomAttributeFinder is initialized successfully");
        }
    }

    public String getModuleName() { return "Custom Attribute Finder"; }

    @Override
    public Set<String> getAttributeValues(String subjectId, String resourceId, String actionId,
                                          String environmentId, String attributeId, String issuer) throws Exception {

        Set<String> attributeValues = null;

        if (log.isDebugEnabled()) {
            log.debug("CustomAttributeFinder calling custom get attribute values\n" +
                    "subjectId: " + subjectId + "\nresourceId: " + resourceId + "\nactionId: " + actionId +
                    "\nenvironmentId: " + environmentId + "\nattributeId: " + attributeId + "\nissuer: " + issuer);
        }

        attributeValues = super.getAttributeValues(subjectId, resourceId, actionId, environmentId,
                attributeId.toString(), issuer);
        if (log.isDebugEnabled()) {
            log.debug("attributeValues from DefaultAttributeFinder: " + attributeValues);
        }

        // If attributeId!=ROLE, just return attributeValues from DefaultAttributeFinder
        if (!ROLE.equals(attributeId)){
            if (log.isDebugEnabled()) {
                log.debug("Returning since attributeId is not " + ROLE);
            }
            return  attributeValues;
        }

        String regexForSecondaryUserStore = ".+" + UserCoreConstants.DOMAIN_SEPARATOR + ".+";
        Pattern pattern = Pattern.compile(regexForSecondaryUserStore);

        Matcher matcher = pattern.matcher(subjectId);

        if (!subjectId.startsWith(UserCoreConstants.PRIMARY_DEFAULT_DOMAIN_NAME + UserCoreConstants.DOMAIN_SEPARATOR)
                && matcher.matches()) {
            if (log.isDebugEnabled()) {
                log.debug("User in secondary userstore");
            }
//            int tenantId = AttributeFinderDataHolder.getInstance().getRealmService().getTenantManager().
//                    getTenantId(MultitenantUtils.getTenantDomain(subjectId));
//
//            UserStoreManager userStoreManager = (UserStoreManager) AttributeFinderDataHolder.getInstance().getRealmService().
//                    getTenantUserRealm(tenantId).getUserStoreManager();
//            log.info("Userstore retrieved successfully.");
//            String[] roles = userStoreManager.getRoleListOfUser(subjectId);
//            log.info("Roles retrieved from userstore successfully.");

            if (log.isDebugEnabled()) {
                log.debug("append hospitaladmin role to attributeValues");
            }
            attributeValues.add("hospitaladmin");
        } else {
            if (log.isDebugEnabled()) {
                log.debug("User not in secondary userstore");
            }
        }
        return attributeValues;
    }

}
