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
import cn.devspace.ribosome.manager.message.messageManager;
import cn.devspace.ribosome.manager.permission.action.clubActionType;
import cn.devspace.ribosome.manager.permission.permissionType;
import cn.devspace.ribosome.manager.user.userUnit;
import cn.devspace.ribosome.units.ClubUnits;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;

import java.util.*;

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
        String[] params = {"cid", "token"};
        if (!checkParams(args, params)) return errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);

        // 检查是否是本社团成员
        User user = userUnit.getUserByToken(args.get("token"));
        if (user == null) return errorManager.newInstance().catchErrors(errorType.Illegal_Permission);
        if(!ClubUnits.isMember(args.get("cid"), String.valueOf(user.getUid()))) return errorManager.newInstance().catchErrors(errorType.Illegal_Permission);

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
        ApplicationInfo applicationInfo = MapperManager.newInstance().applicationInfoBaseMapper.selectById(args.get("cid"));
        ClubApplication clubApplication = new ClubApplication();
        clubApplication.setCid(applicationInfo.getCid().toString());
        clubApplication.setUid(uid);
        clubApplication.setFee(args.get("fee"));
        clubApplication.setReason(args.get("reason"));
        MapperManager.newInstance().clubApplicationBaseMapper.insert(clubApplication);
        return ResponseString(200,1,"success");
    }

    /**
     * 获取社团申请信息
     * Get club application information
     * @param args 传入POST参数 POST parameters
     * @return 返回社团申请信息 Club application information
     */
    @Router("getClubApplicationInfo")
    public Object getClubApplicationInfo(Map<String, String> args){
        String[] params = {"cid"};
        if (!checkParams(args, params)) return errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);
        ApplicationInfo applicationInfo = MapperManager.newInstance().applicationInfoBaseMapper.selectById(args.get("cid"));
        if (applicationInfo == null) return errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);

        return applicationInfo;
    }

    /**
     * 获取社团申请列表
     * Get club application list
     * @param args 传入POST参数 POST parameters
     * @return 返回社团申请列表 Club application list
     */
    @Router("getClubApplicationList")
    public Object getClubApplicationList(Map<String, String> args){
        String[] params = {"token", "cid"};
        if (checkParams(args, params)) errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);
        User user = userUnit.getUserByToken(args.get("token"));
        if (user == null) errorManager.newInstance().catchErrors(errorType.Callback_Login_Token_Error);
        // TODO: 2023/2/11 后期需要匹配准确的权限, 目前先匹配社长
        if (!ClubUnits.isPresident(String.valueOf(user.getUid()), args.get("cid"))) errorManager.newInstance().catchErrors(errorType.Illegal_Permission);

        Map<String,Object> data = new HashMap<>();
        List<ClubApplication> list = MapperManager.newInstance().clubApplicationBaseMapper.selectList(new QueryWrapper<ClubApplication>().eq("uid",user.getUid()));
        data.put("data",list);
        data.put("code",200);
        data.put("msg","success");
        return data;
    }

    /**
     * 获取社团公告
     * Get club announcement
     * @param args 传入POST参数 POST parameters
     * @return 返回社团公告信息 Club announcement information
     */
    @Router("getClubAnnouncement")
    public Object getClubAnnouncement(Map<String, String> args){
        String[] params = {"cid", "token"};
        if (!checkParams(args, params)) return errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);
        ClubAnnouncement announcements = MapperManager.newInstance().clubAnnouncementBaseMapper.selectOne(new QueryWrapper<ClubAnnouncement>().eq("cid", args.get("cid")).orderByDesc("anid").last("limit 1"));
        if (announcements == null) return ResponseString(200,0,"success");
        return ResponseObject(200,1,"success",announcements);
    }

    /**
     * 新增社团公告
     * Add a new club announcement
     * @param args 传入POST参数 POST parameters
     * @return 返回社团公告信息 Club announcement information
     */
    @Router("newClubAnnouncement")
    public Object newClubAnnouncement(Map<String, String> args){
        String[] params = {"cid", "token", "title", "content"};
        if (!checkParams(args, params)) return errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);
        User user = userUnit.getUserByToken(args.get("token"));
        if (user == null) return errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);

        // TODO: 2023/2/11 后期需要匹配准确的权限, 目前先匹配社长
        if (!ClubUnits.isPresident(String.valueOf(user.getUid()), args.get("cid"))) return errorManager.newInstance().catchErrors(errorType.Illegal_Permission);

        ClubAnnouncement announcement = new ClubAnnouncement();
        announcement.setCid(args.get("cid"));
        announcement.setTitle(args.get("title"));
        announcement.setContent(args.get("content"));
        announcement.setCreateUser(String.valueOf(user.getUid()));

        MapperManager.newInstance().clubAnnouncementBaseMapper.insert(announcement);
        return ResponseString(200,1,"success");
    }



    /**
     * 更新社团信息
     * Update club information
     * @param args 传入POST参数 POST parameters
     * @return 返回社团信息 Club information
     */
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

    @Router("getAllAction")
    public Object getAllAction(Map<String, String> args){
        // 遍历clubActionType中的所有动作
       List<Object> list = new ArrayList<>();
        for (clubActionType action : clubActionType.values()) {
            Map<String, Object> map = new HashMap<>();
            map.put("label", action.getDescription());
            map.put("value", action.toString());
            list.add(map);
        }
        return list;
    }

    @Router("newClubRole")
    public Object newClubRole(Map<String, String> args) {
        String[] params = {"cid", "token", "role", "action"};
        if (!checkParams(args, params)) return errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);

        try {
            String builder = args.get("action").replace("[", "").replace("]", "");
            String[] actionArray = builder.split(",");

            for (String action : actionArray) {
                // 检查动作是否存在
                if (!clubActionType.isset(action))
                    return errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);
                Log.sendLog(action);
            }
            // 检查操作者是否存在
            User operator = userUnit.getUserByToken(args.get("token"));
            if (operator == null) return errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);
            String permission = permissionManager.permissionManager.newPermission(actionArray);
            ClubRole clubRole = new ClubRole();
            clubRole.setCid(args.get("cid"));
            clubRole.setRole(args.get("role"));
            clubRole.setPermissionToken(permission);
            MapperManager.newInstance().clubRoleBaseMapper.insert(clubRole);
            return ResponseString(200,1,"success");
        } catch (Exception e) {
            e.printStackTrace();
            return errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);
        }
    }

    /**
     * 获取社团角色
     * Get club role
     * @param args 传入POST参数 POST parameters
     * @return 返回社团角色 Club role
     */
    @Router("getClubRole")
    public Object getClubRole(Map<String, String> args){
        String[] params = {"cid", "token"};
        if (!checkParams(args, params)) return errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);
        // 需要保证用户是社团成员
        // Need to ensure that the user is a member of the club
        User operator = userUnit.getUserByToken(args.get("token"));
        if (operator == null) return errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);
        if (!ClubUnits.isMember(args.get("cid"),String.valueOf(operator.getUid()))) return errorManager.newInstance().catchErrors(errorType.Illegal_Permission);
        List<ClubRole> clubRoles = MapperManager.newInstance().clubRoleBaseMapper.selectList(new QueryWrapper<ClubRole>().eq("cid", args.get("cid")));
        return clubRoles;
    }

    /**
     * 处理社团申请
     * Handle club application
     * @param args  传入POST参数 POST parameters
     * @return 返回社团成员列表 Club member list
     */
    @Router("handleApplication")
    public Object handleApplication(Map<String, String> args){
        String[] param = {"cid", "token", "aid", "status"};
        if (!checkParams(args, param)) return errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);
        // 查询操作者是否拥有权限
        User operator = userUnit.getUserByToken(args.get("token"));
        if (operator == null) return errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);
        if (!ClubUnits.checkPermission(args.get("cid"),String.valueOf(operator.getUid()),clubActionType.APPROVE_NEW_MEMBER)) return errorManager.newInstance().catchErrors(errorType.Illegal_Permission);

        // 检查申请的状态
        ClubApplication clubApplication = MapperManager.newInstance().clubApplicationBaseMapper.selectOne(new QueryWrapper<ClubApplication>().eq("aid", args.get("aid")));
        if (clubApplication == null) return errorManager.newInstance().catchErrors(errorType.APPLICATION_Not_Found);
        if (clubApplication.getStatus() != 0) return errorManager.newInstance().catchErrors(errorType.APPLICATION_Status);
        if (args.get("status").equals("1")){
            // 同意申请
            // Agree to apply
            clubApplication.setStatus(1);
            clubApplication.setApproved(String.valueOf(operator.getUid()));
            MapperManager.newInstance().clubApplicationBaseMapper.updateById(clubApplication);
            // 添加用户到社团成员列表
            // Add user to club member list
            ClubUser clubMember = new ClubUser();
            clubMember.setCid(args.get("cid"));
            clubMember.setUid(clubApplication.getUid());
            clubMember.setJoinReason(clubApplication.getReason());
            MapperManager.newInstance().clubUserBaseMapper.insert(clubMember);
            // 发送通知
            // Send notification
            messageManager.applyClubResult(clubApplication.getUid(),args.get("cid"),true);


            return ResponseString(200,1,"success");
        }else if (args.get("status").equals("2")){
            // 拒绝申请
            // Refuse to apply
            clubApplication.setStatus(2);
            clubApplication.setApproved(String.valueOf(operator.getUid()));
            MapperManager.newInstance().clubApplicationBaseMapper.updateById(clubApplication);
            // 发送通知
            // Send notification
            messageManager.applyClubResult(clubApplication.getUid(),args.get("cid"),false);
            return ResponseString(200,1,"success");
        }else {
            return errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);
        }
    }

    /**
     * 更新社团成员角色
     * Update club member role
     * @param args 传入POST参数 POST parameters
     * @return 返回社团成员列表 Club member list
     */
    @Router("updateClubUserRole")
    public Object updateClubUserRole(Map<String, String> args){
        String[] param = {"cid", "token", "uid", "rid"};
        if (!checkParams(args, param)) return errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);
        // 查询操作者是否拥有权限
        User operator = userUnit.getUserByToken(args.get("token"));
        if (operator == null) return errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);
        if (!ClubUnits.isPresident(args.get("cid"),String.valueOf(operator.getUid()))) return errorManager.newInstance().catchErrors(errorType.Illegal_Permission);
        // 检查是否存在这个角色
        ClubRole clubRole = MapperManager.newInstance().clubRoleBaseMapper.selectOne(new QueryWrapper<ClubRole>().eq("cid", args.get("cid")).eq("rid", args.get("rid")).last("limit 1"));
        if (clubRole == null) return errorManager.newInstance().catchErrors(errorType.ROLE_Not_Found);

        ClubUser clubUser = MapperManager.newInstance().clubUserBaseMapper.selectOne(new QueryWrapper<ClubUser>().eq("cid", args.get("cid")).eq("uid", args.get("uid")).last("limit 1"));
        if (clubUser == null) return errorManager.newInstance().catchErrors(errorType.USER_Not_Found);
        clubUser.setRole(args.get("rid"));
        MapperManager.newInstance().clubUserBaseMapper.updateById(clubUser);
        return ResponseString(200,1,"success");
    }




    private Object testList(){
        List<Club> clubs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Club club = new Club();
            club.setCid(Long.valueOf("1"));
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
            clubActivity.setAid(Long.valueOf("1"));
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
            clubUser.setJoinTime("2020-01-01");
            clubUser.setQuitTime("2020-01-01");
            clubUsers.add(clubUser);
        }

        return clubUsers;


    }



    private Object test(String cid){
        Club club = new Club();
        club.setCid(Long.valueOf(cid));
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
