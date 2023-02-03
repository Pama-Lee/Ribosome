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

import cn.devspace.nucleus.Manager.Annotation.Router;
import cn.devspace.nucleus.Manager.RouteManager;
import cn.devspace.ribosome.entity.Club;
import cn.devspace.ribosome.entity.ClubActivity;
import cn.devspace.ribosome.entity.ClubUser;
import cn.devspace.ribosome.error.errorManager;
import cn.devspace.ribosome.error.errorType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        return test(args.get("cid"));
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
        return testUser(args.get("cid"));
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

    @Router("getClubList")
    public Object getClubList(Map<String, String> args){
        return testList();
    }


    private Object testList(){
        List<Club> clubs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Club club = new Club();
            club.setCid("1");
            club.setClub_name("社团名称");
            club.setClub_description("社团描述");
            club.setClub_logo("社团图片");
            club.setClub_president("社团负责人");
            club.setClub_type("社团类型");
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
        club.setClub_name(clubs[random]);
        // 获取随机一个社长
        club.setClub_president(president[random]);
        // 获取随机一个社团描述
        club.setClub_description(description[random]);
        // 获取随机一个社团公告
        club.setAnnouncement(announcement[random]);
        club.setClub_logo("https://github.com/Pama-Lee/Nucleus/raw/main/img/Nucleus-1@0.25x.png");
        club.setClub_type("1");
        return club;
    }


}
