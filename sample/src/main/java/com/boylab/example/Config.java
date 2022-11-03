package com.boylab.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Config {

    public static final List<String> BAUD= new ArrayList<String>(){{
        add("1200");
        add("1800");
        add("2400");
        add("4800");
        add("9600");
        add("19200");
        add("38400");
        add("57600");
        add("115200");
        add("230400");
        add("460800");
        add("500000");
        add("576000");
        /*add("921600");
        add("1000000");
        add("1152000");
        add("1500000");
        add("2000000");
        add("2500000");
        add("3000000");
        add("3500000");
        add("4000000");*/
    }};

    public static final List<String> DataBit= new ArrayList<String>(){{
        add("8");
        add("7");
        add("6");
        add("5");
    }};

    public static final List<String> PARITY= new ArrayList<String>(){{
        add("无校验");
        add("奇校验");
        add("偶校验");
    }};
    public static final HashMap<String, Integer> PARITY_KEY= new HashMap<String, Integer>(){{
        put("无校验", 0);
        put("奇校验", 1);
        put("偶校验", 2);
    }};

    public static final List<String> StopBit= new ArrayList<String>(){{
        add("1");
        add("2");
    }};

    public static final List<String> Delay= new ArrayList<String>(){{
        add("50ms");
        add("100ms");
        add("200ms");
        add("500ms");
        add("1000ms");
        add("2000ms");
        add("3000ms");
    }};

}
