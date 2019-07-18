package org.the_jk.write_only_field_r8;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.WeakHashMap;

class Manager {
    interface Listener {
        void loaded();
        void invalidated();
    }

    private final Set<Listener> mLoadListeners = new HashSet<>();
    private final Set<Listener> mInvalidateListeners = Collections.newSetFromMap(new WeakHashMap<Listener, Boolean>());
    private boolean mLoaded;

    void addListener(Listener listener) {
        if (mLoaded) {
            listener.loaded();
            mInvalidateListeners.add(listener);
        } else {
            mLoadListeners.add(listener);
        }
    }

    void load() {
        mLoaded = true;
        ArrayList<Listener> call = new ArrayList<>(mLoadListeners);
        mLoadListeners.clear();
        for (Listener listener : call) {
            listener.loaded();
            mInvalidateListeners.add(listener);
        }
    }

    void invalidate() {
        ArrayList<Listener> call = new ArrayList<>(mInvalidateListeners);
        for (Listener listener : call) {
            listener.invalidated();
        }
    }
}
