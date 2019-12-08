package com.weimu.chewu.origin.list;


import com.weimu.chewu.backend.http.observer.OnRequestListListener;

/**
 * Created by tonychen on 15/10/19.
 */
public abstract class CommandV2<T> {
    public abstract void execute(String pageNumber, String pageSize, OnRequestListListener<T> observer);
}

