package com.pm.skinlibrary.base;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.AttributeSet;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;

import com.pm.skinlibrary.attr.SkinAttr;
import com.pm.skinlibrary.attr.SkinAttrSupport;
import com.pm.skinlibrary.attr.SkinAttrType;
import com.pm.skinlibrary.attr.SkinView;
import com.pm.skinlibrary.callback.ISkinChangedListener;
import com.pm.skinlibrary.skin.SkinManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BaseSkinActivity extends AppCompatActivity implements ISkinChangedListener {
    private static final String TAG = "BaseSkinActivity";

    private static final Class<?>[] sConstructorSignature = new Class[]{
            Context.class, AttributeSet.class};

    private static final Class<?>[] sCreateViewSignature = new Class[]{
            View.class, String.class, Context.class, AttributeSet.class};

    private static final Map<String, Constructor<? extends View>> sConstructorMap
            = new ArrayMap<>();

    private static final String[] sClassPrefixList = {
            "android.widget.",
            "android.view.",
            "android.webkit."
    };

    private final Object[] mConstructorArgs = new Object[2];
    private final Object[] mCreateViewArgs = new Object[4];
    private Method mCreateView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SkinManager.getInstance().registerListener(this);
        LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LayoutInflaterCompat.setFactory(mInflater, new LayoutInflaterFactory() {
            @Override
            public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
                //系统没有使用setFactory，AppCompatActivity使用了。
                //完成AppCompatActivity的View构建过程。
                AppCompatDelegate delegate = getDelegate();
                View view = null;
                List<SkinAttr> skinAttrs = null;
                try {
                    if (mCreateView == null) {

                        mCreateView = delegate.getClass().getMethod("createView",
                                sCreateViewSignature);
                        mCreateViewArgs[0] = parent;
                        mCreateViewArgs[1] = name;
                        mCreateViewArgs[2] = context;
                        mCreateViewArgs[3] = attrs;
                        view = (View) mCreateView.invoke(delegate, mCreateViewArgs);
                    }


                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "onCreateView: name="+name);
                skinAttrs = SkinAttrSupport.getSkinAttrs(attrs, context);
                int size = skinAttrs.size();
                Log.d(TAG, "onCreateView: size=" + size);
                for (int i = 0; i < size; i++) {
                    String resName = skinAttrs.get(i).getResName();
                    SkinAttrType attrType = skinAttrs.get(i).getType();
                    Log.d(TAG, "onCreateView: resName=" + resName + "\tattrType=" + attrType);
                }
                if (skinAttrs.isEmpty()) {
                    return null;
                }

                if (view == null) {
                    view = createViewFromTag(context, name, attrs);
                }

                if (view != null) {
                    //构建SkinView
                    injectSkinView(view, skinAttrs);
                }
                return view;
            }
        });
        super.onCreate(savedInstanceState);
    }

    private void injectSkinView(View view, List<SkinAttr> skinAttrs) {
        List<SkinView> skinViews = SkinManager.getInstance().getSkinViews(this);
        if (skinViews == null) {
            skinViews = new ArrayList<SkinView>();
            SkinManager.getInstance().addSkinView(this, skinViews);
        }
        skinViews.add(new SkinView(skinAttrs, view));
        //check当前是否需要自动换肤，如果需要，则走换肤的逻辑
        if(SkinManager.getInstance().needChangSkin()){
            SkinManager.getInstance().doRealSkin(this);
        }
    }

    private View createViewFromTag(Context context, String name, AttributeSet attrs) {
        if (name.equals("view")) {
            name = attrs.getAttributeValue(null, "class");
        }

        try {
            mConstructorArgs[0] = context;
            mConstructorArgs[1] = attrs;

            if (-1 == name.indexOf('.')) {
                for (int i = 0; i < sClassPrefixList.length; i++) {
                    final View view = createView(context, name, sClassPrefixList[i]);
                    if (view != null) {
                        return view;
                    }
                }
                return null;
            } else {
                return createView(context, name, null);
            }
        } catch (Exception e) {
            // We do not want to catch these, lets return null and let the actual LayoutInflater
            // try
            return null;
        } finally {
            // Don't retain references on context.
            mConstructorArgs[0] = null;
            mConstructorArgs[1] = null;
        }
    }

    private View createView(Context context, String name, String prefix)
            throws ClassNotFoundException, InflateException {
        Constructor<? extends View> constructor = sConstructorMap.get(name);

        try {
            if (constructor == null) {
                // Class not found in the cache, see if it's real, and try to add it
                Class<? extends View> clazz = context.getClassLoader().loadClass(
                        prefix != null ? (prefix + name) : name).asSubclass(View.class);

                constructor = clazz.getConstructor(sConstructorSignature);
                sConstructorMap.put(name, constructor);
            }
            constructor.setAccessible(true);
            return constructor.newInstance(mConstructorArgs);
        } catch (Exception e) {
            // We do not want to catch these, lets return null and let the actual LayoutInflater
            // try
            return null;
        }
    }

    @Override
    public void onSkinChanged() {
        Log.d(TAG, "onSkinChanged: ");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SkinManager.getInstance().unRegisterListener(this);
    }
}
