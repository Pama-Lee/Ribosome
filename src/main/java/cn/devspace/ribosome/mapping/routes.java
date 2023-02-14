/*
 *
 *   .______       __  .______     ______        _______.  ______   .___  ___.  _______
 *   |   _  \     |  | |   _  \   /  __  \      /       | /  __  \  |   \/   | |   ____|
 *   |  |_)  |    |  | |  |_)  | |  |  |  |    |   (----`|  |  |  | |  \  /  | |  |__
 *   |      /     |  | |   _  <  |  |  |  |     \   \    |  |  |  | |  |\/|  | |   __|
 *   |  |\  \----.|  | |  |_)  | |  `--'  | .----)   |   |  `--'  | |  |  |  | |  |____
 *   | _| `._____||__| |______/   \______/  |_______/     \______/  |__|  |__| |_______|
 *
 *   CreateTime: 2023/1/29
 *   Author: Li JiaKe(Pama)
 */

package cn.devspace.ribosome.mapping;

import cn.devspace.nucleus.App.Permission.unit.permissionManager;
import cn.devspace.nucleus.Manager.Annotation.Router;
import cn.devspace.nucleus.Manager.RouteManager;
import cn.devspace.ribosome.entity.Club;
import cn.devspace.ribosome.entity.ClubUser;
import cn.devspace.ribosome.entity.Route;
import cn.devspace.ribosome.entity.User;
import cn.devspace.ribosome.error.errorManager;
import cn.devspace.ribosome.error.errorType;
import cn.devspace.ribosome.manager.user.userUnit;
import cn.devspace.ribosome.units.ClubUnits;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class routes extends RouteManager {

    @Router("getRoutes")
    public Object getRoutes(Map<String, String> args){
        String[] params = {"token"};
        if (!checkParams(args,params)) return errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);
        User user = userUnit.getUserByToken(args.get("token"));
        if (user==null) return errorManager.newInstance().catchErrors(errorType.Illegal_Permission);

        // TODO: 2023/2/9 先预备获取列表, 以便后期拓展
        List<String > roles = permissionManager.permissionManager.getPermissionList(user.getPermissionToken());
        if (roles==null) return errorManager.newInstance().catchErrors(errorType.Illegal_Permission);

        Map<String,Object> map = new HashMap<>();
        List<Route> data = new ArrayList<>();

        if (roles.contains("admin")) {
            return data.add(admin());
        }else {
            List<ClubUser> clubs = userUnit.getClubByUID(user.getUid());
            data.add(home());
            data.add(my());
            data.add(user());
            if (clubs!=null) data.add(club(clubs));
            data.add(settings());
        }
        map.put("data",data);
        return map;
    }

    public Route club(List<ClubUser> clubs){
        // 定义社团路由
        // 社团主路由
        Route club = new Route();
        club.setName("club");
        club.setPath("/club");
        // 社团子路由
        List<Route> clubChildren = new ArrayList<>();
        // 社团列表
        Route clubList = new Route();
        // 社团首页
        Route clubIndex = new Route();
        clubList.setName("club_List");
        clubList.setPath("/club/list");
        clubList.setComponent("./club/ClubList");
        clubIndex.setName("club_Index");
        clubIndex.setPath("/club/index");
        clubIndex.setComponent("./club/ClubIndex");
        clubChildren.add(clubIndex);
        clubChildren.add(clubList);

        // 加入的社团
        List<Route> clubIndexChildren = new ArrayList<>();
        Route clubIndexHome = new Route();
        clubIndexHome.setPath("/club/detail/:id");
        for (ClubUser club1 : clubs) {
            Route route = new Route();
            route.setName(ClubUnits.getClubByCid(club1.getCid()).getName());
            route.setPath("/club/detail/"+club1.getCid());
            clubIndexChildren.add(route);
        }
        clubIndexChildren.add(clubIndexHome);
        clubIndex.setRoutes(clubIndexChildren);
        club.setRoutes(clubChildren);
        return club;
    }


    public Route admin(){
        Route admin = new Route();
        admin.setName("admin");
        admin.setPath("/admin");
        List<Route> adminChildren = new ArrayList<>();
        Route dashboard = new Route();
        dashboard.setName("dashboard");
        dashboard.setPath("/admin/dashboard");
        dashboard.setComponent("./admin/DashboardAnalysis");
        Route clubManager = new Route();
        clubManager.setName("club-manager");
        clubManager.setPath("/admin/club-manager");
        clubManager.setComponent("./admin/ClubManager");
        Route activityManager = new Route();
        activityManager.setName("activity-manager");
        activityManager.setPath("/admin/activity-manager");
        activityManager.setComponent("./admin/ActivityManager");
        adminChildren.add(clubManager);
        adminChildren.add(activityManager);
        adminChildren.add(dashboard);
        admin.setRoutes(adminChildren);
        return admin;
    }

    public Route my(){
        Route home = new Route();
        home.setName("个人中心");
        home.setPath("/accountcenter");
        home.setComponent("./AccountCenter");
        return home;
    }

    public Route settings(){
        Route home = new Route();
        home.setName("个人设置");
        home.setPath("/accountsettings");
        home.setComponent("./AccountSettings");
        return home;
    }

    public Route home(){
        Route home = new Route();
        home.setName("welcome");
        home.setPath("/welcome");
        home.setComponent("./Welcome");
        return home;
    }

    public Route user(){
        Route user = new Route();
        user.setPath("/user");
        user.setLayout(false);
        Route login = new Route();
        login.setName("login");
        login.setPath("/user/login");
        login.setComponent("./user/Login");
        List<Route> userChildren = new ArrayList<>();
        userChildren.add(login);
        user.setRoutes(userChildren);
        return user;
    }



}
