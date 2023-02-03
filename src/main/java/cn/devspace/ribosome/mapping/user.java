/*
 *
 *   .______       __  .______     ______        _______.  ______   .___  ___.  _______
 *   |   _  \     |  | |   _  \   /  __  \      /       | /  __  \  |   \/   | |   ____|
 *   |  |_)  |    |  | |  |_)  | |  |  |  |    |   (----`|  |  |  | |  \  /  | |  |__
 *   |      /     |  | |   _  <  |  |  |  |     \   \    |  |  |  | |  |\/|  | |   __|
 *   |  |\  \----.|  | |  |_)  | |  `--'  | .----)   |   |  `--'  | |  |  |  | |  |____
 *   | _| `._____||__| |______/   \______/  |_______/     \______/  |__|  |__| |_______|
 *
 *   CreateTime: 2023/2/2
 *   Author: Li JiaKe(Pama)
 */

package cn.devspace.ribosome.mapping;

import cn.devspace.nucleus.Manager.Annotation.Router;
import cn.devspace.nucleus.Manager.RouteManager;
import cn.devspace.ribosome.entity.User;
import cn.devspace.ribosome.entity.UserMessage;
import cn.devspace.ribosome.error.errorManager;
import cn.devspace.ribosome.error.errorType;
import cn.devspace.ribosome.manager.user.userUnit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户相关接口
 * User related interfaces
 */
public class user extends RouteManager {

    /**
     * 获取用户消息列表
     * Get user message list
     * @param args uid,token
     * @return List<UserMessage>
     */
    @Router("getUserMessageList")
    public Object getUserMessageList(Map<String, String> args) {
        String[] params = {"uid", "token"};
        if (checkParams(args, params)) errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);
        Map<String,Object> data = new HashMap<>();
        Map<String,Object> list = new HashMap<>();
        list.put("list",testMessage(args.get("uid")));
        data.put("data",list);
        return data;
    }


    /**
     * 获取用户信息
     * Get user information
     * @param args token
     * @return User
     */
    @Router("getUserInfo")
    public Object getUserInfo(Map<String, String> args){
        String[] params = {"token"};
        if (checkParams(args, params)) errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);
        User user = userUnit.getUserByToken(args.get("token"));
        if (user == null) errorManager.newInstance().catchErrors(errorType.Callback_Login_Token_Error);
        Map<String,Object> data = new HashMap<>();
        data.put("data",user);
        data.put("code",200);
        data.put("msg","success");
        data.put("status","1");
        return data;
    }


    private Object testMessage(String uid) {
        List<UserMessage> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            UserMessage message = new UserMessage();
            message.setUid(Long.valueOf(uid));
            message.setTitle("测试消息" + i);
            message.setContent("测试消息内容" + i);
            message.setStatus("0");
            list.add(message);
        }
        return list;
    }
}
