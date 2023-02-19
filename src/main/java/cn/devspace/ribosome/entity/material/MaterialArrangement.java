/*
 *
 *   .______       __  .______     ______        _______.  ______   .___  ___.  _______
 *   |   _  \     |  | |   _  \   /  __  \      /       | /  __  \  |   \/   | |   ____|
 *   |  |_)  |    |  | |  |_)  | |  |  |  |    |   (----`|  |  |  | |  \  /  | |  |__
 *   |      /     |  | |   _  <  |  |  |  |     \   \    |  |  |  | |  |\/|  | |   __|
 *   |  |\  \----.|  | |  |_)  | |  `--'  | .----)   |   |  `--'  | |  |  |  | |  |____
 *   | _| `._____||__| |______/   \______/  |_______/     \______/  |__|  |__| |_______|
 *
 *   CreateTime: 2023/2/20
 *   Author: Li JiaKe(Pama)
 */

package cn.devspace.ribosome.entity.material;

import cn.devspace.nucleus.Plugin.DataEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;

@Entity
@Table(name = "ribo_material_arrangement")
@TableName("ribo_material_arrangement")
@Data
public class MaterialArrangement extends DataEntity {

    @Id
    @TableId(type = com.baomidou.mybatisplus.annotation.IdType.AUTO)
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    private Long aid;

    private Long cid;
    private Long uid;
    private String mid;
    private String reason;

    @Column(columnDefinition = "int default 0")
    private Integer status;

    private Long approvedBy;

    private Timestamp approvedTime;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createTime;


}
