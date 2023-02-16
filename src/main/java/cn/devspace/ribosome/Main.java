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
import cn.devspace.ribosome.error.errorManager;
import cn.devspace.ribosome.manager.database.DataBaseManager;
import cn.devspace.ribosome.manager.languageManager;
import cn.devspace.ribosome.mapping.*;
import cn.devspace.ribosome.mapping.admin.adminBase;


/**
 *  启动类
 */
public class Main extends PluginBase {

    public final String version = "v0.0.1";
    public final String PluginName = "Ribosome";

    private final DataBaseManager dataBaseManager = new DataBaseManager();
    private final errorManager errorManager = new errorManager();

    // 单例模式下的多语言管理
    private static final languageManager languageManager = cn.devspace.ribosome.manager.languageManager.newInstance();

    /**
     * 当该项目加载时事件
     * 此时会对数据库进行配置并注册路由
     */
    @Override
    public void onLoad() {
        sendLog(translateMessage("Loading"));
        // 注册单例语言管理
        languageManager.setLangBase(getPluginLang());
        // 注册命令
        CommandManager.registerCommand(new Command());
        // 注册路由
        initRoute(auth.class);
        initRoute(routes.class);
        initRoute(club.class);
        initRoute(user.class);
        initRoute(adminBase.class);
        initRoute(material.class);
    }

    @Override
     public void onEnable() {
        sendLog(translateMessage("Enable"));
    }

    @Override
    public void onEnabled() {

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
