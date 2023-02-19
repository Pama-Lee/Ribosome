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

import cn.devspace.nucleus.App.VisitLobby.Entity.Visit;
import cn.devspace.nucleus.Manager.Annotation.DataMapper;
import cn.devspace.ribosome.entity.*;
import cn.devspace.ribosome.entity.Error;
import cn.devspace.ribosome.entity.material.*;
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

    @Resource
    public BaseMapper<ClubApplication> clubApplicationBaseMapper;

    @Resource
    public BaseMapper<Club> clubBaseMapper;

    @Resource
    public BaseMapper<ClubUser> clubUserBaseMapper;

    @Resource
    public BaseMapper<ClubActivity> clubActivityBaseMapper;

    @Resource
    public BaseMapper<UserMessage> userMessageBaseMapper;

    @Resource
    public BaseMapper<ApplicationInfo> applicationInfoBaseMapper;

    @Resource
    public BaseMapper<ClubRole> clubRoleBaseMapper;

    @Resource
    public BaseMapper<ClubAnnouncement> clubAnnouncementBaseMapper;

    @Resource
    public BaseMapper<ClassroomAvailable> classroomAvailableBaseMapper;

    @Resource
    public BaseMapper<ClassroomArrangement> classroomArrangementBaseMapper;

    @Resource
    public BaseMapper<Classroom> classroomBaseMapper;

    @Resource
    public BaseMapper<Visitor> visitBaseMapper;

    @Resource
    public BaseMapper<Error> errorBaseMapper;

    @Resource
    public BaseMapper<MaterialArrangement> materialArrangementBaseMapper;

    @Resource
    public BaseMapper<Material> materialBaseMapper;


    public static MapperManager manager;

    /**
     * 初始化MapperManager
     * 由于MapperManager是一个单例，所以在初始化时，将自身赋值给manager
     */
    @PostConstruct
    public void init(){
        manager = this;
    }

    public static MapperManager newInstance() {
        return manager;
    }

}
