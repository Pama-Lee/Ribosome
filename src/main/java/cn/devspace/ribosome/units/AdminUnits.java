/*
 *
 *   .______       __  .______     ______        _______.  ______   .___  ___.  _______
 *   |   _  \     |  | |   _  \   /  __  \      /       | /  __  \  |   \/   | |   ____|
 *   |  |_)  |    |  | |  |_)  | |  |  |  |    |   (----`|  |  |  | |  \  /  | |  |__
 *   |      /     |  | |   _  <  |  |  |  |     \   \    |  |  |  | |  |\/|  | |   __|
 *   |  |\  \----.|  | |  |_)  | |  `--'  | .----)   |   |  `--'  | |  |  |  | |  |____
 *   | _| `._____||__| |______/   \______/  |_______/     \______/  |__|  |__| |_______|
 *
 *   CreateTime: 2023/2/18
 *   Author: Li JiaKe(Pama)
 */

package cn.devspace.ribosome.units;

import cn.devspace.nucleus.App.Permission.unit.permissionManager;
import cn.devspace.ribosome.entity.User;
import cn.devspace.ribosome.error.errorManager;
import cn.devspace.ribosome.error.errorType;
import cn.devspace.ribosome.manager.user.userUnit;

import java.util.Map;

/**
 * 管理员工具类
 */
public class AdminUnits {

    public static boolean checkParameter(Map<String, String> map, String[] params) {
            String[] var3 = params;
            int var4 = params.length;
            for(int var5 = 0; var5 < var4; ++var5) {
                String param = var3[var5];
                if (!map.containsKey(param)) {
                    return false;
                }
            }
            if (map.get("token") == null) {
                return false;
            }
            if (!verifyAdmin(map.get("token"))) {
                return false;
            }
            return true;
    }

    /**
     * 验证是否为管理员
     * Verify whether it is an administrator
     * @param token token
     * @return boolean
     */
    public static boolean verifyAdmin(String token) {
        User user = userUnit.getUserByToken(token);
        if (user == null) {
            return false;
        }
        String permission = user.getPermissionToken();
        if (permission == null) {
            return false;
        }
        if (permissionManager.permissionManager.checkPermission(permission, "admin")) {
            return true;
        }
        return false;
    }

}
