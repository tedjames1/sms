package com.example.smsapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Telephony.Sms.Intents;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String[] PERMISSIONS_REQUEST = {Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS};
    private static final int PERMISSIONS_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initSmsReceiver();

        MyApplication.host = ((TextView) findViewById(R.id.tvHost)).getText().toString();
//        MyApplication.password = ((TextView) findViewById(R.id.tvServerPwd)).getText().toString();

        findViewById(R.id.tvHost).setOnClickListener(v -> {
            Log.d(TAG, "clicked host:" + v.getId() + "");
            modifyDialog(this, v);
        });
//        findViewById(R.id.tvServerPwd).setOnClickListener(v -> {
//            Log.d(TAG, "clicked pwd:" + v.getId() + "");
//            modifyDialog(this, v);
//        });

        // 检查是否已经授予READ_PHONE_STATE权限
        if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            // 获取本机号码
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            MyApplication.phone = telephonyManager.getLine1Number();
            ((TextView) findViewById(R.id.tvPhone)).setText(MyApplication.phone);
        } else {
            // 请求获取READ_PHONE_STATE权限
            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        }

        // for test
        findViewById(R.id.main).setOnClickListener(view -> {

            String phone = MyApplication.phone;
            if (phone == null || phone.length() == 0) {
                Toast.makeText(this, "Please insert sim card", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                String log = SmsPoster.postToServer(getApplicationContext(), "your code is 1234");
                Log.e(TAG, log);
                runOnUiThread(() -> ((TextView) findViewById(R.id.tvLog)).setText(log));
            }).start();
        });

    }

    private static void modifyDialog(Context ctx, View v) {
        final EditText etInput = new EditText(ctx);
        etInput.setLines(1);
        etInput.setText(((TextView) v).getText().toString());
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle("Modify").setView(etInput).setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
        builder.setPositiveButton("确定", (dialog, which) -> {
            String input = etInput.getText().toString();
            Log.d(TAG, "input info:" + v.getId() + "" + input);
            //do something...
            ((TextView) v).setText(input);

            //
            if (v.getId() == R.id.tvHost) {
                MyApplication.host = ((TextView) v).getText().toString();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void initSmsReceiver() {
        // 检查权限，任意一个没满足就重新申请
        if (Arrays.stream(PERMISSIONS_REQUEST).anyMatch(it -> ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS_REQUEST, PERMISSIONS_REQUEST_CODE);
        } else {
            registerSmsReceiver();
        }
    }

    private void registerSmsReceiver() {
        SmsReceiver smsReceiver = new SmsReceiver();
        IntentFilter filter = new IntentFilter(Intents.SMS_RECEIVED_ACTION);
        registerReceiver(smsReceiver, filter);

        if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            // 获取本机号码
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            MyApplication.phone = telephonyManager.getLine1Number();
            ((TextView) findViewById(R.id.tvPhone)).setText(MyApplication.phone);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                registerSmsReceiver();
            } else {
                // 权限被拒绝，重新申请
                initSmsReceiver();
            }
        }
    }


}