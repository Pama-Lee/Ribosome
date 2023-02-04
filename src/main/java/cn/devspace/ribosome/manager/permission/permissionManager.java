/*
 *
 *   .______       __  .______     ______        _______.  ______   .___  ___.  _______
 *   |   _  \     |  | |   _  \   /  __  \      /       | /  __  \  |   \/   | |   ____|
 *   |  |_)  |    |  | |  |_)  | |  |  |  |    |   (----`|  |  |  | |  \  /  | |  |__
 *   |      /     |  | |   _  <  |  |  |  |     \   \    |  |  |  | |  |\/|  | |   __|
 *   |  |\  \----.|  | |  |_)  | |  `--'  | .----)   |   |  `--'  | |  |  |  | |  |____
 *   | _| `._____||__| |______/   \______/  |_______/     \______/  |__|  |__| |_______|
 *
 *   CreateTime: 2023/2/2
 *   Author: Li JiaKe(Pama)
 */

package cn.devspace.ribosome.manager.permission;

import cn.devspace.ribosome.entity.User;
import cn.devspace.ribosome.manager.user.userUnit;

public class permissionManager {
    private final cn.devspace.nucleus.App.Permission.unit.permissionManager permissionManager = new cn.devspace.nucleus.App.Permission.unit.permissionManager();

    /**
     * 检查用户权限
     * Check user permission
     * @param user 用户
     * @param permission 权限
     * @return 是否有权限
     */
    public boolean checkPermission(User user, String permission) {
        return permissionManager.checkPermission(getPermissionToken(user), permission);
    }

    /**
     * 检查用户权限
     * Check user permission
     * @param openid 用户openid
     * @param permission 权限
     * @return 是否有权限
     */
    public boolean checkPermission(String openid, String permission) {
        return permissionManager.checkPermission(getPermissionToken(userUnit.getUserByopenID(openid)),permission);
    }

    public User setPermission(User user, String permission) {
        String token = permissionManager.newPermission(permission);
        user.setPermissionToken(token);
        userUnit.updateUser(user);
        return user;
    }

    public User setPermission(User user, String[] permissions) {
        String token = permissionManager.newPermission(permissions);
        user.setPermissionToken(token);
        userUnit.updateUser(user);
        return user;
    }

    /**
     * 获取用户权限令牌
     * @param user 用户
     * @return 权限令牌
     */
    private String getPermissionToken(User user) {
        if (user == null) return null;
        return user.getPermissionToken();
    }
}
