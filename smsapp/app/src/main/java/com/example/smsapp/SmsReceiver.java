package com.example.smsapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.provider.Telephony.Sms.Intents;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;

class SmsReceiver extends BroadcastReceiver {

    private static final String TAG = "SmsReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (TextUtils.equals(intent.getAction(), Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {

            SmsMessage[] messages = Intents.getMessagesFromIntent(intent);

            HashSet<String> set = new HashSet<>();

            for (SmsMessage message : messages) {

                String msg = message.getDisplayMessageBody();

                if (set.contains(msg)) {
                    continue;
                }

                set.add(msg);

                Log.e(TAG, "收到短信：" + msg);

                new Thread(() -> {


                    // 处理短信内容（耗时操作需切子线程）

                    String err = SmsPoster.postToServer(context, message.getDisplayMessageBody());

                    Log.e(TAG, "post result :" + err);

//                    Toast.makeText(context, err, Toast.LENGTH_SHORT).show();


                }).start();
            }
        }
    }

}


