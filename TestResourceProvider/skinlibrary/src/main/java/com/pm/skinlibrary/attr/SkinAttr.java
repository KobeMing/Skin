package com.pm.skinlibrary.attr;

import android.view.View;

/**
 * Created by puming on 2017/3/11.
 */

public class SkinAttr {
    private String mResName;
    private SkinAttrType mType;

    public SkinAttr(String resName, SkinAttrType type) {
        this.mResName = resName;
        this.mType = type;
    }

    public String getResName() {
        return mResName;
    }

    public void setResName(String resName) {
        this.mResName = resName;
    }

    public SkinAttrType getType() {
        return mType;
    }

    public void setType(SkinAttrType type) {
        this.mType = type;
    }

    public void apply(View view){
        mType.apply(view,mResName);
    }
}
