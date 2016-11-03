package com.example.konka.workbench.activity.main;

/**
 * Created by HP on 2016-10-28.
 */
public interface MainActivityStatusListener {
    public abstract void onCreateMain();
    public abstract void onStartMain();
    public abstract void onDestroyMain();
    public abstract void onLogout();
}
