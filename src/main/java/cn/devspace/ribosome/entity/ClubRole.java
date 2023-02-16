/*
 *
 *   .______       __  .______     ______        _______.  ______   .___  ___.  _______
 *   |   _  \     |  | |   _  \   /  __  \      /       | /  __  \  |   \/   | |   ____|
 *   |  |_)  |    |  | |  |_)  | |  |  |  |    |   (----`|  |  |  | |  \  /  | |  |__
 *   |      /     |  | |   _  <  |  |  |  |     \   \    |  |  |  | |  |\/|  | |   __|
 *   |  |\  \----.|  | |  |_)  | |  `--'  | .----)   |   |  `--'  | |  |  |  | |  |____
 *   | _| `._____||__| |______/   \______/  |_______/     \______/  |__|  |__| |_______|
 *
 *   CreateTime: 2023/2/11
 *   Author: Li JiaKe(Pama)
 */

package cn.devspace.ribosome.entity;

import cn.devspace.nucleus.App.Permission.unit.permissionManager;
import cn.devspace.nucleus.Plugin.DataEntity;
import cn.devspace.ribosome.manager.permission.action.clubActionType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@TableName("ribo_club_role")
@Entity
@Table(name = "ribo_club_role")
public class ClubRole extends DataEntity {
    @TableId(type = IdType.AUTO)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rid;
    private String cid;
    private String role;
    private String permissionToken;
    @Column(columnDefinition = "datetime default CURRENT_TIMESTAMP")
    private Date createTime;

    /**
     * 用于存储权限列表
     * use for store permission list
     */
    @TableField(exist = false)
    @Transient
    private List<String> permission;

    /**
     * 用于存储权限列表
     *
     * @return
     */
    public List<String> getPermission() {
        List<String> list = permissionManager.permissionManager.getPermissionList(permissionToken);
        List<String> res = new ArrayList<>();
        for (String s : list) {
            clubActionType action = clubActionType.valueOf(s);
            res.add(action.getDescription());
        }
        return res;
    }

    public List<clubActionType> getPermissionList(){
        List<String> list = permissionManager.permissionManager.getPermissionList(permissionToken);
        List<clubActionType> res = new ArrayList<>();
        for (String s : list) {
            clubActionType action = clubActionType.valueOf(s);
            res.add(action);
        }
        return res;
    }
}
