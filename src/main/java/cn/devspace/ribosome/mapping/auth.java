/*
 *
 * .______       __  .______     ______        _______.  ______   .___  ___.  _______
 * |   _  \     |  | |   _  \   /  __  \      /       | /  __  \  |   \/   | |   ____|
 * |  |_)  |    |  | |  |_)  | |  |  |  |    |   (----`|  |  |  | |  \  /  | |  |__
 * |      /     |  | |   _  <  |  |  |  |     \   \    |  |  |  | |  |\/|  | |   __|
 * |  |\  \----.|  | |  |_)  | |  `--'  | .----)   |   |  `--'  | |  |  |  | |  |____
 * | _| `._____||__| |______/   \______/  |_______/     \______/  |__|  |__| |_______|
 *
 * CreateTime: 2023/1/21
 * Author: Li JiaKe(Pama)
 */

package cn.devspace.ribosome.mapping;

import cn.devspace.nucleus.Manager.Annotation.Router;
import cn.devspace.nucleus.Manager.RouteManager;
import cn.devspace.nucleus.Message.Log;
import cn.devspace.ribosome.entity.User;
import cn.devspace.ribosome.error.errorManager;
import cn.devspace.ribosome.error.errorType;
import cn.devspace.ribosome.manager.languageManager;
import cn.devspace.ribosome.manager.user.userUnit;

import java.util.Map;
import java.util.Objects;

/**
 * 管理账号验证模块的路由
 * Manage routes for account verification modules
 */

public class auth extends RouteManager{

    /**
     * 与RootJam对接的回调接口
     * Callback interface connected with RootJam
     * @param args 传入的数据｜The incoming data
     * @return 返回响应给RootJam的数据|Returns a response to RootJam data
     */
    @Router("auth/loginToken")
    public Object callBack(Map<String ,String > args) {
        // TODO: 需要匹配的参数
        String[] params = {"token", "time", "version"};
        if (!checkParams(args, params)) {
            return errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);
        }
        try {
            Map<String ,Object> backMap = userUnit.requestToken(args.get("token"));
            if (backMap == null || !Objects.equals(String.valueOf(backMap.get("msg")), "success")){
                Log.sendLog(backMap.get("msg").toString());
                return errorManager.newInstance().catchErrors(errorType.Callback_Login_Token_Error);
            }
            User user = userUnit.getUserByopenID(String.valueOf(backMap.get("openid")));
            if (user == null) {
                return errorManager.newInstance().catchErrors(errorType.Callback_Login_Token_Error);
            }
            return ResponseString(200,1, languageManager.translateMessage("Auth.Login.Success"));
        } catch (Exception e) {
            Log.sendWarn(e.getMessage());
            return errorManager.newInstance().catchErrors(errorType.Callback_Data_Error);
        }
    }








}
