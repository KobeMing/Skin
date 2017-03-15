package com.pm.skinlibrary.attr;

import android.view.View;

import java.util.List;

/**
 * Created by puming on 2017/3/11.
 */

public class SkinView {
    private View mView;
    private List<SkinAttr> mAttrs;

    public SkinView(List<SkinAttr> attr, View view) {
        if (attr != null && view != null) {
            this.mAttrs = attr;
            this.mView = view;
        } else {
            throw new IllegalArgumentException("SkinView:attr is null or view is null");
        }
    }

    public List<SkinAttr> getAttrs() {
        return mAttrs;
    }

    public void setAttrs(List<SkinAttr> attrs) {
        this.mAttrs = attrs;
    }

    public View getView() {
        return mView;
    }

    public void setView(View view) {
        this.mView = view;
    }

    public void apply() {
        for (SkinAttr attr : mAttrs) {
            attr.apply(mView);
        }
    }
}
