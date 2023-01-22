/*
 *
 * .______       __  .______     ______        _______.  ______   .___  ___.  _______
 * |   _  \     |  | |   _  \   /  __  \      /       | /  __  \  |   \/   | |   ____|
 * |  |_)  |    |  | |  |_)  | |  |  |  |    |   (----`|  |  |  | |  \  /  | |  |__
 * |      /     |  | |   _  <  |  |  |  |     \   \    |  |  |  | |  |\/|  | |   __|
 * |  |\  \----.|  | |  |_)  | |  `--'  | .----)   |   |  `--'  | |  |  |  | |  |____
 * | _| `._____||__| |______/   \______/  |_______/     \______/  |__|  |__| |_______|
 *
 * CreateTime: 2023/1/22
 * Author: Li JiaKe(Pama)
 */

package cn.devspace.ribosome.manager.database;

import cn.devspace.nucleus.Manager.Annotation.DataMapper;
import cn.devspace.ribosome.entity.Token;
import cn.devspace.ribosome.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * MyBatis-Plus管理的Mapper
 */
@DataMapper
public class MapperManager {

    @Resource
    public BaseMapper<Token> tokenBaseMapper;

    @Resource
    public BaseMapper<User> userBaseMapper;

    public static MapperManager manager;

    @PostConstruct
    public void init(){
        manager = this;
    }

    public static MapperManager newInstance() {
        return manager;
    }

}
