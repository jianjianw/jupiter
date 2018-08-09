package com.qiein.jupiter.util.alicloud;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * @author edward
 * @date 2018/5/18
 */
public class Environment {
    private static Object locker = new Object();
    private static Properties properties = null;

    private static Properties getProperties() {
        if (properties == null) {
            synchronized (locker) {
                if (properties == null) {
                    properties = new Properties();
                    InputStream inputStream = Environment.class.getResourceAsStream("/application.properties");
                    try {
                        properties.load(inputStream);
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    for (Map.Entry<String, String> entry : System.getenv().entrySet()) {
                        properties.put(entry.getKey(), entry.getValue());
                    }
                }
            }
        }
        return properties;
    }

    public static String get(String key) {
        return getProperties().getProperty(key);
    }
}
