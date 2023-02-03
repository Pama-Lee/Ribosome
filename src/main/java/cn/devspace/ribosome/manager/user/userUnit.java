/*
 *
 *   .______       __  .______     ______        _______.  ______   .___  ___.  _______
 *   |   _  \     |  | |   _  \   /  __  \      /       | /  __  \  |   \/   | |   ____|
 *   |  |_)  |    |  | |  |_)  | |  |  |  |    |   (----`|  |  |  | |  \  /  | |  |__
 *   |      /     |  | |   _  <  |  |  |  |     \   \    |  |  |  | |  |\/|  | |   __|
 *   |  |\  \----.|  | |  |_)  | |  `--'  | .----)   |   |  `--'  | |  |  |  | |  |____
 *   | _| `._____||__| |______/   \______/  |_______/     \______/  |__|  |__| |_______|
 *
 *   CreateTime: 2023/2/3
 *   Author: Li JiaKe(Pama)
 */

package cn.devspace.ribosome.manager.user;

import cn.devspace.nucleus.Message.Log;
import cn.devspace.ribosome.entity.User;
import cn.devspace.ribosome.manager.database.MapperManager;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class userUnit {

    /**
     * 通过openID获取用户信息
     * get user information by openID
     * @param openID 用户的OpenID｜User's OpenID
     * @return 返回用户信息｜Returns user information
     */
    public static User getUserByopenID(String openID) {
        Map<String ,Object> param = new HashMap<>(2);
        Map<String, Object> userInfo = requestUserInfo(openID);
        // 无法获取到用户信息 | Unable to get user information
        if (userInfo == null) {
            return null;
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("openid", openID);
        User user = MapperManager.newInstance().userBaseMapper.selectOne(queryWrapper);
        if (user == null) {
            User newUser = new User();
            newUser.setEmail(String.valueOf(userInfo.get("email")));
            newUser.setOpenid(openID);
            MapperManager.newInstance().userBaseMapper.insert(newUser);
            user = newUser;
        }
        return user;
    }

    /**
     * 通过Token获取用户信息
     * get user information by Token
     * @param token 用户的Token｜User's Token
     * @return 返回用户信息｜Returns user information
     */
    public static User getUserByToken(String token) {
        Map<String, Object> login = requestToken(token);
        if (login == null || !Objects.equals(String.valueOf(login.get("msg")), "success")){
            return null;
        }
        String openID = String.valueOf(login.get("openid"));
        return getUserByopenID(openID);
    }


    /**
     * 与RootJam对接的回调接口
     * Callback interface connected with RootJam
     * @param openID 传入的数据｜The incoming data
     * @return 返回响应给RootJam的数据|Returns a response to RootJam data
     */
    private static Map<String,Object> requestUserInfo(String openID) {
        Map<String ,Object> param = new HashMap<>(2);
        param.put("openid", openID);
        param.put("appid", "694873c0726514c1");
        String back = HttpUtil.post("https://api.pamalee.cn/User/UserInfo",param);
        Log.sendLog(back);
        Map<String ,Object> backMap = new Gson().fromJson(back, Map.class);
        if (backMap == null || !Objects.equals(String.valueOf(backMap.get("msg")), "success")){
            return null;
        }
        return backMap;
    }
    /**
     * 与RootJam对接的回调接口
     * Callback interface connected with RootJam
     * @param token 传入的数据｜The incoming data
     * @return 返回响应给RootJam的数据|Returns a response to RootJam data
     */
    public static Map<String ,Object> requestToken(String token) {
        Map<String ,Object> param = new HashMap<>(2);
        param.put("token", token);
        param.put("appid", "694873c0726514c1");
        String back = HttpUtil.post("https://api.pamalee.cn/Login/openID",param);
        Log.sendLog(back);
        Map<String ,Object> backMap = new Gson().fromJson(back, Map.class);
        return backMap;
    }
}
