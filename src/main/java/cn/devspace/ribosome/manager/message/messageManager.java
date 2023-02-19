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

package cn.devspace.ribosome.manager.message;

import cn.devspace.ribosome.entity.Club;
import cn.devspace.ribosome.entity.UserMessage;
import cn.devspace.ribosome.manager.database.MapperManager;
import cn.devspace.ribosome.units.ClubUnits;

public class messageManager {


    /**
     *
     * @param uid
     * @param isList
     * @param isAdmin
     * @return
     */
    public static UserMessage newClassroomApplication(String uid, boolean isList, boolean isAdmin) {
        UserMessage message = new UserMessage();
        if (isAdmin){
            message.setTitle("新的教室申请");
            message.setContent("您有一条新的教室申请，请及时处理");
        }
        else{
            message.setTitle("教室申请已提交");
            if (isList) {
                message.setContent("您的教室申请已提交,由于你一次性选择了多个教室, 因此被分成了多个单独申请, 请耐心等待处理结果");
            } else {
                message.setContent("您的教室申请已提交，请耐心等待处理结果");
            }
        }
        message.setUid(Long.parseLong(uid));
        MapperManager.newInstance().userMessageBaseMapper.insert(message);
        return message;
    }

    /**
     * 申请结果通知
     * Inform the result of application
     * @param uid 用户ID
     * @param cid 社团ID
     * @param result 申请结果
     * @return 消息实体
     */
    public static UserMessage applyClubResult(String uid, String cid, boolean result) {

        Club club = ClubUnits.getClubByCid(cid);
        if (club == null)
            return null;
        String clubName = club.getName();

        String status = result ? "已通过" : "未通过";
        String title = "申请结果通知";
        String content = "您申请加入" + clubName + "的申请已被处理，结果为：" + status;
        return createMessage(uid, title, content);
    }

    public static UserMessage add2Club(String uid, String cid){
        Club club = ClubUnits.getClubByCid(cid);
        if (club == null)
            return null;
        String clubName = club.getName();

        String title = "加入社团通知";
        String content = "您已被管理员加入" + clubName + "，欢迎加入我们";
        return createMessage(uid, title, content);
    }

    /**
     * 欢迎新用户
     * @param uid 用户ID
     * @return 消息实体
     */
    public static UserMessage welcomeNewUser(String uid) {
        String title = "欢迎加入";
        String content = "欢迎加入Ribosome，我们将为您提供最好的社团管理体验";
        return createMessage(uid, title, content);
    }


    /**
     * 创建一条消息
     * Create a message
     * @param uid 用户ID
     * @param title 标题
     * @param content 内容
     * @return 消息实体
     */
    public static UserMessage createMessage(String uid,String title, String content) {
        UserMessage message = new UserMessage();
        message.setUid(Long.parseLong(uid));
        message.setTitle(title);
        message.setContent(content);
        MapperManager.newInstance().userMessageBaseMapper.insert(message);
        return message;
    }

}
