package com.geek45.exampleall.io.nio.util;

import java.net.*;
import java.util.Enumeration;
import java.util.UUID;

public class Util {
    //可靠的获取非回环本机ip方法
    public static InetAddress getLocalhost() throws UnknownHostException {
        try {
            for (Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces(); interfaces.hasMoreElements();) {
                NetworkInterface networkInterface = interfaces.nextElement();
                if (networkInterface.isLoopback() || networkInterface.isVirtual() || !networkInterface.isUp()) {
                    continue;
                }
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                if (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    if(address instanceof Inet4Address) {
                        return address;
                    }
                }
            }
        } catch (SocketException e) {
        }
        return InetAddress.getLocalHost();
    }

    public static String uuid(){
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }

}
