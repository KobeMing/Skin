package com.pm.skinlibrary.skin;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

/**
 * Created by puming on 2017/3/11.
 */

public class ResourceManager {
    private Resources mResources;
    private String mPkgName;

    private String mSuffix;

    public ResourceManager(String mPkgName, Resources mResources, String suffix) {
        this.mPkgName = mPkgName;
        this.mResources = mResources;
        if (suffix == null) {
            suffix = "";
        }
        this.mSuffix = suffix;
    }

    public Drawable getDrawableByResName(String name) {
        name = appendSuffix(name);
        try {
            return mResources.getDrawable(mResources.getIdentifier(name, "drawable", mPkgName));
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            return null;
        } finally {
        }
    }

    public Drawable getMipmapByResName(String name) {
        name = appendSuffix(name);

        try {
            return mResources.getDrawable(mResources.getIdentifier(name, "mipmap", mPkgName));
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            return null;
        } finally {
        }
    }

    public ColorStateList getColorByResName(String name) {
        name = appendSuffix(name);

        try {
            return mResources.getColorStateList(mResources.getIdentifier(name, "color", mPkgName));
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            return null;
        } finally {
        }
    }

    private String appendSuffix(String name) {
        if (!TextUtils.isEmpty(mSuffix)) {
            return name += "_" + mSuffix;
        }
        return null;
    }

}
