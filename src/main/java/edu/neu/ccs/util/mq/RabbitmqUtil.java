package edu.neu.ccs.util.mq;

/**
 * Created by Administrator on 2018/3/2.
 */

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitmqUtil {
    private static String CONNECTION_VIR_URL;
    private static String CONNECTION_URL ;
    private static int CONNECTION_PORT;
    private static String CONNECTION_USERNAME;
    private static String CONNECTION_PASSWORD;

    static {
        CONNECTION_VIR_URL = RabbitmqUtil.CONNECTION_VIR_URL;
        CONNECTION_URL = RabbitmqUtil.CONNECTION_URL;
        CONNECTION_PORT = RabbitmqUtil.CONNECTION_PORT;
        CONNECTION_USERNAME = RabbitmqUtil.CONNECTION_USERNAME;
        CONNECTION_PASSWORD = RabbitmqUtil.CONNECTION_PASSWORD;

    }

    public RabbitmqUtil() {
    }

    public static Connection getInstance() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setVirtualHost(CONNECTION_VIR_URL);
        factory.setHost(CONNECTION_URL);
        factory.setPort(CONNECTION_PORT);
        factory.setUsername(CONNECTION_USERNAME);
        factory.setPassword(CONNECTION_PASSWORD);
        Connection conn = null;

        try {
            conn = factory.newConnection();
        } catch (IOException var3) {
            var3.printStackTrace();
        } catch (TimeoutException var4) {
            var4.printStackTrace();
        }

        return conn;
    }

    public static void main(String[] args) {
        Connection conn = getInstance();
        System.out.println(conn);

        try {
            try {
                conn.close();
            } catch (IOException var6) {
                var6.printStackTrace();
            }

        } finally {
            ;
        }
    }
}
