package org.wso2.custom.pip.attribute.finder.internal;

import org.wso2.carbon.user.core.service.RealmService;

public class AttributeFinderDataHolder {

    private static AttributeFinderDataHolder instance = new AttributeFinderDataHolder();

    private RealmService realmService;

    private AttributeFinderDataHolder() {

    }

    public static AttributeFinderDataHolder getInstance() {

        return instance;
    }

    public RealmService getRealmService() {

        if (realmService == null) {
            throw new RuntimeException("Cannot find the RealmService.");
        }
        return realmService;
    }

    public void setRealmService(RealmService realmService) {

        this.realmService = realmService;
    }

}
