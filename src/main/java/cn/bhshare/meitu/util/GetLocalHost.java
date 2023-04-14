package cn.bhshare.meitu.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class GetLocalHost {
    public static void main(String[] args) {
        try {
            Enumeration<NetworkInterface> nifs = NetworkInterface.getNetworkInterfaces();

            while (nifs.hasMoreElements()) {
                NetworkInterface nif = nifs.nextElement();
                Enumeration<InetAddress> address = nif.getInetAddresses();
                while (address.hasMoreElements()) {
                    InetAddress addr = address.nextElement();
                    if (addr instanceof Inet4Address) {
                        System.out.println("网卡名称：" + nif.getName());
                        System.out.println("网络接口地址：" + addr.getHostAddress());
                        System.out.println();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

//输出：
//网卡名称：eno1
//网络接口地址：10.221.99.172
//
//网卡名称：lo
//网络接口地址：127.0.0.1
