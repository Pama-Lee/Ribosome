/*
 *
 * .______       __  .______     ______        _______.  ______   .___  ___.  _______
 * |   _  \     |  | |   _  \   /  __  \      /       | /  __  \  |   \/   | |   ____|
 * |  |_)  |    |  | |  |_)  | |  |  |  |    |   (----`|  |  |  | |  \  /  | |  |__
 * |      /     |  | |   _  <  |  |  |  |     \   \    |  |  |  | |  |\/|  | |   __|
 * |  |\  \----.|  | |  |_)  | |  `--'  | .----)   |   |  `--'  | |  |  |  | |  |____
 * | _| `._____||__| |______/   \______/  |_______/     \______/  |__|  |__| |_______|
 *
 * CreateTime: 2023/1/21
 * Author: Li JiaKe(Pama)
 */
package cn.devspace.ribosome;

import cn.devspace.nucleus.Manager.Command.CommandManager;
import cn.devspace.nucleus.Manager.DataBase.DataBase;
import cn.devspace.nucleus.Plugin.PluginBase;
import cn.devspace.ribosome.command.Command;
import cn.devspace.ribosome.entity.User;
import cn.devspace.ribosome.manager.database.DataBaseManager;
import cn.devspace.ribosome.mapping.auth;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import javax.annotation.Resource;

/**
 *  启动类
 */
public class Main extends PluginBase {

    public final String version = "v0.0.1";
    public final String PluginName = "Ribosome";

    private DataBaseManager dataBaseManager = new DataBaseManager();

    @Resource
    private BaseMapper<User> userBaseMapper;


    /**
     * 当该项目加载时事件
     * 此时会对数据库进行配置并注册路由
     */
    @Override
    public void onLoad() {
        sendLog(translateMessage("Loading"));
        CommandManager.registerCommand(new Command());
        // 注册路由
        initRoute(auth.class);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onEnabled() {
        super.onEnabled();
    }

    public String getPluginName(){
        return this.PluginName;
    }
    public String getVersion(){
        return this.version;
    }
    public DataBaseManager getDataBaseManager() {
        return dataBaseManager;
    }
    public DataBase getDatabase(){
        return dataBaseManager.getDataBase();
    }
}
