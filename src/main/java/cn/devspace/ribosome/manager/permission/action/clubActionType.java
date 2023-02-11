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
        APPROVE_NEW_MEMBER, // 批准新成员的权限 | Approve the permission of new member
        DELETE_MEMBER, // 踢出成员 | Delete member
        EDIT_CLUB_INFO, // 修改社团信息 | Edit club information
        CHANGE_PRESIDENT, // 更改社长 | Change president
        VISITABLE, // 访问社团的权限 | Permission to access the club
        CREATE_ACTIVITY, // 创建新的活动 | Create new activity
        CREATE_MATERIAL_APPLICATION, //创建新的物料申请 | Create new material application
}
