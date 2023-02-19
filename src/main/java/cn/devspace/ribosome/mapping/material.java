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

package cn.devspace.ribosome.mapping;

import cn.devspace.nucleus.Manager.Annotation.Router;
import cn.devspace.nucleus.Manager.RouteManager;
import cn.devspace.nucleus.Message.Log;
import cn.devspace.ribosome.entity.User;
import cn.devspace.ribosome.entity.material.ClassroomArrangement;
import cn.devspace.ribosome.entity.material.ClassroomAvailable;
import cn.devspace.ribosome.error.errorManager;
import cn.devspace.ribosome.error.errorType;
import cn.devspace.ribosome.manager.database.MapperManager;
import cn.devspace.ribosome.manager.message.messageManager;
import cn.devspace.ribosome.manager.permission.action.clubActionType;
import cn.devspace.ribosome.manager.user.userUnit;
import cn.devspace.ribosome.units.ClubUnits;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.hibernate.jpa.internal.util.LogHelper;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 教室管理
 */
public class material extends RouteManager {



    @Router("material/classroom/arrangement")
    public Object getApplicationList(Map<String,String> args){
        String[] param = {"cid","token"};
        if(!checkParams(args,param)) return errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);
        User user = userUnit.getUserByToken(args.get("token"));
        if(user == null) return errorManager.newInstance().catchErrors(errorType.Callback_Login_Token_Error);
        if(!ClubUnits.checkPermission(args.get("cid"),String.valueOf(user.getUid()),clubActionType.CREATE_MATERIAL))
            return errorManager.newInstance().catchErrors(errorType.Illegal_Permission);
        List<ClassroomArrangement> list = MapperManager.newInstance().classroomArrangementBaseMapper.selectList(new QueryWrapper<ClassroomArrangement>().eq("clubId",args.get("cid")));
        for (ClassroomArrangement classroomArrangement : list) {
            String[] time = classroomArrangement.getTime().split(",");
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < time.length; i++) {
                String[] date = time[i].split("-");
                String label  = date[0]+"-"+date[1]+"-"+date[2]+"-"+formatLabel(date[3]);
                builder.append(label);
                if (i != time.length - 1) builder.append(",");
            }
            classroomArrangement.setTime(builder.toString());
            classroomArrangement.setCn(classroomArrangement.getCn());
        }
        return ResponseObject(200,1,"success",list);
    }


    /**
     * 新的教室申请
     * New classroom application
     * @param args cid, token
     * @return null
     */
    @Router("material/newClassroom")
    public Object newClassroom(Map<String,String> args) {
        String[] params = {"cid", "token", "reason", "data"};
        if (!checkParams(args, params)) return errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);
        // 检查是否有权限
        // Check if you have permission
        User user = userUnit.getUserByToken(args.get("token"));
        if (user == null) return errorManager.newInstance().catchErrors(errorType.Callback_Login_Token_Error);
        if (!ClubUnits.checkPermission(args.get("cid"), String.valueOf(user.getUid()), clubActionType.CREATE_MATERIAL))
            return errorManager.newInstance().catchErrors(errorType.Illegal_Permission);

        // data 的格式为 2023-02-12,1,8+2023-02-13,1,8+2023-02-14,1,8
        // 需要将其转换为列表
        Object result = getClassroomArrangement(args.get("data"), args.get("reason"), String.valueOf(user.getUid()), args.get("cid"));
        // 如果是列表
        try{
            if (result instanceof List) {
                List<ClassroomArrangement> list = (List<ClassroomArrangement>) result;
                for (ClassroomArrangement classroomArrangement : list) {
                    MapperManager.newInstance().classroomArrangementBaseMapper.insert(classroomArrangement);
                }
                messageManager.newClassroomApplication(user.getUid().toString(),true,false);
            }
            // 如果是单个
            if (result instanceof ClassroomArrangement) {
                MapperManager.newInstance().classroomArrangementBaseMapper.insert((ClassroomArrangement) result);
                messageManager.newClassroomApplication(user.getUid().toString(),false,false);
            }
            return ResponseString(200,1,"success");
        }catch (Exception e){
            return errorManager.newInstance().catchErrors(errorType.Database_Error);
        }
    }

    /**
     * 获取教室申请
     * Get the classroom application
     * @param data cid, token
     * @param reason reason
     * @param uid uid
     * @param cids  club id
     * @return  null
     */
    private Object getClassroomArrangement(String data, String reason, String uid, String cids){
        String[] dataArr = data.split("\\+");
        // 用于存储时间于教室的对应关系
        // 教室id->时间列表
        Map<String, List<String>> timeMap = new HashMap<>();
        for (String s : dataArr) {
            String[] sArr = s.split(",");
            String cid = sArr[1];
            if (timeMap.containsKey(cid)){
                timeMap.get(cid).add(sArr[0]+"-"+sArr[2]);
            }else {
                List<String> timeList = new ArrayList<>();
                timeList.add(sArr[0]+"-"+sArr[2]);
                timeMap.put(cid, timeList);
            }
        }
        if (timeMap.size()>1){
            List<ClassroomArrangement> list = new ArrayList<>();
            for (String s : timeMap.keySet()) {
                list.add(singleClassroomArrangement(Long.valueOf(s), formatTimeList(timeMap.get(s)), timeMap.get(s), reason, uid, cids));
            }
            return list;
        }else {
            // 当只选择了一个教室时
            ClassroomArrangement classroomArrangement = null;
            // 此时确保timeMap只有一个元素
           for (String s : timeMap.keySet()) {
               List<String> timeList = timeMap.get(s);
               classroomArrangement = singleClassroomArrangement(Long.valueOf(s), formatTimeList(timeList), timeMap.get(s), reason, uid, cids);
           }
           return classroomArrangement;

        }
    }

    private ClassroomArrangement singleClassroomArrangement(Long cid,String day, List<String> time, String reason, String uid, String cids){
        String timeFormat = time.toString();
        timeFormat = timeFormat.replace("[", "");
        timeFormat = timeFormat.replace("]", "");
        timeFormat = timeFormat.replace(" ", "");
        ClassroomArrangement classroomArrangement = new ClassroomArrangement();
        classroomArrangement.setCid(cid);
        classroomArrangement.setTime(timeFormat);
        classroomArrangement.setDay(day);
        classroomArrangement.setReason(reason);
        classroomArrangement.setUid(uid);
        classroomArrangement.setClubId(cids);
        return classroomArrangement;
    }

    private String formatTimeList(List<String> timeList){
        StringBuilder sb = new StringBuilder();
        for (String s : timeList) {
            String[] sArr = s.split("-");
            String day = sArr[0]+"-"+sArr[1]+"-"+sArr[2];
            sb.append(day).append(",");
            // 如果是最后一个元素
            if (timeList.indexOf(s) == timeList.size()-1){
                sb.deleteCharAt(sb.length()-1);
            }
        }
        return sb.toString();
    }


    /**
     * 获取教室可用时间
     * Get the available time of the classroom
     * @param args cid, token
     * @return List<ClassroomAvailable>
     */
    @Router("material/getClassroomAvailable")
    public Object getClassroomAvailable(Map<String,String> args){
        String[] params = {"cid","token"};
        if (!checkParams(args, params)) return errorManager.newInstance().catchErrors(errorType.Illegal_Parameter);
        // 检查是否有权限
        // Check if you have permission
        User user = userUnit.getUserByToken(args.get("token"));
        if (user == null) return errorManager.newInstance().catchErrors(errorType.Callback_Login_Token_Error);
        if (!ClubUnits.checkPermission(args.get("cid"), String.valueOf(user.getUid()), clubActionType.CREATE_MATERIAL)) return  errorManager.newInstance().catchErrors(errorType.Illegal_Permission);

        List<ClassroomAvailable> list = MapperManager.newInstance().classroomAvailableBaseMapper.selectList(new QueryWrapper<ClassroomAvailable>().eq("isAvailable", 1));
        // 只要是正在申请的, 都先锁定
        List<ClassroomArrangement> list1 = MapperManager.newInstance().classroomArrangementBaseMapper.selectList(new QueryWrapper<ClassroomArrangement>().eq("status", 1).eq("status", 0));

        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        // 每天固定的不可用时间
        Integer[] unavailable = {0,1,2,3,4,5,6,22,23};

        // 返回的结果
        List<Object> result = new ArrayList<>();
        // 只返回接下来一周的空闲教室
        result = getAvailableClassroom(calendar, simpleDateFormat, list, list1, unavailable);
        return result;
    }

    /**
     * 获取可用教室
     * Get available classrooms
     * @param calendar 日期
     * @param simpleDateFormat 日期格式
     * @param classroomAvailable 可用教室
     * @param classroomArrangement 教室安排
     * @param unavailable 不可用时间
     * @return 可用教室
     */
    public List<Object> getAvailableClassroom(Calendar calendar, SimpleDateFormat simpleDateFormat,List<ClassroomAvailable> classroomAvailable, List<ClassroomArrangement>   classroomArrangement, Integer[] unavailable){

        List<Object> result = new ArrayList<>();
        // 循环时间
        for (int i = 0; i < 7; i++) {
            //提前一天
            calendar.add(Calendar.DATE, 1);
            String dateStr = simpleDateFormat.format(calendar.getTime());
            // 初始化这一天的数据
            Map<String,Object> map = new HashMap<>();
            map.put("label",dateStr);
            map.put("value",dateStr);
            List<Object> availableRoom = new ArrayList<>();
            // 遍历今天可用的教室
            for (ClassroomAvailable classroomAvailable1 : classroomAvailable) {
                if (classroomAvailable1.getUnavailableDay() != null && classroomAvailable1.getUnavailableDay().contains(dateStr)) continue;
                Map<String,Object> maps = new HashMap<>();
                maps.put("label",classroomAvailable1.getClassroomName());
                maps.put("value",classroomAvailable1.getCid());
                // 这个教室的可用时间
                Integer[] available = getAvailable(classroomAvailable1.getAvailableTime());
                // 转为列表
                List<Integer> availableList = new ArrayList<>();
                for (Integer integer : available) {
                    availableList.add(integer);
                }
                // 循环遍历所有的教室安排, 如果教室安排的教室和可用教室一致, 则将该教室的不可用时间从可用时间中去除
                for (ClassroomArrangement classroomArrangement1 : classroomArrangement) {
                    List<String> lists = formatDate(classroomArrangement1.getDay());
                    if (!lists.contains(dateStr)) continue;

                    if (classroomAvailable1.getCid().equals(classroomArrangement1.getCid())){
                        // 获取这个预约的占用时间
                        Map<String, Object> unavailable1 = getUnavailable(classroomArrangement1.getTime());

                        // 如果预约中某个元素是本日期
                        if (unavailable1.containsKey(dateStr)){
                            // 获取这个时间段
                            List<Integer> unavailableList = (List<Integer>) unavailable1.get(dateStr);
                            Integer[] unavailable2 = new Integer[unavailableList.size()];
                            for (int j = 0; j < unavailableList.size(); j++) {
                                unavailable2[j] = unavailableList.get(j);
                            }
                            // 将这个时间段和固定的时间段合并
                            Integer[] unavailable3 = new Integer[unavailable.length + unavailable2.length];
                            System.arraycopy(unavailable, 0, unavailable3, 0, unavailable.length);
                            System.arraycopy(unavailable2, 0, unavailable3, unavailable.length, unavailable2.length);
                            // 排序
                            Arrays.sort(unavailable3);
                            // 去重
                            List<Integer> list = new ArrayList<>();
                            for (Integer integer : unavailable3) {
                                if (!list.contains(integer)){
                                    list.add(integer);
                                }
                            }
                            // 转换为数组
                            Integer[] unavailable4 = new Integer[list.size()];
                            for (int j = 0; j < list.size(); j++) {
                                unavailable4[j] = list.get(j);
                            }
                            // 将不可用时间从可用时间中去除
                            for (Integer integer : unavailable4) {
                                if (availableList.contains(integer)){
                                    availableList.remove(integer);
                                }
                            }
                        }
                    }
                }
                List<Object> list = new ArrayList<>();
                // 将可用时间格式化
                for(Integer integer : availableList){
                    Map<String,Object> maps1 = new HashMap<>();
                    maps1.put("label",formatLabel(integer.toString()));
                    maps1.put("value",integer);
                    list.add(maps1);
                }
                maps.put("children",list);
                availableRoom.add(maps);
            }
            map.put("children",availableRoom);
            result.add(map);
        }
        return result;
    }


    // 处理标签格式化的函数
    // 例如: 8 格式化为8:00-9:00
    public String formatLabel(String str){
        return str + ":00-" + (Integer.parseInt(str) + 1) + ":00";
    }



    // 处理日期格式化的函数
    // 例如: 2020-01-01,2020-01-02,2020-01-03, 2020-01-04,2020-01-05,2020-01-06,2020-01-07 格式化为List<String>
    public List<String> formatDate(String str){
        String[] dates = str.split(",");
        List<String> list = new ArrayList<>();
        for (String date : dates) {
            list.add(date);
        }
        return list;
    }

    /**
     * 获取不可用时间
     * @param str 例如: 2023-02-14-9, 2023-02-14-10, 2023-02-14-11
     * @return Integer[]
     */
    public Map<String , Object> getUnavailable(String str){
        String[] dates = str.split(",");
        // 2023-02-14-9
        Map<String , Object> map = new HashMap<>();
        for (String s : dates) {
            String[] date = s.split("-");
            // 0: 2023, 1: 02, 2: 14 3: 9\
            String day = date[0] + "-" + date[1] + "-" + date[2];
            // 构造字符串
            StringBuilder sb = new StringBuilder();
            sb.append(date[0]).append("-").append(date[1]).append("-").append(date[2]);
            String day1 = sb.toString();
            if (map.containsKey(day1)) {
                List<Integer> list = (List<Integer>) map.get(day);
                list.add(Integer.valueOf(date[3]));
            } else {
                List<Integer> list = new ArrayList<>();
                list.add(Integer.valueOf(date[3]));
                map.put(day1, list);
            }
        }
        return map;
    }

    /**
     * 获取可用时间
     * @param str 例如: 7,8,9,10,11,12,13,14,15,16,17,18,19,20,21
     * @return Integer[]
     */
    public Integer[] getAvailable(String str){
        String[] dates = str.split(",");
        Integer[] list = new Integer[dates.length];
        for (int i = 0; i < dates.length; i++) {
            list[i] = Integer.valueOf(dates[i]);
        }
        return list;
    }


}
