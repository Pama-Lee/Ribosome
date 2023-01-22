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
import cn.devspace.nucleus.Units.ApiUnit;
import cn.devspace.ribosome.auth.authUnit;
import cn.devspace.ribosome.entity.Token;
import cn.devspace.ribosome.error.errorManager;
import cn.devspace.ribosome.error.errorType;
import cn.devspace.ribosome.manager.database.MapperManager;
import cn.devspace.ribosome.manager.languageManager;

import java.util.Map;

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
    @Router("auth/callback")
    public Object callBack(Map<String ,String > args) {
        // TODO: 需要匹配的参数
        String[] params = {"sign", "token", "uid", "time", "version"};
        if (!checkParams(args, params)) {
            return errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);
        }
        if (!authUnit.verifyCallbackSignature(args.get("sign"), args.get("time"))) {
            return errorManager.newInstance().catchErrors(errorType.Callback_Signature_Error);
        }
        try {
            // 实例化新的token实体
            Token token = new Token();
            token.setSign(args.get("sign"));
            token.setTime(System.currentTimeMillis());
            token.setToken(args.get("token"));
            token.setUid(Long.valueOf(args.get("uid")));

            MapperManager.manager.tokenBaseMapper.insert(token);
            return ResponseString(200,1, languageManager.translateMessage("Auth.Login.Success"));
        } catch (Exception e) {
            return errorManager.newInstance().catchErrors(errorType.Callback_Data_Error);
        }
    }

}
