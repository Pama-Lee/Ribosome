/*
 *
 *   .______       __  .______     ______        _______.  ______   .___  ___.  _______
 *   |   _  \     |  | |   _  \   /  __  \      /       | /  __  \  |   \/   | |   ____|
 *   |  |_)  |    |  | |  |_)  | |  |  |  |    |   (----`|  |  |  | |  \  /  | |  |__
 *   |      /     |  | |   _  <  |  |  |  |     \   \    |  |  |  | |  |\/|  | |   __|
 *   |  |\  \----.|  | |  |_)  | |  `--'  | .----)   |   |  `--'  | |  |  |  | |  |____
 *   | _| `._____||__| |______/   \______/  |_______/     \______/  |__|  |__| |_______|
 *
 *   CreateTime: 2023/2/12
 *   Author: Li JiaKe(Pama)
 */

package cn.devspace.ribosome.entity.material;

import cn.devspace.nucleus.Plugin.DataEntity;
import cn.devspace.ribosome.manager.database.MapperManager;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.Mapper;
import lombok.Data;

import java.util.Date;

@Data
@TableName("ribo_material_classroom_arrangement")
public class ClassroomArrangement extends DataEntity {
    @TableId(type = IdType.AUTO)
    private Long aid;
    private Long cid;
    // 格式为 2020-01-01-8,2020-01-01-9,2020-01-01-10

    private String clubId;

    private String time;
    // 格式: 2020-01-01,2020-01-02,2020-01-03
    private String day;
    private String reason;
    private String uid;
    private String status;
    private Date createTime;

    @TableField(exist = false)
    private String cn;

    public String getCn() {
        return MapperManager.newInstance().classroomBaseMapper.selectById(cid).getName();
    }
}
