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

package cn.devspace.ribosome.error;

import cn.devspace.nucleus.Manager.ManagerBase;
import cn.devspace.ribosome.manager.languageManager;

/**
 * 错误处理管理器
 * Error handling manager
 */
public class errorManager extends ManagerBase {

    private static final errorManager errorManager = new errorManager();

    public static errorManager newInstance() {
        return errorManager;
    }

    public Object catchErrors(int code){
        return switch (code) {
            case errorType.Illegal_Parameter ->
                    ResponseString(code, -1, languageManager.translateMessage("Error.Illegal.parameter"));
            case errorType.Callback_Signature_Error ->
                    ResponseString(code, -1, languageManager.translateMessage("Error.Callback.Signature.Error"));
            case errorType.Callback_Data_Error ->
                    ResponseString(code,-1,languageManager.translateMessage("Error.Callback.Data.Error"));
            case errorType.Callback_Login_Token_Error ->
                    ResponseString(code,-1,languageManager.translateMessage("Error.Callback.Login.Token.Error"));
            case errorType.Illegal_Permission ->
                    ResponseString(code,-1,languageManager.translateMessage("Error.Illegal.Permission"));
            case errorType.APPLICATION_Already_Applied ->
                    ResponseString(code,-1,languageManager.translateMessage("Error.Application.Already.Applied"));

            case errorType.Unknown_Error -> UnknownError(code);
            default -> UnknownError(code);
        };
    }


    /**
     * 未知的错误编号
     * @param code 错误编号
     * @return 返回String类型的json
     */
    public String UnknownError(int code){
        return ResponseString(code,-1,languageManager.translateMessage("Error.Unknown.error"));
    }

}
