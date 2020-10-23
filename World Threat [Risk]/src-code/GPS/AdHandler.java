package com.strat7.game.GPS;

/**
 * Created by Andrey (cb) Mikheev
 * GPGS
 * 26.09.2016
 */
public interface AdHandler {
    public void show(boolean show);
    public void showVideo();
    public void notEnoughMoney();
    public void atLeast2();
    public void showSmart(boolean show);

    public int getAppVersionCode();
}
