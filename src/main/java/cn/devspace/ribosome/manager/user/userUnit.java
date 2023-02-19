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
import cn.devspace.ribosome.entity.*;
import cn.devspace.ribosome.manager.message.messageManager;
import cn.devspace.ribosome.manager.database.MapperManager;
import cn.devspace.ribosome.manager.permission.permissionType;
import cn.devspace.ribosome.units.jsonUnits;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import org.springframework.util.DigestUtils;

import java.util.*;

import static cn.devspace.nucleus.App.Permission.unit.permissionManager.permissionManager;

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
        queryWrapper.eq("openid", openID).last("LIMIT 1");
        User user = MapperManager.newInstance().userBaseMapper.selectOne(queryWrapper);
        if (user == null) {
           return null;
        }else {
            // 检查用户信息是否有更新 | Check if user information has been updated
            if (!Objects.equals(user.getEmail(), String.valueOf(userInfo.get("email"))) || !Objects.equals(user.getAvatar(), String.valueOf(userInfo.get("avatar")))) {
                user.setAvatar(String.valueOf(userInfo.get("avatar")));
                user.setEmail(String.valueOf(userInfo.get("email")));
                MapperManager.newInstance().userBaseMapper.updateById(user);
            }
            user.setAvatar("https://api.pamalee.cn/"+user.getAvatar());
        }

        return user;
    }

    public static User getUserByUID(String uid) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", uid).last("LIMIT 1");
        User user = MapperManager.newInstance().userBaseMapper.selectOne(queryWrapper);
        if (user == null) {
            return null;
        }
        return user;
    }


    /**
     * 注册新用户
     * Register a new user
     * @param email 用户邮箱｜User's email
     * @param openID 用户的OpenID｜User's OpenID
     * @return 返回新用户信息｜Returns new user information
     */
    public static User register(String email, String openID){
        User newUser = new User();
        newUser.setEmail(String.valueOf(email));
        newUser.setOpenid(openID);
        // 默认权限
        newUser.setPermissionToken(permissionManager.newPermission(permissionType.PERMISSION_USER));
        // 检查该openid是否已经注册过
        User user = MapperManager.newInstance().userBaseMapper.selectOne(new QueryWrapper<User>().eq("openid", openID).last("LIMIT 1"));
        if (user != null) {
            return user;
        }
        MapperManager.newInstance().userBaseMapper.insert(newUser);


        // 发送欢迎邮件
        messageManager.welcomeNewUser(String.valueOf(newUser.getUid()));

        return newUser;
    }


    /**
     * 生成新的Token
     * @param user 用户信息｜User information
     * @return
     */
    public static String newLocalToken(User user) {
        String token = DigestUtils.md5DigestAsHex((new Date().getTime()+user.getOpenid()).getBytes());
        Token token1 = new Token();
        token1.setToken(token);
        token1.setStatus(1);
        token1.setUid(user.getUid());
        // 设置过期时间为1天
        token1.setTime(new Date().getTime()+86400000);
        MapperManager.newInstance().tokenBaseMapper.insert(token1);
        return token;
    }

    /**
     * 检查Token是否有效
     * Check if the Token is valid
     * @param token
     * @return
     */
    public static boolean verifyToken(String token) {
        Token tokenData = MapperManager.newInstance().tokenBaseMapper.selectOne(new QueryWrapper<Token>().eq("token", token).eq("status","1").last("LIMIT 1"));
        if (tokenData == null) {
            return false;
        }
        return tokenData.getTime() >= new Date().getTime();
    }


    /**
     * 通过Token获取用户信息
     * get user information by Token
     * @param token 用户的Token｜User's Token
     * @return 返回用户信息｜Returns user information
     */
    public static User getUserByToken(String token) {
        if (!verifyToken(token)) {
            return null;
        }
        Token tokenData = MapperManager.newInstance().tokenBaseMapper.selectOne(new QueryWrapper<Token>().eq("token", token).eq("status","1").last("LIMIT 1"));
        return getUserByUID(String.valueOf(tokenData.getUid()));
    }
    /**
     * 更新用户信息
     * Update user information
     * @param user 用户信息｜User information
     * @return  返回更新后的用户信息｜Returns updated user information
     */
    public static User updateUser(User user) {
        MapperManager.newInstance().userBaseMapper.updateById(user);
        return user;
    }

    /**
     * 通过uid获取用户所在的所有社团
     * get all clubs by uid
     * @param uid 用户uid | user uid
     * @return 返回用户所在的所有社团 | Returns all clubs where the user is located
     */
    public static List<ClubUser> getClubByUID(Long uid) {
        return MapperManager.newInstance().clubUserBaseMapper.selectList(new QueryWrapper<ClubUser>().eq("uid", uid));
    }

    /**
     * 与RootJam对接的回调接口
     * Callback interface connected with RootJam
     * @param openID 传入的数据｜The incoming data
     * @return 返回响应给RootJam的数据|Returns a response to RootJam data
     */
    public static Map<String,Object> requestUserInfo(String openID) {
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
        Map<String ,Object> backMap = new Gson().fromJson(back, Map.class);
        Log.sendLog(back);
        if (backMap == null){
            Log.sendLog("backMap is null");
            return null;
        }
        return backMap;
    }
}
