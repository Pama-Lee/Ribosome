/*
 *
 *   .______       __  .______     ______        _______.  ______   .___  ___.  _______
 *   |   _  \     |  | |   _  \   /  __  \      /       | /  __  \  |   \/   | |   ____|
 *   |  |_)  |    |  | |  |_)  | |  |  |  |    |   (----`|  |  |  | |  \  /  | |  |__
 *   |      /     |  | |   _  <  |  |  |  |     \   \    |  |  |  | |  |\/|  | |   __|
 *   |  |\  \----.|  | |  |_)  | |  `--'  | .----)   |   |  `--'  | |  |  |  | |  |____
 *   | _| `._____||__| |______/   \______/  |_______/     \______/  |__|  |__| |_______|
 *
 *   CreateTime: 2023/1/30
 *   Author: Li JiaKe(Pama)
 */

package cn.devspace.ribosome.mapping;

import cn.devspace.nucleus.App.Permission.unit.permissionManager;
import cn.devspace.nucleus.Manager.Annotation.Router;
import cn.devspace.nucleus.Manager.RouteManager;
import cn.devspace.nucleus.Message.Log;
import cn.devspace.ribosome.entity.*;
import cn.devspace.ribosome.error.errorManager;
import cn.devspace.ribosome.error.errorType;
import cn.devspace.ribosome.manager.database.MapperManager;
import cn.devspace.ribosome.manager.permission.permissionType;
import cn.devspace.ribosome.manager.user.userUnit;
import cn.devspace.ribosome.units.ClubUnits;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 社团相关接口
 * Club related interfaces
 */
public class club extends RouteManager {

