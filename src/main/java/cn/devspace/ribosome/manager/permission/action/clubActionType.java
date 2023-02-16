/*
 *
 *   .______       __  .______     ______        _______.  ______   .___  ___.  _______
 *   |   _  \     |  | |   _  \   /  __  \      /       | /  __  \  |   \/   | |   ____|
 *   |  |_)  |    |  | |  |_)  | |  |  |  |    |   (----`|  |  |  | |  \  /  | |  |__
 *   |      /     |  | |   _  <  |  |  |  |     \   \    |  |  |  | |  |\/|  | |   __|
 *   |  |\  \----.|  | |  |_)  | |  `--'  | .----)   |   |  `--'  | |  |  |  | |  |____
 *   | _| `._____||__| |______/   \______/  |_______/     \______/  |__|  |__| |_______|
 *
 *   CreateTime: 2023/2/11
 *   Author: Li JiaKe(Pama)
 */

package cn.devspace.ribosome.manager.permission.action;

/**
 * 社团的权限
 */
public enum clubActionType {
        APPROVE_NEW_MEMBER("Approve new member"), // 批准新成员的权限 | Approve the permission of new member
        DELETE_MEMBER("Delete member"), // 踢出成员 | Delete member
        EDIT_CLUB_INFO("Edit club info"), // 修改社团信息 | Edit club information
        CHANGE_PRESIDENT("(!)Change president"), // 更改社长 | Change president
        VISITABLE("Visit club page"), // 访问社团的权限 | Permission to access the club
        CREATE_ACTIVITY("Create a new activity"), // 创建新的活动 | Create new activity
        CREATE_MATERIAL_APPLICATION("Create a new material application"), //创建新的物料申请 | Create new material application
        CREATE_MATERIAL("Create a new material application"), // 创建新的物料 | Create new material
        CREATE_ANNOUNCEMENT("Create a new announcement"); // 创建新的物料类型 | Create new material type

        private final String description;
        private clubActionType(String des) {
                this.description = des;
        }

        public String getDescription() {
                return description;
        }

        // 检查是否为此枚举的值 | Check if it is the value of this enumeration
        public static boolean isset(String value) {
                for (clubActionType v : values()) {
                        if (v.name().equals(value)) {
                                return true;
                        }
                }
                return false;
        }
}
