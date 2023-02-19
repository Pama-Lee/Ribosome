/*
 *
 *   .______       __  .______     ______        _______.  ______   .___  ___.  _______
 *   |   _  \     |  | |   _  \   /  __  \      /       | /  __  \  |   \/   | |   ____|
 *   |  |_)  |    |  | |  |_)  | |  |  |  |    |   (----`|  |  |  | |  \  /  | |  |__
 *   |      /     |  | |   _  <  |  |  |  |     \   \    |  |  |  | |  |\/|  | |   __|
 *   |  |\  \----.|  | |  |_)  | |  `--'  | .----)   |   |  `--'  | |  |  |  | |  |____
 *   | _| `._____||__| |______/   \______/  |_______/     \______/  |__|  |__| |_______|
 *
 *   CreateTime: 2023/2/12
 *   Author: Li JiaKe(Pama)
 */

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

package cn.devspace.ribosome.mapping.admin;

import cn.devspace.nucleus.App.Permission.entity.Permission;
import cn.devspace.nucleus.App.Permission.unit.permissionManager;
import cn.devspace.nucleus.App.VisitLobby.Entity.Visit;
import cn.devspace.nucleus.App.VisitLobby.Main;
import cn.devspace.nucleus.Manager.Annotation.Router;
import cn.devspace.nucleus.Manager.RouteManager;
import cn.devspace.nucleus.Message.Log;
import cn.devspace.ribosome.entity.Club;
import cn.devspace.ribosome.entity.User;
import cn.devspace.ribosome.entity.Visitor;
import cn.devspace.ribosome.entity.material.ClassroomArrangement;
import cn.devspace.ribosome.entity.material.MaterialArrangement;
import cn.devspace.ribosome.error.errorManager;
import cn.devspace.ribosome.error.errorType;
import cn.devspace.ribosome.manager.database.MapperManager;
import cn.devspace.ribosome.manager.user.userUnit;
import cn.devspace.ribosome.units.AdminUnits;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class adminBase extends RouteManager {
    @Router("admin/getClubList")
    public Object getClubList(Map<String, String> args){
        String[] params = {"token"};
        if(!AdminUnits.checkParameter(args, params)) return errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);
        QueryWrapper<Club> queryWrapper = new QueryWrapper<>();
        if (args.get("name") != null && !args.get("name").equals("")){
            queryWrapper.like("name", args.get("name"));
        }
        if (args.get("status") != null && !args.get("status").equals("")){
            queryWrapper.eq("status", args.get("status"));
        }
        List<Club> clubList = MapperManager.newInstance().clubBaseMapper.selectList(queryWrapper);
        return returnSuccess(clubList);
    }

    @Router("admin/newClub")
    public Object newClub(Map<String, String> args){
        String[] params = {"token","clubName"};
        if(!AdminUnits.checkParameter(args, params)) return errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);
        Club club = new Club();
        club.setName(args.get("clubName"));
        MapperManager.newInstance().clubBaseMapper.insert(club);
        return returnSuccess(club);
    }

    @Router("selectPresident")
    public Object selectPresident(Map<String, String> args){
        String[] params = {"token","query"};
        if(!AdminUnits.checkParameter(args, params)) return errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);
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
        if(!AdminUnits.checkParameter(args, params)) return errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);
        Club club = MapperManager.newInstance().clubBaseMapper.selectById(args.get("cid"));
        if (club == null) return errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);
        club.setStatus(2);
        MapperManager.newInstance().clubBaseMapper.updateById(club);
        return returnSuccess(club);
    }

    /**
     * 修改社团状态
     * Edit club status
     * @param args cid, status
     * @return club
     */
    @Router("admin/updateClubStatus")
    public Object updateClubStatus(Map<String, String> args){
        String[] params = {"token","cid","status"};
        if(!AdminUnits.checkParameter(args, params)) return errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);
        Club club = MapperManager.newInstance().clubBaseMapper.selectById(args.get("cid"));
        if (club == null) return errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);
        club.setStatus(Integer.valueOf(args.get("status")));
        MapperManager.newInstance().clubBaseMapper.updateById(club);
        return returnSuccess(club);
    }


    /**
     * 获取所有用户
     * Get all users
     * @param args token, current, pageSize
     * @return user list
     */
    @Router("admin/getAllUser")
    public Object getAllUser(Map<String, String> args){
        String[] params = {"token","current","pageSize"};
        if(!AdminUnits.checkParameter(args, params)) return errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);
        Integer pages = Integer.valueOf(args.get("current"));
        Integer size = Integer.valueOf(args.get("pageSize"));

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (args.get("uid") != null && !args.get("uid").equals("")){
           queryWrapper.eq("uid", args.get("uid"));
        }
        if (args.get("name") != null && !args.get("name").equals("")){
            queryWrapper.like("name", args.get("name"));
        }
        if (args.get("email") != null && !args.get("email").equals("")){
            queryWrapper.like("email", args.get("email"));
        }

        // 分页查询
        Page<User> page = new Page<>(pages, size);
        Page<User> userList = MapperManager.newInstance().userBaseMapper.selectPage(page, queryWrapper);
        return returnSuccess(userList);
    }

    /**
     * 获取展板数据
     * Get dashboard data
     * @param args
     * @return
     */
    @Router("admin/dashBoard")
    public Object getDashBoard(Map<String, String> args){
        if (!AdminUnits.checkParameter(args, new String[]{"token"})) return errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        data.put("userCount", MapperManager.newInstance().userBaseMapper.selectCount(null));
        data.put("clubCount", MapperManager.newInstance().clubBaseMapper.selectCount(null));
        data.put("activityCount", MapperManager.newInstance().clubActivityBaseMapper.selectCount(null));
        data.put("visitCount", Main.getVisitCount("ribo"));

        res.put("code", 200);
        res.put("data", data);
        return res;
    }

    /**
     * 获取访问日志
     * Get access log
     * @param args
     * @return
     */
    @Router("admin/getAccessLog")
    public Object getAccessLog(Map<String, String> args){
        if (!AdminUnits.checkParameter(args, new String[]{"token","current","pageSize"})) return errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);
        // 分页
        Integer pages = Integer.valueOf(args.get("current"));
        Integer size = Integer.valueOf(args.get("pageSize"));
        Page<Visitor> page = new Page<>(pages, size);
        QueryWrapper<Visitor> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("app", "ribo");
        if (args.get("ip") != null && !args.get("ip").equals("")){
            queryWrapper.like("ip", args.get("ip"));
        }
        if (args.get("url") != null && !args.get("url").equals("")){
            queryWrapper.like("url", args.get("url"));
        }
        // 倒序
        queryWrapper.last("order by visitTime desc");

        // TODO: 后期需要添加UID查询

        Page<Visitor> accessLogList = MapperManager.newInstance().visitBaseMapper.selectPage(page, queryWrapper);
        return returnSuccess(accessLogList);
    }

    /**
     * 获取所有申请
     * Get all application
     * @param args
     * @return
     */
    @Router("admin/getMaterialApplication")
    public Object getMaterialApplication(Map<String, String> args){
        if (!AdminUnits.checkParameter(args, new String[]{"token","current","pageSize","type"})) return errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);
        // 分页
        Integer pages = Integer.valueOf(args.get("current"));
        Integer size = Integer.valueOf(args.get("pageSize"));

        if (args.get("type").equals("classroom")) {
            // 获取所有申请
            Page<ClassroomArrangement> page = new Page<>(pages, size);
            QueryWrapper<ClassroomArrangement> queryWrapper = new QueryWrapper<>();
            if (args.get("uid") != null && !args.get("uid").equals("")) {
                queryWrapper.eq("uid", args.get("uid"));
            }
            if (args.get("status") != null && !args.get("status").equals("")) {
                queryWrapper.eq("status", args.get("status"));
            }
            // 倒序
            queryWrapper.last("order by createTime desc");
            Page<ClassroomArrangement> materialBaseList = MapperManager.newInstance().classroomArrangementBaseMapper.selectPage(page, queryWrapper);
            return returnSuccess(materialBaseList);
        }else {
            Page<MaterialArrangement> page = new Page<>(pages, size);
            QueryWrapper<MaterialArrangement> queryWrapper = new QueryWrapper<>();
            if (args.get("uid") != null && !args.get("uid").equals("")) {
                queryWrapper.eq("uid", args.get("uid"));
            }
            if (args.get("status") != null && !args.get("status").equals("")) {
                queryWrapper.eq("status", args.get("status"));
            }
            // 倒序
            queryWrapper.last("order by createTime desc");
            Page<MaterialArrangement> materialBaseList = MapperManager.newInstance().materialArrangementBaseMapper.selectPage(page, queryWrapper);
            return returnSuccess(materialBaseList);
        }
    }















































    private Object returnSuccess(Object data){
        Map<String, Object> res = new HashMap<>();
        res.put("code", 200);
        res.put("data", data);
        return res;
    }

}
