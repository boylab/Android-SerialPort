package com.boylab.example.utils;

/**
 * Created by Administrator on 2016/4/8.
 */
public class ViewClick {

    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    private static long firstActionTime = 0;
    private static int actionTimes = 0;
    public static boolean isFastDoubleAction() {
        long time = System.currentTimeMillis();
        if (time - firstActionTime < 1000*3 ){
            return true;
        }
        firstActionTime = time;
        actionTimes = 0;
        return false;
    }

    private static long lastTime;

    public static boolean isDelayTime() {
        long time = System.currentTimeMillis();
        if (time - lastTime < 500) {
            return true;
        }
        lastTime = time;
        return false;
    }

}

