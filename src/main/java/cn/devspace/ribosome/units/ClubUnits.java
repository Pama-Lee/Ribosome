/*
 *
 *   .______       __  .______     ______        _______.  ______   .___  ___.  _______
 *   |   _  \     |  | |   _  \   /  __  \      /       | /  __  \  |   \/   | |   ____|
 *   |  |_)  |    |  | |  |_)  | |  |  |  |    |   (----`|  |  |  | |  \  /  | |  |__
 *   |      /     |  | |   _  <  |  |  |  |     \   \    |  |  |  | |  |\/|  | |   __|
 *   |  |\  \----.|  | |  |_)  | |  `--'  | .----)   |   |  `--'  | |  |  |  | |  |____
 *   | _| `._____||__| |______/   \______/  |_______/     \______/  |__|  |__| |_______|
 *
 *   CreateTime: 2023/2/8
 *   Author: Li JiaKe(Pama)
 */

package cn.devspace.ribosome.units;

import cn.devspace.nucleus.App.Login.Login;
import cn.devspace.nucleus.Message.Log;
import cn.devspace.ribosome.entity.Club;
import cn.devspace.ribosome.entity.ClubRole;
import cn.devspace.ribosome.entity.ClubUser;
import cn.devspace.ribosome.manager.database.MapperManager;
import cn.devspace.ribosome.manager.permission.action.clubActionType;
import cn.devspace.ribosome.manager.permission.permissionType;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public class ClubUnits {


    /**
     * 获取社团成员列表成员数
     * Get the number of members in the club member list
     * @param cid 社团ID | Club ID
     * @return 成员数 | Number of members
     */
    public static int getClubMemberCount(String cid){
        BaseMapper<ClubUser> club =  MapperManager.newInstance().clubUserBaseMapper;
        QueryWrapper<ClubUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("cid",cid);
        Integer clubMemberCount = club.selectCount(queryWrapper);
        return clubMemberCount;
    }

    /**
     * 检查用户是否为社团成员
     * Check if the user is a club member
     * @param cid 社团ID | Club ID
     * @param uid 用户ID | User ID
     * @return 是否为社团成员 | Whether the user is a club member
     */
    public static String checkClubMember(String cid, String uid){
        BaseMapper<ClubUser> club =  MapperManager.newInstance().clubUserBaseMapper;
        QueryWrapper<ClubUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("cid",cid);
        queryWrapper.eq("uid",uid);
        List<ClubUser> clubUsers = club.selectList(queryWrapper);
        if(clubUsers.size() == 0){
            return permissionType.NO_PERMISSION;
        }else{
            return clubUsers.get(0).getRole();
        }
    }

    /**
     * 检查用户是否为社团成员
     * Check if the user is a club member
     * @param cid 社团ID | Club ID
     * @param uid 用户ID | User ID
     * @return 是否为社团成员 | Whether the user is a club member
     */
    public static boolean isMember(String cid, String uid){
        BaseMapper<ClubUser> club =  MapperManager.newInstance().clubUserBaseMapper;
        QueryWrapper<ClubUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("cid",cid);
        queryWrapper.eq("uid",uid);
        List<ClubUser> clubUsers = club.selectList(queryWrapper);
        if(clubUsers.size() == 0){
            return false;
        }else{
            return true;
        }
    }

    /**
     * 获取社团信息
     * Get club information
     * @param cid 社团ID | Club ID
     * @return 社团信息 | Club information
     */
    public static Club getClubByCid(String cid){
        BaseMapper<Club> clubBaseMapper = MapperManager.newInstance().clubBaseMapper;
        QueryWrapper<Club> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("cid",cid);
        List<Club> clubs = clubBaseMapper.selectList(queryWrapper);
        if(clubs.size() == 0){
            return null;
        }else{
            return clubs.get(0);
        }
    }

    /**
     * 检查用户是否为社团社长
     * @param cid 社团ID | Club ID
     * @param uid 用户ID | User ID
     * @return 是否为社团社长 | Whether the user is a club president
     */
    public static boolean isPresident(String cid, String uid){
        BaseMapper<Club> clubBaseMapper = MapperManager.newInstance().clubBaseMapper;
        QueryWrapper<Club> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("cid",cid);
        queryWrapper.eq("president",uid);
        List<Club> clubs = clubBaseMapper.selectList(queryWrapper);
        return clubs.size() != 0;
    }

    /**
     * 获取社团成员对象
     * Get club member object
     * @param cid 社团ID | Club ID
     * @param uid 用户ID | User ID
     * @return 社团成员对象 | Club member object
     */
    public static ClubUser getClubUser(String cid, String uid){
        BaseMapper<ClubUser> clubUserBaseMapper = MapperManager.newInstance().clubUserBaseMapper;
        QueryWrapper<ClubUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("cid",cid);
        queryWrapper.eq("uid",uid);
        List<ClubUser> clubUsers = clubUserBaseMapper.selectList(queryWrapper);
        if(clubUsers.size() == 0){
            return null;
        }else{
            return clubUsers.get(0);
        }
    }


    /**
     * 检查用户是否有权限
     * Check if the user has permission
     * @param cid 社团ID | Club ID
     * @param uid 用户ID | User ID
     * @param actionType 动作类型 | Action type
     * @return 是否有权限 | Whether the user has permission
     */
    public static boolean checkPermission(String cid, String uid, clubActionType actionType){
        ClubUser clubUser = getClubUser(cid,uid);
        if(clubUser == null) {
            Log.sendLog("用户不是社团成员");
            return false;
        }
        String role = clubUser.getRole();
        ClubRole clubRole = MapperManager.newInstance().clubRoleBaseMapper.selectById(role);
        if(clubRole == null) {
            Log.sendLog("用户角色不存在");
            return false;
        }
        List<clubActionType> permission = clubRole.getPermissionList();
        for (clubActionType clubActionType : permission) {
            Log.sendLog(clubActionType.toString()+"---"+actionType.toString());
            if(clubActionType == actionType) return true;
        }
        Log.sendLog("用户没有权限");
        return false;
    }

    /**
     * 检查用户是否有权限
     * Check if the user has permission
     * @param cid 社团ID | Club ID
     * @param uid 用户ID | User ID
     * @param actionTypes 动作类型 | Action type
     * @return  是否有权限 | Whether the user has permission
     */
    public static boolean checkPermission(String cid, String uid, clubActionType[] actionTypes){
        ClubUser clubUser = getClubUser(cid,uid);
        if(clubUser == null) return false;
        String role = clubUser.getRole();
        ClubRole clubRole = MapperManager.newInstance().clubRoleBaseMapper.selectById(role);
        if(clubRole == null) return false;
        List<clubActionType> permission = clubRole.getPermissionList();
        // 需保证全部权限都有
        for (clubActionType actionType : actionTypes) {
            boolean hasPermission = false;
            for (clubActionType clubActionType : permission) {
                if(clubActionType == actionType) {
                    hasPermission = true;
                    break;
                }
            }
            if(!hasPermission) return false;
        }
        return true;
    }

}
