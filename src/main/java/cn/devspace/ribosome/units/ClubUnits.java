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
import cn.devspace.ribosome.entity.ClubUser;
import cn.devspace.ribosome.manager.database.MapperManager;
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
     * 获取社团成员列表
     * Get the list of club members
     * @param cid
     * @param uid
     * @return
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



}
