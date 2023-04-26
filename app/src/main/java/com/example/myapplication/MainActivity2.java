package com.example.myapplication;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class MainActivity2 extends BasicActivity implements View.OnClickListener {
    private final String TAG = "MainActivity2";
    private int imgcount = 12;
    private static int cnt = 0;
    private static boolean finish = false;
    private int[] imgid = {R.drawable.rdr1,R.drawable.rdr2,R.drawable.rdr3,R.drawable.rdr4,R.drawable.rdr5,R.drawable.rdr6,R.drawable.rdr7,R.drawable.rdr8,R.drawable.rdr9,R.drawable.rdr10,R.drawable.pic2,R.drawable.pic1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.hide();
        }
        Intent it0 = getIntent();
        String str0 = it0.getStringExtra("para1");
        String str1 = it0.getStringExtra("para2");
        Log.d(TAG, str0+str1);
        ProgressBar ps = (ProgressBar)findViewById(R.id.progress);
        Button bt0 = (Button) findViewById(R.id.jixu);
        bt0.setOnClickListener(this);

        Button bt1 = (Button) findViewById(R.id.change);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                ps.setProgress((cnt+1)*10);
                if(cnt == imgcount - 1) {
                    cnt = 0;
                    finish = true;
                }
                else
                    cnt++;
                ImageView img = (ImageView) findViewById(R.id.rdr2);
                img.setImageResource(imgid[cnt]);
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent it1 = new Intent(MainActivity2.this,MainActivity3.class);
        startActivity(it1);
/*        if(finish == true)
        {
            Intent it1 = new Intent(MainActivity2.this,MainActivity3.class);
            startActivity(it1);
        }
        else
            Toast.makeText(MainActivity2.this, "别急，还没看完！还差"+Integer.toString(9-cnt)+"张", Toast.LENGTH_SHORT).show();*/
    }

    @Override
    public void onBackPressed() {
        Intent it2 = new Intent();
        it2.putExtra("two2one","好不容易进去，回来干什么!");
        setResult(RESULT_OK,it2);
        finish();
    }

    public static void start_2_activity(Context context, ActivityResultLauncher<Intent> intentActivityResultLauncher, String data1, String data2)
    {
        Intent intent = new Intent(context,MainActivity2.class);
        intent.putExtra("para1",data1);
        intent.putExtra("para2",data2);
        intentActivityResultLauncher.launch(intent);
    }

    /**
     * 将图片转换成十六进制字符串
     * @param photo
     * @return
     *//*
    public static String sendPhoto(ImageView photo) {
        Drawable d = photo.getDrawable();
        Bitmap bitmap=((BitmapDrawable)d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
        int options = 95;
        //如果压缩后的大小超出所要求的，继续压缩
        while (stream.toByteArray().length / 1024 > 50){
            stream.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG,options,stream);
            //每次减少5%质量
            if (options>5){//避免出现options<=0
                options -=5;
            } else {
                break;
            }
        }
        System.out.println("压缩了"+options);
        byte[] bt = stream.toByteArray();
        String photoStr = byte2hex(bt);
        return photoStr;
    }


    *//**
     * 二进制转字符串
     * @param b
     * @return
     *//*
    public static String byte2hex(byte[] b)
    {
        StringBuilder sb = new StringBuilder();
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1) {
                sb.append("0" + stmp);
            } else {
                sb.append(stmp);
            }
        }
        return sb.toString();
    }*/
}