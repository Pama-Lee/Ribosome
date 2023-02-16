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
import cn.devspace.ribosome.units.ClubUnits;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@TableName("ribo_application_info")
@Entity
@Table(name = "ribo_application_info")
public class ApplicationInfo extends DataEntity {
    @TableId(type = IdType.AUTO)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long iid;
    private Long cid;
    private String logo;
    private String description;
    @Column(columnDefinition = "int default 0")
    private Integer visit;
    @CreationTimestamp
    private Date createTime;

    @TableField(exist = false)
    private String clubName;

    public String getClubName() {
        return ClubUnits.getClubByCid(String.valueOf(cid)).getName();
    }
}
