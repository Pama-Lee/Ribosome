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

package cn.devspace.ribosome.mapping;

import cn.devspace.nucleus.Manager.Annotation.Router;
import cn.devspace.nucleus.Manager.RouteManager;
import cn.devspace.ribosome.entity.ClubActivity;
import cn.devspace.ribosome.entity.User;
import cn.devspace.ribosome.error.errorManager;
import cn.devspace.ribosome.error.errorType;
import cn.devspace.ribosome.manager.database.MapperManager;
import cn.devspace.ribosome.manager.user.userUnit;
import cn.devspace.ribosome.units.ClubUnits;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Map;

public class activity extends RouteManager {


    /**
     * 获取社团活动列表
     * Get club activity list
     * @param args cid, token, current, pageSize
     * @return List<ClubActivity>
     */
    @Router("activity/getClubActivityList")
    public Object getClubActivityList(Map<String, String> args) {
        String[] params = {"cid", "token", "current", "pageSize"};
        if (!checkParams(args, params)) return errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);
        User user = userUnit.getUserByToken(args.get("token"));
        if (user == null) return errorManager.newInstance().catchErrors(errorType.Callback_Login_Token_Error);
        if (!ClubUnits.isMember(args.get("cid"), String.valueOf(user.getUid()))) return errorManager.newInstance().catchErrors(errorType.Illegal_Permission);

        Page<ClubActivity> page = new Page<ClubActivity>(Integer.parseInt(args.get("current")), Integer.parseInt(args.get("pageSize")));
        Page<ClubActivity> list = MapperManager.newInstance().clubActivityBaseMapper.selectPage(page, new QueryWrapper<ClubActivity>().eq("cid", args.get("cid")));
           return list;
    }

    @Router("activity/getActivityDetail")
    public Object getActivityDetail(Map<String, String> args){
        return null;
    }

    @Router("activity/newActivity")
    public Object newActivity(Map<String, String> args){
        return null;
    }

    @Router("activity/updateActivity")
    public Object updateActivity(Map<String, String> args){
        return null;
    }

    @Router("activity/deleteActivity")
    public Object deleteActivity(Map<String, String> args){
        return null;
    }



}
