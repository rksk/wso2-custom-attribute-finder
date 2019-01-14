package org.wso2.custom.pip.attribute.finder.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.wso2.carbon.identity.entitlement.pip.AbstractPIPAttributeFinder;
import org.wso2.carbon.user.core.service.RealmService;
import org.wso2.custom.pip.attribute.finder.CustomAttributeFinder;

@Component(name = "custom.attribute.finder",
           immediate = true)
public class AttributeFinderServiceComponent {

    private static final Log LOG = LogFactory.getLog(AttributeFinderServiceComponent.class);

    @Activate
    protected void activate(ComponentContext context) {

        context.getBundleContext()
                .registerService(AbstractPIPAttributeFinder.class.getName(), new CustomAttributeFinder(), null);
        LOG.info("Custom attribute finder activated successfully.");
    }

    @Reference(name = "user.realm.service.default",
               service = RealmService.class,
               cardinality = ReferenceCardinality.MANDATORY,
               policy = ReferencePolicy.DYNAMIC,
               unbind = "unsetRealmService")
    protected void setRealmService(RealmService realmService) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("RealmService is set in the custom attribute finder bundle.");
        }

        AttributeFinderDataHolder.getInstance().setRealmService(realmService);
    }

    protected void unsetRealmService(RealmService realmService) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("RealmService is unset in the custom attribute finder bundle.");
        }
        AttributeFinderDataHolder.getInstance().setRealmService(null);
    }
}
