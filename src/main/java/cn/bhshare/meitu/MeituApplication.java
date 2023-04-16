package cn.bhshare.meitu;

import cn.bhshare.meitu.util.ConsoleQRcode;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

@SpringBootApplication
@MapperScan("cn.bhshare.meitu.dao")
@Slf4j
public class MeituApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        ConfigurableApplicationContext application = SpringApplication.run(MeituApplication.class, args);
        System.out.println("项目启动成功 *^_^* \n" + " .-------.       ____     __        \n"
                + " |  _ _   \\      \\   \\   /  /    \n" + " | ( ' )  |       \\  _. /  '       \n"
                + " |(_ o _) /        _( )_ .'         \n" + " | (_,_).' __  ___(_ o _)'          \n"
                + " |  |\\ \\  |  ||   |(_,_)'         \n" + " |  | \\ `'   /|   `-'  /           \n"
                + " |  |  \\    /  \\      /           \n" + " ''-'   `'-'    `-..-'              ");
        Environment env = application.getEnvironment();
        String port = env.getProperty("server.port");
        String path = env.getProperty("server.servlet.context-path");
        if (StringUtils.isEmpty(path)) {
            path = "";
        }
        System.out.println("\n" +
                "----------------------------------------------------------\n\t" +
                "Application  is running! Access URLs:\n\t" +
                "Local访问网址: \t\thttp://localhost:" + port + path + "\n\t");
        try {
            Enumeration<NetworkInterface> nifs = NetworkInterface.getNetworkInterfaces();
            boolean shouldPrintQRcode = true;
            while (nifs.hasMoreElements()) {
                NetworkInterface nif = nifs.nextElement();
                Enumeration<InetAddress> address = nif.getInetAddresses();
                if (nif.getName().equals("lo")) { // localhost
                    continue;
                }
                while (address.hasMoreElements()) {
                    InetAddress addr = address.nextElement();
                    if (addr instanceof Inet4Address) {
                        System.out.println("\tExternal访问网址(" + nif.getName() + "): \thttp://" + addr.getHostAddress() + ":" + port + path);
                        if (shouldPrintQRcode) { // 仅打印第一个外部网址
                            ConsoleQRcode.printQRcode("http://" + addr.getHostAddress() + ":" + port + path);
                            shouldPrintQRcode = false;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("----------------------------------------------------------");
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(MeituApplication.class);
    }

}
