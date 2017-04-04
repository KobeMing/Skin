package com.pm.skinlibrary.skin;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.ArrayMap;

import com.pm.skinlibrary.attr.SkinView;
import com.pm.skinlibrary.callback.ISkinChangeCallback;
import com.pm.skinlibrary.callback.ISkinChangedListener;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by puming on 2017/3/11.
 */

public class SkinManager {
    private volatile static SkinManager skinManager;
    private Context mContext;
    private ResourceManager mResourcesManager;

    private List<ISkinChangedListener> mListeners = new ArrayList<ISkinChangedListener>();


    private ArrayMap<ISkinChangedListener, List<SkinView>> mSkinViewMaps =
            new ArrayMap<ISkinChangedListener, List<SkinView>>();
    private PrefUtils mPrefUtils;

    private String mCurrPath;
    private String mCurrPak;
    private String mSuffix;


    private SkinManager() {

    }

    public static SkinManager getInstance() {
        if (skinManager == null) {
            synchronized (SkinManager.class) {
                if (skinManager == null) {
                    skinManager = new SkinManager();
                    return skinManager;
                }
            }
        }
        return skinManager;
    }

    public ResourceManager getResourcesManager() {
        if (!usePlugin()) {
            //应用内部换肤
            return new ResourceManager(mContext.getPackageName(), mContext.getResources(), mSuffix);
        }
        return mResourcesManager;
    }


    public void init(Context context) {
        this.mContext = context.getApplicationContext();
        mPrefUtils = new PrefUtils(mContext);

        String pluginPath = mPrefUtils.getPluginPath();
        String pluginPkg = mPrefUtils.getPluginPkg();
        mSuffix = mPrefUtils.getSuffix();

        try {
            File file = new File(pluginPath);
            if (file.exists()) {
                loadPlugin(pluginPath, pluginPkg);
            }

        } catch (Exception e) {
            e.printStackTrace();
            mPrefUtils.clear();
        }

    }

    private void loadPlugin(String skinPluginPath, String skinPluginPkg) {
        try {
            if (skinPluginPath.equals(mCurrPath) && skinPluginPkg.equals(mCurrPak)) {
                return;
            }
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, skinPluginPath);
            Resources superResources = mContext.getResources();
            Resources resources = new Resources(assetManager, superResources.getDisplayMetrics(),
                    superResources.getConfiguration());
            mResourcesManager = new ResourceManager(skinPluginPkg, resources, null);

            mCurrPath = skinPluginPath;
            mCurrPak = skinPluginPkg;

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public List<SkinView> getSkinViews(ISkinChangedListener listener) {
        return mSkinViewMaps.get(listener);
    }

    public void addSkinView(ISkinChangedListener listener, List<SkinView> view) {
        mSkinViewMaps.put(listener, view);
    }

    public void registerListener(ISkinChangedListener listener) {
        mListeners.add(listener);
    }

    public void unRegisterListener(ISkinChangedListener listener) {
        mListeners.remove(listener);
        mSkinViewMaps.remove(listener);
    }

    /**
     * 应用内的资源换肤
     *
     * @param suffix
     */
    public void changeSkin(String suffix) {
        // TODO: 2017/3/12  
        clearPluginInfo();
        mSuffix = suffix;
        mPrefUtils.saveSuffix(suffix);
        notifyChangeListener();
    }

    private void clearPluginInfo() {
        mCurrPath = null;
        mCurrPak = null;
        mSuffix = null;
        mPrefUtils.clear();

    }

    /**
     * 插件式换肤
     *
     * @param skinPluginPath
     * @param skinPluginPkg
     * @param callback
     */
    public void changeSkin(final String skinPluginPath, final String skinPluginPkg, ISkinChangeCallback callback) {
        if (callback == null) {
            callback = ISkinChangeCallback.sDefaultCallback;
        }
        final ISkinChangeCallback changeCallback = callback;
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                changeCallback.onStart();
            }

            @Override
            protected Integer doInBackground(Void... params) {
                try {
                    loadPlugin(skinPluginPath, skinPluginPkg);
                } catch (Exception e) {
                    e.printStackTrace();
                    changeCallback.onError(e);
                    return -1;
                }
                return 0;
            }

            @Override
            protected void onPostExecute(Integer result) {
                super.onPostExecute(result);
                if (result == -1) {
                    changeCallback.onError(new Exception());
                }
                try {
                    //通知换肤
                    notifyChangeListener();
                    changeCallback.onComplete();

                    //update path and pkg
                    mPrefUtils.savePluginPath(skinPluginPath);
                    mPrefUtils.savePluginPkg(skinPluginPkg);
                } catch (Exception e) {
                    e.printStackTrace();
                    changeCallback.onError(e);
                }
            }
        }.execute();
    }

    private void updatePluginInfo() {
        // TODO: 2017/3/12
    }

    /**
     * 通知SkinView换肤，并回调换肤逻辑onSkinChanged
     */
    private void notifyChangeListener() {
        for (ISkinChangedListener listener : mListeners) {
            doRealSkin(listener);
            listener.onSkinChanged();
        }
    }

    /**
     * SkinView真正的做换肤操作。
     *
     * @param listener
     */
    public void doRealSkin(ISkinChangedListener listener) {
        List<SkinView> skinViews = mSkinViewMaps.get(listener);
        for (SkinView skinView : skinViews) {
            skinView.apply();
        }
    }

    public boolean needChangSkin() {
        return usePlugin() || useSuffix();
    }

    private boolean usePlugin() {
        return mCurrPath != null && !mCurrPath.trim().equals("");
    }

    private boolean useSuffix() {
        return mSuffix != null && !mSuffix.trim().equals("");
    }
}
