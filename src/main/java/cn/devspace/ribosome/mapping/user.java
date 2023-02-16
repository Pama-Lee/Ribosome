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

import cn.devspace.nucleus.App.Permission.unit.permissionManager;
import cn.devspace.nucleus.Manager.Annotation.Router;
import cn.devspace.nucleus.Manager.RouteManager;
import cn.devspace.ribosome.entity.ClubApplication;
import cn.devspace.ribosome.entity.ClubUser;
import cn.devspace.ribosome.entity.User;
import cn.devspace.ribosome.entity.UserMessage;
import cn.devspace.ribosome.error.errorManager;
import cn.devspace.ribosome.error.errorType;
import cn.devspace.ribosome.manager.database.MapperManager;
import cn.devspace.ribosome.manager.user.userUnit;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.*;

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
    @Router("user/getUserMessageList")
    public Object getUserMessageList(Map<String, String> args) {
        String[] params = {"token"};
        if (checkParams(args, params)) errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);
        User user = userUnit.getUserByToken(args.get("token"));
        if (user == null) errorManager.newInstance().catchErrors(errorType.Callback_Login_Token_Error);
        Map<String,Object> data = new HashMap<>();
        List<UserMessage> list = MapperManager.newInstance().userMessageBaseMapper.selectList(new QueryWrapper<UserMessage>().eq("uid", user.getUid()));
        for (UserMessage userMessage: list){
            if (userMessage.getStatus().equals("0"))
            {
                userMessage.setStatus("1");
                MapperManager.newInstance().userMessageBaseMapper.updateById(userMessage);
            }
        }
        data.put("data",list);
        data.put("code",200);
        data.put("msg","success");
        return data;
    }

    /**
     * 获取加入的社团列表
     * Get the list of joined clubs
     * @param args 传入POST参数 POST parameters
     * @return 返回社团列表 Club list
     */
    @Router("getUserClub")
    public Object getClubList(Map<String, String> args){
        String[] params = {"token"};
        if (!checkParams(args, params)) return errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);
        User user = userUnit.getUserByToken(args.get("token"));
        if (user == null) return errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);
        List<ClubUser> clubs = userUnit.getClubByUID(user.getUid());
        if (clubs == null) return ResponseString(200,0,"success");
        return clubs;
    }

    @Router("user/getClubApplicationList")
    public Object getClubApplicationList(Map<String, String> args){
        String[] params = {"token"};
        if (checkParams(args, params)) errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);
        User user = userUnit.getUserByToken(args.get("token"));
        if (user == null) errorManager.newInstance().catchErrors(errorType.Callback_Login_Token_Error);
        Map<String,Object> data = new HashMap<>();
        List<ClubApplication> list = MapperManager.newInstance().clubApplicationBaseMapper.selectList(new QueryWrapper<ClubApplication>().eq("uid",user.getUid()));
        List<Map<String,Object>> list1 = new ArrayList<>();
        for (ClubApplication clubApplication : list) {
            Map<String,Object> map = new HashMap<>();
            map.put("clubName",MapperManager.newInstance().clubBaseMapper.selectById(clubApplication.getCid()).getName());
            map.put("clubId",clubApplication.getCid());
            map.put("uid",clubApplication.getUid());
            map.put("status",clubApplication.getStatus());
            map.put("time",clubApplication.getCreateTime());
            map.put("approvedTime",clubApplication.getUpdateTime());
            list1.add(map);
        }
        data.put("data",list1);
        data.put("code",200);
        data.put("msg","success");
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
        Map<String ,Object> data = new HashMap<>();
        if (user == null) errorManager.newInstance().catchErrors(errorType.Callback_Login_Token_Error);
        List<String> permissions = permissionManager.permissionManager.getPermissionList(user.getPermissionToken());

        // 获取用户消息数量
        Map<String,Object> messageCountMap = new HashMap<>();
        Integer messageCount = MapperManager.newInstance().userMessageBaseMapper.selectCount(new QueryWrapper<UserMessage>().eq("uid",user.getUid()).eq("status",0));
        Integer applicationCount = MapperManager.newInstance().clubApplicationBaseMapper.selectCount(new QueryWrapper<ClubApplication>().eq("uid",user.getUid()));
        messageCountMap.put("messageCount",messageCount);
        messageCountMap.put("applicationCount",applicationCount);

        data.put("data",user);
        data.put("permission",permissions);
        data.put("messageCount",messageCountMap);
        data.put("code",200);
        data.put("msg","success");
        data.put("status","1");
        return data;
    }

    @Router("updateUserInfo")
    public Object updateUserInfo(Map<String, String> args){
        String[] params = {"token","name","phone"};
        if (checkParams(args, params)) errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);
        User user = userUnit.getUserByToken(args.get("token"));
        if (user == null) errorManager.newInstance().catchErrors(errorType.Callback_Login_Token_Error);
        user.setName(args.get("name"));
        user.setPhone(args.get("phone"));
        User newUser = userUnit.updateUser(user);
        return returnSuccess(newUser);
    }



    private Object returnSuccess(User newUser) {
        Map<String,Object> data = new HashMap<>();
        data.put("data",newUser);
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
