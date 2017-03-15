package com.pm.skinlibrary.attr;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import com.pm.skinlibrary.config.Const;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by puming on 2017/3/11.
 */

public class SkinAttrSupport {
    private static final String TAG = "SkinAttrSupport";

    public static List<SkinAttr> getSkinAttrs(AttributeSet attrs, Context context) {
        List<SkinAttr> skinAttrs = new ArrayList<>();
        SkinAttrType skinAttrType = null;
        SkinAttr skinAttr = null;
        int count = attrs.getAttributeCount();
        Log.d(TAG, "getSkinAttrs: count=" + count);
        for (int i = 0; i < count; i++) {
            String attributeName = attrs.getAttributeName(i);
            String attributeValue = attrs.getAttributeValue(i);
            Log.d(TAG, "getSkinAttrs: attributeName=" + attributeName + "\tattributeValue=" + attributeValue);
            if (attributeValue.startsWith("@")) {
                int id = -1;
                try {
                    //style不能被解析成int类型。
                    id = Integer.parseInt(attributeValue.substring(1));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                if (id == -1) {
                    continue;
                }
                String resName = context.getResources().getResourceEntryName(id);
                Log.d(TAG, "getSkinAttrs: resName=" + resName);
                if (resName.startsWith(Const.SKIN_PREFIX)) {
                    Log.d(TAG, "getSkinAttrs: Const.SKIN_PREFIX");
                    skinAttrType = getSupportAttrType(attributeName);
                    /*SkinAttrType[] values = SkinAttrType.values();
                    for (int j = 0; j < values.length; j++) {
                        Log.d(TAG, "getSkinAttrs: values="+values[j]);
                        if(values[j].getResType().equals(attributeName)){
                            Log.d(TAG, "getSkinAttrs: values[j].getResType()="+values[j].getResType());
                        }
                    }
                    for (SkinAttrType attrType : SkinAttrType.values()) {
                        if (attrType.getResType().equals(attributeName)) {
                            skinAttr = new SkinAttr(resName, attrType);
                            skinAttrs.add(skinAttr);
                            Log.d(TAG, "getSkinAttrs: skinAttrs.size()="+skinAttrs.size());
                        }
                    }*/

                    if (skinAttrType == null) {
                        Log.d(TAG, "getSkinAttrs: skinAttrType == null");
                        continue;
                    } else {
                        skinAttr = new SkinAttr(resName, skinAttrType);
                        Log.d(TAG, "getSkinAttrs: skinAttrType!=null");
                        skinAttrs.add(skinAttr);
                    }
                }
            }
        }
        return skinAttrs;
    }

    private static SkinAttrType getSupportAttrType(String attributeName) {
        for (SkinAttrType attrType : SkinAttrType.values()) {
            if (attrType.getResType().equals(attributeName)) {
                return attrType;
            }
        }
        return null;
    }
}
