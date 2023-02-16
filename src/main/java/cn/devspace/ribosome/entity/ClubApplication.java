/*
 *
 *   .______       __  .______     ______        _______.  ______   .___  ___.  _______
 *   |   _  \     |  | |   _  \   /  __  \      /       | /  __  \  |   \/   | |   ____|
 *   |  |_)  |    |  | |  |_)  | |  |  |  |    |   (----`|  |  |  | |  \  /  | |  |__
 *   |      /     |  | |   _  <  |  |  |  |     \   \    |  |  |  | |  |\/|  | |   __|
 *   |  |\  \----.|  | |  |_)  | |  `--'  | .----)   |   |  `--'  | |  |  |  | |  |____
 *   | _| `._____||__| |______/   \______/  |_______/     \______/  |__|  |__| |_______|
 *
 *   CreateTime: 2023/2/5
 *   Author: Li JiaKe(Pama)
 */

package cn.devspace.ribosome.entity;

import cn.devspace.nucleus.Plugin.DataEntity;
import cn.devspace.ribosome.manager.user.userUnit;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Date;

@Data
@TableName("ribo_club_application")
@Entity
@Table(name = "ribo_club_application")
public class ClubApplication extends DataEntity {
        /**
         * 主键
         */
        @TableId(type = IdType.AUTO)
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long aid;

        /**
         * 用户id
         */
        private String uid;

        /**
         * 社团id
         */
        private String cid;

        /**
         * 申请理由
         */
        private String reason;

        /**
         * 费用
         */
        private String fee;

        /**
         * 是否通过
         */
        @Column(columnDefinition = "int default 0")
        private Integer status;

        /**
         * 批准的人
         */
        private String approved;

        /**
         * 更新时间
         */
        @UpdateTimestamp
        private Date updateTime;

        /**
         * 创建时间
         */
        @CreationTimestamp
        private Date createTime;

        /**
         * 是否删除(0-未删, 1-已删)
         */
        @Column(columnDefinition = "int default 0")
        private Integer isDeleted;

        @TableField(exist = false)
        private String username;

        public String getUsername() {
            return userUnit.getUserByUID(uid).getName();
        }

    }

