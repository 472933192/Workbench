package com.example.konka.workbench.service;

/**
 * Created by HP on 2016-10-20.
 */
public interface MessageServiceListener {
    public abstract void onCreateService();
    public abstract void onStartService();
    public abstract void onDestroyService();
}
