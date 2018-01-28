package com.alipchansky.chat;

import java.util.regex.Pattern;

public class Constants {
    public final static int PORT = 9786;
    public final static String IP_BROADCAST1 = "192.168.0.102";
    public final static Pattern regex = Pattern.compile("[\u0020-\uFFFF]");
    public static final String DISPATCHER_HOST = "192.168.0.102";
    public static final int DISPATCHER_PORT = 9090;
}
