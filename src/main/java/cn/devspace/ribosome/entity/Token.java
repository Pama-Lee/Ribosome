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

import javax.persistence.*;
import java.util.Date;


@Data
@TableName("ribo_token")
@Entity
@Table(name = "ribo_token")
public class Token extends DataEntity {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tid;

    /**
     * 下发的token
     */
    private String token;

    /**
     * 用户ID
     */
    private Long uid;

    /**
     * 签名
     */
    @Column(columnDefinition = "int default 1")
    private int status;

    /**
     * 传入时间戳
     */
    private Long time;

    /**
     * 创建时间
     */
    @Column(columnDefinition = "datetime default CURRENT_TIMESTAMP")
    private Date createTime;

    /**
     * 是否删除(0-未删, 1-已删)
     */
    @Column(columnDefinition = "int default 0")
    private Integer isDeleted;

    /**
     * 更新时间
     */
    @Column(columnDefinition = "datetime default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP")
    private Date updateTime;


}
