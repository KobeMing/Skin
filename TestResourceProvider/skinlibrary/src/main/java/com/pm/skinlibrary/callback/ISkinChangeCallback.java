package com.pm.skinlibrary.callback;

import android.util.Log;

/**
 * Created by puming on 2017/3/11.
 */

public interface ISkinChangeCallback {
    void onStart();

    void onError(Exception e);

    void onComplete();

    DefaultISkinChangeCallback sDefaultCallback = new DefaultISkinChangeCallback();

    class DefaultISkinChangeCallback implements ISkinChangeCallback {
        private static final String TAG = "DefaultISkinChangeCallback";

        @Override
        public void onStart() {
            Log.d(TAG, "onStart: ");
        }

        @Override
        public void onError(Exception e) {
            Log.d(TAG, "onError: ");
        }

        @Override
        public void onComplete() {
            Log.d(TAG, "onComplete: ");
        }
    }
}
