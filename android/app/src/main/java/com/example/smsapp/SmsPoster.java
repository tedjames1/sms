package com.example.smsapp;

import android.content.Context;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SmsPoster {

    private static final String USER_AGENT = "Mozilla/5.0";

    public static String postToServer(Context ctx, String msg) {

        StringBuilder response = new StringBuilder();

        try {


            String phone = MyApplication.phone;

            // 创建URL对象
            assert phone != null;

            String postUrl = MyApplication.host + "/api/v1/sms/" + phone;

            URL url = new URL(postUrl);

            // 创建HttpURLConnection对象
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // 设置请求方法为POST
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(30000);

            // 设置请求头
            connection.setRequestProperty("User-Agent", USER_AGENT);
            connection.setRequestProperty("Content-Type", "application/json");

            // 启用输出流，以便我们可以将请求体写入连接
            connection.setDoOutput(true);

            // 创建请求体数据
            String requestBody = "{\"content\":\"" + msg + "\"}";

            // 获取连接的输出流
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());

            // 将请求体数据写入输出流
            outputStream.writeBytes(requestBody);

            // 关闭输出流
            outputStream.close();


            // 获取响应代码
            int responseCode = connection.getResponseCode();

            response.append("Response Code: " + responseCode);
            response.append("\n");

            // 读取响应体
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // 关闭连接
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            response.append(e);
        }
        return response.toString();
    }

}
