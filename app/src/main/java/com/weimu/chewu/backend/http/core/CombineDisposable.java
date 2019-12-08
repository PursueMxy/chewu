package com.weimu.chewu.backend.http.core;


import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;


public class CombineDisposable implements Disposable {
    private Map<String, Disposable> mMap = new HashMap<>();

    /**
     * 添加
     */
    public void addDisposable(String key, Disposable disposable) {
        dispose(key);

        mMap.put(key, disposable);
    }

    public void dispose(String key) {
        Disposable disposable = mMap.get(key);
        if (disposable != null) {
            dispose(disposable);
            mMap.remove(key);
        }
    }

    public boolean isDisposable(String key) {
        return isDisposed(mMap.get(key));

    }

    @Override
    public void dispose() {
        for (Map.Entry<String, Disposable> entry : mMap.entrySet()) {
            Disposable disposable = entry.getValue();
            dispose(disposable);
        }
        mMap.clear();
    }


    @Override
    public boolean isDisposed() {
        if (mMap.size() == 0) {
            return true;
        } else {
            for (Map.Entry<String, Disposable> entry : mMap.entrySet()) {
                Disposable disposable = entry.getValue();
                if (!isDisposed(disposable)) {
                    return false;
                }
            }
        }
        return true;
    }


    private boolean isDisposed(Disposable d) {
        return d == null || d.isDisposed();
    }

    private void dispose(Disposable d) {
        if (!isDisposed(d)) d.dispose();
    }
}
