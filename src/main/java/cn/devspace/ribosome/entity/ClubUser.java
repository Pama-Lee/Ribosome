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
import cn.devspace.ribosome.manager.database.DataBaseManager;
import cn.devspace.ribosome.manager.database.MapperManager;
import lombok.Data;

@Data
public class ClubUser extends DataEntity {

        private String cid;
        private String uid;
        private String username;
        private String role;
        private String status;
        private String join_time;
        private String quit_time;
        private String join_reason;
        private String quit_reason;

        private boolean isExist = true;

        public String getUsername() {
            String username = MapperManager.manager.userBaseMapper.selectById(uid).getUsername();
            if (username != null)
                return username;
            else{
                isExist = false;
               return "Unknown User";
            }
        }
}
