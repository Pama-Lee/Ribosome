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

package cn.devspace.ribosome.entity;

import cn.devspace.nucleus.Plugin.DataEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("ribo_user")
public class User extends DataEntity {
    @TableId(type = IdType.AUTO)
    private Long uid;
    private String openid;
    private String name;
    private String email;
    private String permissionToken;
    private String avatar;
    private String slogan;
}
