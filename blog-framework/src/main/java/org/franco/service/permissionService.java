package org.franco.service;


import org.franco.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("permissionService")
public class permissionService {

    /**
     * see if the user has the permission to do the operation
     * @param permission
     * @return
     */
    public boolean hasPermission(String permission){
        if(SecurityUtils.isAdmin()){
            return true;
        }

        List<String> permissions = SecurityUtils.getLoginUser().getPermissions();

        return permissions.contains(permission);
    }
}
