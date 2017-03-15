package com.pm.skinlibrary.attr;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pm.skinlibrary.skin.ResourceManager;
import com.pm.skinlibrary.skin.SkinManager;

/**
 * Created by puming on 2017/3/11.
 */

public enum SkinAttrType {

    SRC("src") {
        @Override
        public void apply(View view, String resName) {
            Drawable drawable = getResourceManager().getMipmapByResName(resName);
            if (drawable != null) {

                if (view instanceof ImageView) {
                    ((ImageView) view).setImageDrawable(drawable);
                }
            }
        }
    },
    BACKGROUND("background") {
        @Override
        public void apply(View view, String resName) {
            Drawable drawable = getResourceManager().getDrawableByResName(resName);
            if (drawable != null) {
                view.setBackground(drawable);
            }
        }
    },
    SRC_COMPAT("srcCompat") {
        @Override
        public void apply(View view, String resName) {
            Drawable drawable = getResourceManager().getMipmapByResName(resName);
            if (drawable != null) {

                if (view instanceof ImageView) {
                    ((ImageView) view).setImageDrawable(drawable);
                }
            }
        }
    },
    TEXT_COLOR("textColor") {
        @Override
        public void apply(View view, String resName) {
            ColorStateList color = getResourceManager().getColorByResName(resName);
            if (color != null) {
                if (view instanceof TextView) {
                    ((TextView) view).setTextColor(color);
                }
            }
        }
    };

    private String mResType;

    public String getResType() {
        return mResType;
    }

    SkinAttrType(String resType) {
        this.mResType = resType;
    }

    public abstract void apply(View view, String resName);

    public ResourceManager getResourceManager() {
        return SkinManager.getInstance().getResourcesManager();
    }
}
