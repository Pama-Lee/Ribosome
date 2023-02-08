/*
 *
 *   .______       __  .______     ______        _______.  ______   .___  ___.  _______
 *   |   _  \     |  | |   _  \   /  __  \      /       | /  __  \  |   \/   | |   ____|
 *   |  |_)  |    |  | |  |_)  | |  |  |  |    |   (----`|  |  |  | |  \  /  | |  |__
 *   |      /     |  | |   _  <  |  |  |  |     \   \    |  |  |  | |  |\/|  | |   __|
 *   |  |\  \----.|  | |  |_)  | |  `--'  | .----)   |   |  `--'  | |  |  |  | |  |____
 *   | _| `._____||__| |______/   \______/  |_______/     \______/  |__|  |__| |_______|
 *
 *   CreateTime: 2023/2/5
 *   Author: Li JiaKe(Pama)
 */

package cn.devspace.ribosome.mapping;

import cn.devspace.nucleus.App.Permission.entity.Permission;
import cn.devspace.nucleus.App.Permission.unit.permissionManager;
import cn.devspace.nucleus.Manager.Annotation.Router;
import cn.devspace.nucleus.Manager.RouteManager;
import cn.devspace.ribosome.entity.Club;
import cn.devspace.ribosome.entity.User;
import cn.devspace.ribosome.error.errorManager;
import cn.devspace.ribosome.error.errorType;
import cn.devspace.ribosome.manager.database.MapperManager;
import cn.devspace.ribosome.manager.user.userUnit;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class admin extends RouteManager {
    @Router("admin/getClubList")
    public Object getClubList(Map<String, String> args){
        String[] params = {"token"};
        if (!checkParams(args, params)) return errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);
        if (!checkToken(args)) return errorManager.newInstance().catchErrors(errorType.Illegal_Permission);
        List<Club> clubList = MapperManager.newInstance().clubBaseMapper.selectList(null);
        return returnSuccess(clubList);
    }

    @Router("admin/newClub")
    public Object newClub(Map<String, String> args){
        String[] params = {"token","clubName"};
        if (!checkParams(args, params)) return errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);
        if (!checkToken(args)) return errorManager.newInstance().catchErrors(errorType.Illegal_Permission);
        Club club = new Club();
        club.setName(args.get("clubName"));
        MapperManager.newInstance().clubBaseMapper.insert(club);
        return returnSuccess(club);
    }

    @Router("selectPresident")
    public Object selectPresident(Map<String, String> args){
        String[] params = {"token","query"};
        if (!checkParams(args, params)) return errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);
        if (!checkToken(args)) return errorManager.newInstance().catchErrors(errorType.Illegal_Permission);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", args.get("query"));
        List<User> userList = MapperManager.newInstance().userBaseMapper.selectList(queryWrapper);
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        for (int i=0;i<userList.size(); i++){
            Map<String, Object> user = new HashMap<>();
            user.put("label", userList.get(i).getName());
            user.put("value", userList.get(i).getUid());
            data.put(String.valueOf(i), user);
        }
        res.put("code", 200);
        res.put("data", data);
        return res;
    }

    @Router("admin/deleteClub")
    public Object deleteClub(Map<String, String> args){
        String[] params = {"token","cid"};
        if (!checkParams(args, params)) return errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);
        if (!checkToken(args)) return errorManager.newInstance().catchErrors(errorType.Illegal_Permission);
        Club club = MapperManager.newInstance().clubBaseMapper.selectById(args.get("cid"));
        if (club == null) return errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);
        club.setStatus(0);
        MapperManager.newInstance().clubBaseMapper.updateById(club);
        return returnSuccess(club);
    }












































    private boolean checkToken(Map<String, String> args){
        if (!checkParams(args, new String[]{"token"})) return false;
        User user = userUnit.getUserByToken(args.get("token"));
        if (user == null) return false;
        String permissionToken = user.getPermissionToken();
        return new permissionManager().checkPermission(permissionToken, "admin");
    }


    private Object returnSuccess(Object data){
        Map<String, Object> res = new HashMap<>();
        res.put("code", 200);
        res.put("data", data);
        return res;
    }

}