    /**
     * 获取社团信息
     * Get club information
     * @param args 传入POST参数 POST parameters
     * @return 返回社团信息 Club information
     */
    @Router("getClub")
    public Object getClub(Map<String, String> args){
        String[] params = {"cid"};
        if (!checkParams(args, params)) return errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);
        Club club = MapperManager.newInstance().clubBaseMapper.selectById(args.get("cid"));
        if (club == null) return errorManager.newInstance().catchErrors(errorType.Illegal_Permission);
        return club;
    }

    /**
     * 获取社团成员
     * Get Club User
     * @param args 传入POST参数 POST parameters
     * @return 返回社团成员信息 Club User information
     */
    @Router("getClubUser")
    public Object getClubUser(Map<String, String> args){
        String[] params = {"cid"};
        if (!checkParams(args, params)) return errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);
        List<ClubUser> users = MapperManager.newInstance().clubUserBaseMapper.selectList(new QueryWrapper<ClubUser>().eq("cid", args.get("cid")));
        if (users == null) return errorManager.newInstance().catchErrors(errorType.Illegal_Permission);
        return users;
    }

    /**
     * 获取社团活动
     * Get Club Activity
     * @param args 传入POST参数 POST parameters
     * @return 返回社团活动信息 Club Activity information
     */
    @Router("getClubActivity")
    public Object getClubActivity(Map<String, String> args){
        String[] params = {"cid"};
        if (!checkParams(args, params)) return errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);
        return testActivity(args.get("cid"));
    }

    /**
     * 获取加入的社团列表
     * Get the list of joined clubs
     * @param args 传入POST参数 POST parameters
     * @return 返回社团列表 Club list
     */
    @Router("getUserClub")
    public Object getClubList(Map<String, String> args){
        String[] params = {"token"};
        if (!checkParams(args, params)) return errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);
        User user = userUnit.getUserByToken(args.get("token"));
        if (user == null) return errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);
        List<ClubUser> clubs = userUnit.getClubByUID(user.getUid());
        if (clubs == null) return ResponseString(200,0,"success");
        return clubs;
    }

    /**
     * 获取所有社团列表
     * Get the list of all clubs
     * @param args 传入POST参数 POST parameters
     * @return 返回社团列表 Club list
     */
    @Router("getClubList")
    public Object getAllClub(Map<String, String> args){
        List<Club> clubs = MapperManager.newInstance().clubBaseMapper.selectList(new QueryWrapper<Club>());
        if (clubs == null) return ResponseString(200,0,"success");
        return clubs;
    }

    /**
     * 新增新的社团申请
     * Add a new club application
     * @param args 传入POST参数 POST parameters
     * @return 返回社团申请信息 Club application information
     */
    @Router("newApplication")
    public Object newApplication(Map<String, String> args){
        String[] params = {"cid", "token","reason","fee"};
        if (!checkParams(args, params)) return errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);
        String uid = String.valueOf(userUnit.getUserByToken(args.get("token")).getUid());
        // 检查是否已经申请过
        List<ClubApplication> applications = MapperManager.newInstance().clubApplicationBaseMapper.selectList(new QueryWrapper<ClubApplication>().eq("uid", uid).eq("cid", args.get("cid")));
        if (applications.size() != 0) return errorManager.newInstance().catchErrors(errorType.APPLICATION_Already_Applied);

        ClubApplication clubApplication = new ClubApplication();
        clubApplication.setCid(args.get("cid"));
        clubApplication.setUid(uid);
        clubApplication.setFee(args.get("fee"));
        clubApplication.setReason(args.get("reason"));
        MapperManager.newInstance().clubApplicationBaseMapper.insert(clubApplication);
        return ResponseString(200,1,"success");
    }

    @Router("getClubApplicationInfo")
    public Object getClubApplicationInfo(Map<String, String> args){
        String[] params = {"cid"};
        if (!checkParams(args, params)) return errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);
        ApplicationInfo applicationInfo = MapperManager.newInstance().applicationInfoBaseMapper.selectById(args.get("cid"));
        if (applicationInfo == null) return errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);

        return applicationInfo;
    }

    @Router("updateClub")
    public Object updateClub(Map<String, String> args){
        String[] params = {"cid", "token"};
        if (!checkParams(args, params)) return errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);

        // 新建更改社团信息的对象
        Club club = MapperManager.newInstance().clubBaseMapper.selectById(args.get("cid"));
        if (club == null) return errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);

        // 检查操作者是否存在
        User operator = userUnit.getUserByToken(args.get("token"));
        if (operator == null) return errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);

        // 检查权限
        // 查询操作者在社团中的角色
        String role = ClubUnits.checkClubMember(args.get("cid"), String.valueOf(operator.getUid()));
        if (Objects.equals(role, permissionType.NO_PERMISSION)){
            // 判断是否是管理员
            if(!permissionManager.permissionManager.checkPermission(operator.getPermissionToken(), permissionType.PERMISSION_ADMIN)){
                return errorManager.newInstance().catchErrors(errorType.Illegal_Permission);
            }else{
                role = permissionType.PERMISSION_ADMIN;
            }
        }else {
            if(permissionManager.permissionManager.checkPermission(operator.getPermissionToken(), permissionType.PERMISSION_ADMIN)){
                role = permissionType.PERMISSION_ADMIN;
            }
        }


        // 只有社长和管理员才有权限修改社团社长
        if (args.get("president") != null){
            if (!Objects.equals(role, permissionType.PERMISSION_PRESIDENT) && !Objects.equals(role, permissionType.PERMISSION_ADMIN)) return errorManager.newInstance().catchErrors(errorType.Illegal_Permission);
            club.setPresident(Integer.valueOf(args.get("president")));
        }
        // 只有管理员才有权限更改社团名称
        if (args.get("name") != null){
            if (!Objects.equals(role, permissionType.PERMISSION_ADMIN)) return errorManager.newInstance().catchErrors(errorType.Illegal_Permission);
            club.setName(args.get("name"));
        }
        if (args.get("description") != null){
            club.setDescription(args.get("description"));
        }
        MapperManager.newInstance().clubBaseMapper.updateById(club);
        return ResponseString(200,1,"success");
    }


















    private Object testList(){
        List<Club> clubs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Club club = new Club();
            club.setCid("1");
            club.setName("社团名称");
            club.setDescription("社团描述");
            club.setLogo("社团图片");
            club.setType("社团类型");
            clubs.add(club);
        }
        return clubs;
    }


    private Object testActivity(String cid){
        // 生成测试{@link ClubActivity}对象数据
        // Generate test {@link ClubActivity} object data
        List<ClubActivity> clubActivities = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ClubActivity clubActivity = new ClubActivity();
            clubActivity.setCid(cid);
            clubActivity.setAid("1");
            clubActivity.setActivity_name("活动名称");
            clubActivity.setActivity_time("2020-01-01");
            clubActivity.setActivity_place("活动地点");
            clubActivity.setActivity_description("活动描述");
            clubActivity.setActivity_type("活动类型");
            clubActivity.setActivity_status("活动状态");
            clubActivity.setActivity_logo("活动图片");
            clubActivity.setActivity_president("活动负责人");
            clubActivity.setActivity_member_limit("活动人数限制");
            clubActivities.add(clubActivity);
        }
        return clubActivities;
    }

    private Object testUser(String cid){
        // 生成测试{@link ClubUser}对象数据
        // Generate test {@link ClubUser} object data
        List<ClubUser> clubUsers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ClubUser clubUser = new ClubUser();
            clubUser.setCid(cid);
            clubUser.setUid("1");
            clubUser.setUsername(clubUser.getUsername());
            clubUser.setRole("管理员");
            clubUser.setJoin_time("2020-01-01");
            clubUser.setQuit_time("2020-01-01");
            clubUsers.add(clubUser);
        }

        return clubUsers;


    }



    private Object test(String cid){
        Club club = new Club();
        club.setCid(cid);
        String[] clubs= {"羽毛球社","篮球社","舞蹈社","游泳社","潜水社"};
        String[] description = {"为热爱羽毛球运动的学生提供比赛与培训机会","培养学生篮球技能，举办校内外比赛","提供舞蹈培训，展示学生舞蹈才华","训练学生游泳技能，举办游泳比赛","提供潜水培训，引领学生领略海洋魅力"};
        String[] president = {"小明","小红","小刚","小李","小王"};
        String[] announcement = {"羽毛球社招新啦！","篮球社招新啦！","舞蹈社招新啦！","游泳社招新啦！","潜水社招新啦！"};

        int random = (int)(Math.random()*clubs.length);

        // 获取随机一个社团
        club.setName(clubs[random]);
        // 获取随机一个社长
        // 获取随机一个社团描述
        club.setDescription(description[random]);
        // 获取随机一个社团公告
        club.setAnnouncement(announcement[random]);
        club.setLogo("https://github.com/Pama-Lee/Nucleus/raw/main/img/Nucleus-1@0.25x.png");
        club.setType("1");
        return club;
    }


}
