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

import java.util.*;

public class routes extends RouteManager {

    @Router("getRoutes")
    public Object getRoutes(Map<String, String> args){
        String[] params = {"token"};
        if (!checkParams(args,params)) return errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);
        User user = userUnit.getUserByToken(args.get("token"));
        if (user==null) {
            Map<String,Object> defaultMap = new HashMap<>();
            List<Route> data = new ArrayList<>();
                data.add(home());
                data.add(my());
                data.add(user());
                data.add(settings());
            defaultMap.put("data",data);
            return defaultMap;
        }
        // TODO: 2023/2/9 先预备获取列表, 以便后期拓展
        List<String > roles = permissionManager.permissionManager.getPermissionList(user.getPermissionToken());
        if (roles==null) return errorManager.newInstance().catchErrors(errorType.Illegal_Permission);

        Map<String,Object> map = new HashMap<>();
        List<Route> data = new ArrayList<>();

        if (roles.contains("admin")) {
            data.add(admin());
            data.add(home());
        }else {
            List<ClubUser> clubs = userUnit.getClubByUID(user.getUid());
            data.add(home());
            data.add(my());
            data.add(user());
            if (clubs!=null) data.add(club(clubs,user));
            data.add(settings());
        }
        map.put("data",data);
        return map;
    }

    /**
     * 社团路由
     * Club's route
     * @param clubs
     * @param user
     * @return
     */
    public Route club(List<ClubUser> clubs, User user){
        // 定义社团路由
        // 社团主路由
        Route club = new Route();
        club.setName("Club");
        club.setIcon("club");
        club.setPath("/club");
        // 社团子路由
        List<Route> clubChildren = new ArrayList<>();
        // 社团列表
        Route clubList = new Route();
        // 社团首页
        Route clubIndex = new Route();
        clubList.setName("Club List");
        clubList.setPath("/club/list");
        clubList.setComponent("./club/ClubList");
        clubIndex.setName("Club Center");
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
            Club clubEntity = ClubUnits.getClubByCid(club1.getCid());
            if (clubEntity==null) continue;

            route.setName(clubEntity.getName());
            route.setPath("/club/detail/"+club1.getCid());
            clubIndexChildren.add(route);
        }
        clubIndexChildren.add(clubIndexHome);
        clubIndex.setRoutes(clubIndexChildren);
        club.setRoutes(clubChildren);
        return club;
    }


    /**
     * 管理员路由
     * @return
     */
    public Route admin(){
        Route admin = new Route();
        admin.setName("Admin");
        admin.setIcon("admin");
        admin.setPath("/admin");
        List<Route> adminChildren = new ArrayList<>();
        // ======================================
        // 分析页
        Route dashboard = new Route();
        dashboard.setName("Dashboard");
        dashboard.setIcon("dashboard");
        dashboard.setPath("/admin/dashboard");
        dashboard.setComponent("./admin/DashboardAnalysis");

        // ======================================
        // 社团管理
        Route clubManager = new Route();
        clubManager.setName("Club Manager");
        clubManager.setIcon("club");
        clubManager.setPath("/admin/club-manager");
        clubManager.setComponent("./admin/ClubManager");

        // ======================================
        // 活动管理
        Route activityManager = new Route();
        activityManager.setName("Activity Manager");
        activityManager.setIcon("activity");
        activityManager.setPath("/admin/activity-manager");
        activityManager.setComponent("./admin/ActivityManager");

        // ======================================
        // 用户管理
        Route userManager = new Route();
        userManager.setName("User Manager");
        userManager.setIcon("user");
        userManager.setPath("/admin/user-manager");
        userManager.setComponent("./admin/UserManager");

        // ======================================
        // 物料管理
        Route materialManager = new Route();
        materialManager.setName("Material Manager");
        materialManager.setIcon("material");
        materialManager.setPath("/admin/material-manager");
        materialManager.setComponent("./admin/MaterialManager");

        // ======================================
        // 访问日志
        Route accessLog = new Route();
        accessLog.setName("Access Log");
        accessLog.setIcon("log");
        accessLog.setPath("/admin/access-log");
        accessLog.setComponent("./admin/AccessLog");


        adminChildren.add(clubManager);
        adminChildren.add(userManager);
        adminChildren.add(activityManager);
        adminChildren.add(dashboard);
        adminChildren.add(materialManager);
        adminChildren.add(accessLog);

        admin.setRoutes(adminChildren);

        return admin;
    }

    public Route my(){
        Route home = new Route();
        home.setName("About Me");
        home.setPath("/accountcenter");
        home.setIcon("my");
        home.setComponent("./AccountCenter");
        return home;
    }

    public Route settings(){
        Route home = new Route();
        home.setName("Settings");
        home.setPath("/accountsettings");
        home.setIcon("settings");
        home.setComponent("./AccountSettings");
        return home;
    }

    public Route home(){
        Route home = new Route();
        home.setName("Welcome");
        home.setPath("/welcome");
        home.setIcon("home");
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
