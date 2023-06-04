import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import jpcap.packet.ARPPacket;
import jpcap.packet.IPPacket;
import jpcap.packet.Packet;
import jpcap.packet.TCPPacket;

import java.io.IOException;
import java.util.Arrays;


/**
 * @author Raymond Li
 * @create 2023-05-08 9:51
 * @description
 */
public class MyJpcap {

    public static void main(String[] args) throws IOException {
        JpcapCaptor jpcap = bindNetworkDevices();
        capture(jpcap);
    }

    /**
     * 绑定网络设备
     *
     * @return JpcapCaptor
     * @throws IOException
     */
    public static JpcapCaptor bindNetworkDevices() throws IOException {
        NetworkInterface[] devices = JpcapCaptor.getDeviceList();
        for (NetworkInterface n : devices) {
            System.out.println(n.name + "    |    " + n.description + "  |  " + n.addresses);
        }
        System.out.println("------------------------------");
        // 构建一个设别的连接（具体的实例，获取的字符串长度，是否开启混杂模式，超时连接）
        return JpcapCaptor.openDevice(devices[1], 1512, true, 50);
    }

    /**
     * 抓包
     *
     * @param jpcap
     */
    public static void capture(JpcapCaptor jpcap) {
        int i = 0;
        while (i < 10) {
            Packet packet = jpcap.getPacket();
            if (packet instanceof IPPacket ip) {
                i++;
                System.out.println("IP数据包");
                System.out.println("版本：IPv" + ((IPPacket) packet).version);
                System.out.println("优先权：" + ip.priority);
                System.out.println("区分服务：最大吞吐量：" + ip.t_flag);
                System.out.println("区分服务：最高可靠性：" + ip.r_flag);
                System.out.println("长度：" + ip.length);
                System.out.println("标识：" + ip.ident);
                System.out.println("DF:Don't Fragment: " + ip.dont_frag);
                System.out.println("MF:More Fragment: " + ip.more_frag);
                System.out.println("片偏移：" + ip.offset);
                System.out.println("生存时间：" + ip.hop_limit);

                String protocol = null;
                switch (ip.protocol) {
                    case 1 -> protocol = "ICMP";
                    case 2 -> protocol = "IGMP";
                    case 6 -> protocol = "TCP";
                    case 8 -> protocol = "EGP";
                    case 9 -> protocol = "IGP";
                    case 17 -> protocol = "UDP";
                    case 41 -> protocol = "IPv6";
                    case 89 -> protocol = "OSPF";
                    default -> {
                    }
                }
                System.out.println("协议：" + protocol);
                System.out.println("源IP：" + ip.src_ip.getHostAddress());
                System.out.println("目的IP：" + ip.dst_ip.getHostAddress());
                System.out.println("源主机名：" + ip.src_ip);
                System.out.println("目的主机名：" + ip.dst_ip);
                System.out.println("------------------------------");
            }
            // else if (packet instanceof ARPPacket arp && arp.hardtype == 1) {
            //     i++;
            //     System.out.println("ARP数据包");
            //     System.out.println("网络类型：以太网");
            //     System.out.println("物理地址长度：" + arp.hlen);
            //     System.out.println("协议地址长度：" + arp.plen);
            //     System.out.println("源MAC：" + arp.getSenderHardwareAddress());
            //     System.out.println("源IP：" + arp.getSenderProtocolAddress());
            //     System.out.println("目标MAC：" + arp.getTargetHardwareAddress());
            //     System.out.println("目标IP：" + arp.getTargetProtocolAddress());
            //     System.out.println("------------------------------");
            // }
        }
    }
}
