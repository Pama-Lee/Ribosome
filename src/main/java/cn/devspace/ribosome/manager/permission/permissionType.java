/*
 *
 *   .______       __  .______     ______        _______.  ______   .___  ___.  _______
 *   |   _  \     |  | |   _  \   /  __  \      /       | /  __  \  |   \/   | |   ____|
 *   |  |_)  |    |  | |  |_)  | |  |  |  |    |   (----`|  |  |  | |  \  /  | |  |__
 *   |      /     |  | |   _  <  |  |  |  |     \   \    |  |  |  | |  |\/|  | |   __|
 *   |  |\  \----.|  | |  |_)  | |  `--'  | .----)   |   |  `--'  | |  |  |  | |  |____
 *   | _| `._____||__| |______/   \______/  |_______/     \______/  |__|  |__| |_______|
 *
 *   CreateTime: 2023/2/4
 *   Author: Li JiaKe(Pama)
 */

package cn.devspace.ribosome.manager.permission;

public class permissionType {

    // 管理员的权限
    // Administrator's permission
    public static final String PERMISSION_ADMIN = "admin";

    // 普通用户的权限
    // User's permission
    public static final String PERMISSION_USER = "user";

    // 社长的权限
    // President's permission
    public static String PERMISSION_PRESIDENT(String clubID){
        return "president_" + clubID;
    }

    // 访问社团的权限
    // Permission to access the club
    public static String PERMISSION_CLUB(String clubID ){
        return "club_" + clubID;
    }


}
