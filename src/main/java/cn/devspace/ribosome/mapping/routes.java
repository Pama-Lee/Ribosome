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

import cn.devspace.nucleus.Manager.Annotation.Router;
import cn.devspace.nucleus.Manager.RouteManager;
import cn.devspace.ribosome.entity.Route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class routes extends RouteManager {

    @Router("getRoutes")
    public Object routes(Map<String, String> args){
        Map<String,Object> map = new HashMap<>();
        List<Route> data = new ArrayList<>();

        // 添加基本路由
        Route home = new Route();
        home.setName("welcome");
        home.setPath("/welcome");
        home.setIcon("smile");
        home.setComponent("./Welcome");

        Route user = new Route();
        user.setPath("/user");
        Route login = new Route();
        login.setName("login");
        login.setPath("/user/login");
        login.setComponent("./user/Login");
        Route notFound = new Route();
        notFound.setComponent("./404");
        List<Route> userChildren = new ArrayList<>();
        userChildren.add(login);
        userChildren.add(notFound);
        user.setRoutes(userChildren);


        // 添加社团路由
        Route club = new Route();
        club.setName("club");
        club.setPath("/club");
        club.setIcon("team");
        List<Route> clubChildren = new ArrayList<>();
        Route clubList = new Route();
        Route clubIndex = new Route();
        clubList.setName("club_List");
        clubList.setPath("/club/list");
        clubList.setComponent("./club/ClubList");
        clubIndex.setName("club_Index");
        clubIndex.setPath("/club/index");
        clubIndex.setComponent("./club/ClubIndex");
        clubChildren.add(clubIndex);
        clubChildren.add(clubList);

        List<Route> clubIndexChildren = new ArrayList<>();
        Route clubIndexHome = new Route();
        clubIndexHome.setPath("/club/:id");
        for (int i=0;i<10;i++){
            Route route = new Route();
            route.setName("社团"+i);
            route.setPath("/club/"+i);
            route.setComponent("./club");
            route.setIcon("team");
            clubIndexChildren.add(route);
        }
        clubIndex.setRoutes(clubIndexChildren);
        club.setRoutes(clubChildren);

        data.add(home);
        data.add(club);
        data.add(user);
        map.put("data",data);
       // 返回route.ts格式信

        return map;
    }

}
