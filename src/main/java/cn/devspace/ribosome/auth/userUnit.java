/*
 *
 *   .______       __  .______     ______        _______.  ______   .___  ___.  _______
 *   |   _  \     |  | |   _  \   /  __  \      /       | /  __  \  |   \/   | |   ____|
 *   |  |_)  |    |  | |  |_)  | |  |  |  |    |   (----`|  |  |  | |  \  /  | |  |__
 *   |      /     |  | |   _  <  |  |  |  |     \   \    |  |  |  | |  |\/|  | |   __|
 *   |  |\  \----.|  | |  |_)  | |  `--'  | .----)   |   |  `--'  | |  |  |  | |  |____
 *   | _| `._____||__| |______/   \______/  |_______/     \______/  |__|  |__| |_______|
 *
 *   CreateTime: 2023/1/23
 *   Author: Li JiaKe(Pama)
 */

package cn.devspace.ribosome.auth;

import cn.devspace.nucleus.Units.ApiUnit;
import cn.devspace.nucleus.Units.Unit;
import cn.devspace.ribosome.manager.api.apiEntity.RootJam;
import org.springframework.util.DigestUtils;

import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Authorization相关的User工具类
 */
public class userUnit {

    /**
     * 通过token获取用户数据
     * Obtain user data through token
     * @param token 传入token
     * @return 返回格式化json后的数据Map, 如果token失效或获取失败, 则返回Null|After returns formatted json data Map, if failure or access token fails,returns `Null`
     */
    public Map<String,Object> getUserDataByToken(String token){
        Map<Object,Object> formData = new HashMap<>(20);
        Long time = System.currentTimeMillis();
        String sign = DigestUtils.md5DigestAsHex((token+time).getBytes());
        formData.put("token", token);
        formData.put("time", time);
        formData.put("sign", sign);
        HttpResponse<String> response = ApiUnit.createPOST(RootJam.get_UserData_By_Token, formData);
        String body = response.body();
        return Unit.json2Map(body);
    }

}
