package com.boylab.example.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SharePref {

    private static SharedPreferences mShared = null;

    public static HashMap<String, Integer> getSerial(Context context){
        mShared = context.getSharedPreferences("serial", Context.MODE_PRIVATE);
        int port = mShared.getInt("port", 0);
        int baud = mShared.getInt("baud", 0);
        int databit = mShared.getInt("databit", 0);
        int parity = mShared.getInt("parity", 0);
        int stopbit = mShared.getInt("stopbit", 0);
        HashMap<String, Integer> serial = new HashMap<>();
        serial.put("port", port);
        serial.put("baud", baud);
        serial.put("databit", databit);
        serial.put("parity", parity);
        serial.put("stopbit", stopbit);
        return serial;
    }

    public static void setSerial(Context context, HashMap<String, Integer> serial){
        mShared = context.getSharedPreferences("serial", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mShared.edit();
        editor.putInt("port", serial.get("port") );
        editor.putInt("baud", serial.get("baud") );
        editor.putInt("databit", serial.get("databit") );
        editor.putInt("parity", serial.get("parity") );
        editor.putInt("stopbit", serial.get("stopbit") );
        editor.commit();
    }

    public static String getSendData(Context context){
        mShared = context.getSharedPreferences("serial", Context.MODE_PRIVATE);
        String sendData = mShared.getString("sendData", "");
        return sendData;
    }

    public static void setSendData(Context context, String sendData){
        mShared = context.getSharedPreferences("serial", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mShared.edit();
        editor.putString("sendData", sendData );
        editor.commit();
    }


    public static boolean isHexShow(Context context){
        mShared = context.getSharedPreferences("serial", Context.MODE_PRIVATE);
        boolean isHex = mShared.getBoolean("isHex", true);
        return isHex;
    }

    public static void setHexShow(Context context, boolean isHex){
        mShared = context.getSharedPreferences("serial", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mShared.edit();
        editor.putBoolean("isHex", isHex);
        editor.commit();
    }

    public static boolean isNextLine(Context context){
        mShared = context.getSharedPreferences("serial", Context.MODE_PRIVATE);
        boolean isNextLine = mShared.getBoolean("isNext", true);
        return isNextLine;
    }

    public static void setNextLine(Context context, boolean isNextLine){
        mShared = context.getSharedPreferences("serial", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mShared.edit();
        editor.putBoolean("isNext", isNextLine);
        editor.commit();
    }

}
