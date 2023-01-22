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


package cn.devspace.ribosome.manager;

import cn.devspace.nucleus.Lang.LangBase;

public class languageManager {
    private static final languageManager singleClazz = new languageManager();
    private LangBase langBase = null;
    public static languageManager newInstance() {
        return singleClazz;
    }

    public void setLangBase(LangBase langBase) {
        this.langBase = langBase;
    }

    public LangBase getLangBase() {
        return langBase;
    }

    public static String translateMessage(String key, Object... params) {
        return newInstance().langBase.TranslateOne(key, params);
    }

    public static String translateMessage(String key, String[] param) {
        return newInstance().langBase.TranslateOne(key, (Object) param);
    }


}
