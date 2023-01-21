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

package cn.devspace.ribosome.auth;

public class authUnit {

    private final String Unit_Version = "0.0.2";

    /**
     * 验证回调签名
     * Version: 0.0.2
     * @param sign 回调sign
     * @param time 回调附带时间戳
     * @return 返回布尔值
     */
    public static boolean verifyCallbackSignature(String sign,String time){
            // TODO: 签名算法
        return true;
    }



    public String getUnit_Version() {
        return Unit_Version;
    }
}
