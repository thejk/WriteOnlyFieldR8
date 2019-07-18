package org.the_jk.write_only_field_r8;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class MyActivity extends Activity {
    private static final String TAG = "r8";
    private boolean mLoaded;
    private boolean mInvalidated;

    private class Listener implements Manager.Listener {
        Listener() {
            mManager.addListener(this);
        }

        @Override
        public void loaded() {
            Log.e(TAG, "loaded");
            mLoaded = true;
        }

        @Override
        public void invalidated() {
            Log.e(TAG, "invalidated");
            mInvalidated = true;
        }
    }

    private final Manager mManager = new Manager();
    private final Listener mListener = new Listener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mManager.load();
        if (!mLoaded) throw new AssertionError();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // System.gc is just a hint, so hammer the heap
        System.gc();
        for (int i = 0; i < 1000; ++i) {
            byte[] tmp = new byte[1000000];
        }
        mManager.invalidate();
        if (!mInvalidated) throw new AssertionError();

        finish();
    }
}
