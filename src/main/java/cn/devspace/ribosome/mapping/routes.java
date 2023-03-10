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
        data.add(home());
        data.add(user());
        data.add(club());
        data.add(my());
        data.add(settings());
        map.put("data",data);
        return map;
    }

    public Route club(){
        Route club = new Route();
        club.setName("club");
        club.setPath("/club");
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
        clubIndexHome.setPath("/club/detail/:id");
        for (int i=1;i<4;i++){
            Route route = new Route();
            route.setName("??????"+i);
            route.setPath("/club/detail/"+i);
            clubIndexChildren.add(route);
        }
        clubIndexChildren.add(clubIndexHome);
        clubIndex.setRoutes(clubIndexChildren);
        club.setRoutes(clubChildren);
        return club;
    }


    public Route my(){
        Route home = new Route();
        home.setName("????????????");
        home.setPath("/accountcenter");
        home.setComponent("./AccountCenter");
        return home;
    }

    public Route settings(){
        Route home = new Route();
        home.setName("????????????");
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
