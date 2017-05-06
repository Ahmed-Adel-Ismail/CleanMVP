package com.base.presentation.base.abstracts.system;

import com.base.presentation.system.ManifestPermissions;

/**
 * implement this interface if your class will need to access the manifest for any reason
 * <p>
 * Created by Ahmed Adel on 1/17/2017.
 */
public interface ManifestAccessor {

    /**
     * get the {@link ManifestPermissions}
     *
     * @return the {@link ManifestPermissions}
     */

    ManifestPermissions getManifestPermissions();
}
