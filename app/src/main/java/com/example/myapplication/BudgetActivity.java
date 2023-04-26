package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BudgetActivity extends BasicActivity {

    Double d1,d2,d3;
    private BudgetActivity.MyHandle myHandle = null;

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)       //设置连接超时
            .readTimeout(60, TimeUnit.SECONDS)          //设置读超时
            .writeTimeout(60, TimeUnit.SECONDS)          //设置写超时
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);
        myHandle = new BudgetActivity.MyHandle();
        Button button = findViewById(R.id.chaxun);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account();
            }
        });
    }

    private void account(){
           Request request = new Request.Builder()
                    .url("https://api.openai.com/dashboard/billing/credit_grants")///chat
                    .header("Authorization", "Bearer sess-1UGLXpFl1uZisUBFvRQtYxWsVS7HLMVLLuhjHRbV")
                    .get()
                    .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                System.out.println("请求失败");
                android.os.Message msg = new android.os.Message();
                msg.what = 2;
                myHandle.sendMessage(msg);
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                         d1 = jsonObject.getDouble("total_granted");
                         d2 = jsonObject.getDouble("total_used");
                         d3 = jsonObject.getDouble("total_available");
                        android.os.Message msg = new android.os.Message();
                        msg.what = 1;
                        myHandle.sendMessage(msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    android.os.Message msg = new android.os.Message();
                    msg.what = 2;
                    myHandle.sendMessage(msg);
                }
            }
        });
    }

    private class MyHandle extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what){
                case 1:
                    TextView t1 = findViewById(R.id.total);
                    TextView t2 = findViewById(R.id.use);
                    TextView t3 = findViewById(R.id.remain);
                    t1.setText(t1.getText()+Double.toString(d1));
                    t2.setText(t2.getText()+Double.toString(d2));
                    t3.setText(t3.getText()+Double.toString(d3));
                    break;
                case 2:
                    TextView tx1 = findViewById(R.id.total);
                    TextView tx2 = findViewById(R.id.use);
                    TextView tx3 = findViewById(R.id.remain);
                    tx1.setText(tx1.getText()+"网络忙，请稍后重试");
                    tx2.setText(tx2.getText()+"网络忙，请稍后重试");
                    tx3.setText(tx3.getText()+"网络忙，请稍后重试");
                    break;
            }
        }
    }
}

