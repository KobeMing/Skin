package com.pm.skinlibrary.skin;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

/**
 * Created by puming on 2017/3/11.
 */

public class ResourceManager {
    private Resources mResources;
    private String mPkgName;

    public ResourceManager(String mPkgName, Resources mResources) {
        this.mPkgName = mPkgName;
        this.mResources = mResources;
    }

    public Drawable getDrawableByResName(String name) {
        try {
            return mResources.getDrawable(mResources.getIdentifier(name, "drawable", mPkgName));
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            return null;
        } finally {
        }
    }

    public Drawable getMipmapByResName(String name) {
        try {
            return mResources.getDrawable(mResources.getIdentifier(name, "mipmap", mPkgName));
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            return null;
        } finally {
        }
    }

    public ColorStateList getColorByResName(String name) {
        try {
            return mResources.getColorStateList(mResources.getIdentifier(name, "color", mPkgName));
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            return null;
        } finally {
        }
    }

}
