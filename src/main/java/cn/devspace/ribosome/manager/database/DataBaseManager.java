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

package cn.devspace.ribosome.manager.database;

import cn.devspace.nucleus.Manager.DataBase.DataBase;
import cn.devspace.ribosome.entity.User;

import java.util.Properties;

/**
 * 此处是对Hibernate数据库进行管理
 * 对于被Hibernate数据库管理的实体, 需要继承{@link cn.devspace.nucleus.Plugin.DataEntity}并且在{@link cn.devspace.ribosome.Main}类的`onLoad()`方法中进行实体注册
 * Mybatis-plus所管理的数据库直接使用注释{@link javax.annotation.Resource}
 * 及 {@link com.baomidou.mybatisplus.core.mapper.BaseMapper}进行访问
 */
public class DataBaseManager{
    private DataBase dataBase = null;

    public DataBaseManager(){
        // 数据库配置文件
        Properties prop = new Properties();
        prop.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/login");
        prop.setProperty("hibernate.connection.username", "root");
        prop.setProperty("hibernate.connection.password", "root");
        prop.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
        prop.setProperty("hibernate.show_sql", "true");
        prop.setProperty("hibernate.format_sql", "true");
        prop.setProperty("dialect", "org.hibernate.dialect.Mysql8Dialect");
        prop.setProperty("hibernate.hbm2ddl.auto", "update");
        // 初始化数据库 默认数据表为User
        this.dataBase = new DataBase(this.getClass(),new User(),prop);
    }



    public DataBase getDataBase() {
        return dataBase;
    }
}
