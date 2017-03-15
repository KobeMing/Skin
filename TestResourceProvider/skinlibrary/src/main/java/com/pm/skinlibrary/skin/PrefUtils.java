package com.pm.skinlibrary.skin;

import android.content.Context;
import android.content.SharedPreferences;

import com.pm.skinlibrary.config.Const;

/**
 * Created by puming on 2017/3/12.
 */

public class PrefUtils {
    private Context mContext;

    public PrefUtils(Context context) {
        this.mContext = context;
    }

    private SharedPreferences getSharedPreferences() {
        return mContext.getSharedPreferences(Const.PREF_NAME, Context.MODE_PRIVATE);
    }

    public void savePluginPath(String path) {
        SharedPreferences sp = getSharedPreferences();
        sp.edit().putString(Const.KEY_PLUGIN_PATH, path).apply();
    }

    public void savePluginPkg(String pkg) {
        SharedPreferences sp = getSharedPreferences();
        sp.edit().putString(Const.KEY_PLUGIN_PKG, pkg).apply();
    }

    public String getPluginPath() {
        SharedPreferences sp = getSharedPreferences();
        return sp.getString(Const.KEY_PLUGIN_PATH, "");

    }

    public String getPluginPkg() {
        SharedPreferences sp = getSharedPreferences();
        return sp.getString(Const.KEY_PLUGIN_PKG, "");
    }

    public void saveSuffix(String suffix){
        SharedPreferences sp = getSharedPreferences();
        sp.edit().putString(Const.KEY_SUFFIX,suffix).apply();
    }

    public String getSuffix() {
        SharedPreferences sp = getSharedPreferences();
        return sp.getString(Const.KEY_SUFFIX, "");
    }


    public void clear() {
        SharedPreferences sp = getSharedPreferences();
        sp.edit().clear().commit();
    }
}
