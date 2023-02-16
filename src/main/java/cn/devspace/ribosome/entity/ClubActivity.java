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
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.*;

@Data
@TableName("ribo_club_activity")
@Entity
@Table(name = "ribo_club_activity")
public class ClubActivity extends DataEntity {
        @TableId(type = IdType.AUTO)
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long aid;
        private String cid;
        private String activity_name;
        private String activity_description;
        private String activity_logo;
        private String activity_type;
        private String activity_president;
        private String activity_time;
        private String activity_place;
        @Column(columnDefinition = "int default 0")
        private String activity_status;
        private String activity_member;
        private String activity_member_number;
        private String activity_member_limit;
}
