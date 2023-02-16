/*
 *
 *   .______       __  .______     ______        _______.  ______   .___  ___.  _______
 *   |   _  \     |  | |   _  \   /  __  \      /       | /  __  \  |   \/   | |   ____|
 *   |  |_)  |    |  | |  |_)  | |  |  |  |    |   (----`|  |  |  | |  \  /  | |  |__
 *   |      /     |  | |   _  <  |  |  |  |     \   \    |  |  |  | |  |\/|  | |   __|
 *   |  |\  \----.|  | |  |_)  | |  `--'  | .----)   |   |  `--'  | |  |  |  | |  |____
 *   | _| `._____||__| |______/   \______/  |_______/     \______/  |__|  |__| |_______|
 *
 *   CreateTime: 2023/1/31
 *   Author: Li JiaKe(Pama)
 */

package cn.devspace.ribosome.entity;

import cn.devspace.nucleus.Plugin.DataEntity;
import cn.devspace.ribosome.manager.database.MapperManager;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.*;

@Data
@TableName("ribo_club_user")
@Entity
@Table(name = "ribo_club_user")
public class ClubUser extends DataEntity {

        @TableId(type = IdType.AUTO)
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long rid;
        private String cid;
        private String uid;
        @TableField(exist = false)
        private String username;
        private String role;
        private Integer status;
        private String joinTime;
        private String quitTime;
        private String joinReason;
        private String quitReason;

        @TableField(exist = false)
        private boolean isExist = true;

        @TableField(exist = false)
        private String roleName;

        public String getRoleName() {
            return MapperManager.manager.clubRoleBaseMapper.selectById(role).getRole();
        }

        public String getUsername() {
            String username = MapperManager.manager.userBaseMapper.selectById(uid).getName();
            if (username != null)
                return username;
            else{
                isExist = false;
               return "Unknown User";
            }
        }
}
