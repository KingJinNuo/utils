package com.cmos.util;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Author ：wx
 * Date ：Created in  2018/9/8 11:28
 */
public class PropertyUtil {
    public static Properties p = new Properties();

    static {
        InputStream in = ClassLoader.getSystemResourceAsStream("ftp.properties");
        try {
            p.load(in);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

}
