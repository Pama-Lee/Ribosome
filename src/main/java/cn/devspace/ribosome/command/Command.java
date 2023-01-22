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

package cn.devspace.ribosome.command;

import cn.devspace.nucleus.Manager.Annotation.Commands;
import cn.devspace.nucleus.Manager.Command.CommandBase;
import cn.devspace.nucleus.Plugin.PluginBase;
import cn.devspace.ribosome.manager.languageManager;


public class Command extends PluginBase implements CommandBase {

    @Commands(Command = "ribo",help = "Ribosome functions, type in [/ribo help] to get helps")
    public void Ribosome(String[] args){
        if (args != null){
            switch (args[0]){
                case "help":
                    help();
                    break;
                case "test":
                    test();
                    break;
            }
        }
    }

    private void help(){
        sendLog("=-=-=-=-=-=Ribosome=-=-=-=-=-=");
        sendLog("->"+ languageManager.translateMessage("Command.help.help"));
        sendLog("->"+languageManager.translateMessage("Command.help.test"));
        sendLog("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
    }

    private void test(){
    }




}
