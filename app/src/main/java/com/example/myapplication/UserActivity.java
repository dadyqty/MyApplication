package com.example.myapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.Date;

public class UserActivity extends BasicActivity implements View.OnClickListener{

    TextView signup,clock_day;//每日签到,显示签到的天数
    RelativeLayout budget_btn,usehelp_btn,about_btn,setting_btn;  //预算中心,使用帮助,关于我们,设置
    de.hdodenhof.circleimageview.CircleImageView circleImageView;
    ActivityResultLauncher<Intent> intentActivityResultLauncher;
    View sigup_view;  //签到弹窗视图
    AlertDialog write;
    SharedPreferences sp;
    SharedPreferences date;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_blank);
        budget_btn = findViewById(R.id.budget_btn);
        usehelp_btn = findViewById(R.id.usehelp_btn);
        about_btn = findViewById(R.id.about_btn);
        setting_btn = findViewById(R.id.setting_btn);
        signup = findViewById(R.id.signup);
        clock_day = findViewById(R.id.clock_day);
        circleImageView = findViewById(R.id.app_img);
        if(Load_String(UrlUserPhoto).length()!=0)
            circleImageView.setImageBitmap(stringToBitmap(Load_String(UrlUserPhoto)));

        budget_btn.setOnClickListener(this);
        usehelp_btn.setOnClickListener(this);
        about_btn.setOnClickListener(this);
        setting_btn.setOnClickListener(this);
        signup.setOnClickListener(this);
        circleImageView.setOnClickListener(this);

        intentActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getData() != null)
                {
                    Uri uri = result.getData().getData();
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                        SaveString(bitmapToString(bitmap),UrlUserPhoto);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    circleImageView.setImageURI(uri);
                    Log.e(this.getClass().getName(), "Uri:" + uri);
                }
            }
        });

        //设置累计签到的天数
        sp =this.getSharedPreferences("tice", Context.MODE_PRIVATE);
        date =this.getSharedPreferences("date", Context.MODE_PRIVATE);
        clock_day.setText(sp.getString("tice","0"));


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signup://签到
                setSignup();
                break;
            case R.id.budget_btn://预算中心
                Intent intent2 = new Intent(this,BudgetActivity.class);
                startActivity(intent2);
                break;
            case R.id.usehelp_btn://使用帮助
                Intent intent3 = new Intent(this,UsehelpActivity.class);
                startActivity(intent3);
                break;
            case R.id.about_btn://关于我们
                Intent intent4 = new Intent(this,AboutActivity.class);
                startActivity(intent4);
                break;
            case R.id.setting_btn://设置
                Intent intent5 = new Intent(this,SettingActivity.class);
                startActivity(intent5);
                break;
            case R.id.app_img://头像
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                intentActivityResultLauncher.launch(intent);
                break;

        }
    }

    //签到功能的实现
    public void setSignup(){

        //显示签到成功视图
        write = new AlertDialog.Builder(this).create();

        Date date1 = new Date(System.currentTimeMillis());
        String day = date1.toString().substring(0,10);
        String year = date1.toString().substring(30);
        //if(date.getString("tice","0"))

        String lastdate = date.getString("date","0");
        if(lastdate.length()!=34) {
            int count = Integer.parseInt(clock_day.getText().toString())+1;
            editor = date.edit();
            editor.putString("date",date1.toString());
            editor.commit(); //写入
            editor = sp.edit();
            editor.putString("tice",Integer.toString(count));
            editor.commit(); //写入
            sigup_view = LayoutInflater.from(this).inflate(R.layout.pop_up_signup,null);
            write.setView(sigup_view);
            write.show();
        }
        else if(lastdate.substring(0,10).equals(day)&&lastdate.substring(30).equals(year))
        {
            sigup_view = LayoutInflater.from(this).inflate(R.layout.pop_up_signup,null);
            TextView textView = sigup_view.findViewById(R.id.signup);
            ImageView imageView = sigup_view.findViewById(R.id.laud);
            imageView.setImageResource(R.drawable.failed);
            textView.setText("重复签到！");
            write.setView(sigup_view);
            write.show();
        }
        else
        {
            int count = Integer.parseInt(clock_day.getText().toString())+1;
            editor = date.edit();
            editor.putString("date",date1.toString());
            editor.commit(); //写入
            editor = sp.edit();
            editor.putString("tice",Integer.toString(count));
            editor.commit(); //写入
            sigup_view = LayoutInflater.from(this).inflate(R.layout.pop_up_signup,null);
            write.setView(sigup_view);
            write.show();
        }
        onResume();//刷新
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sp =this.getSharedPreferences("tice",Context.MODE_PRIVATE);
        clock_day.setText(sp.getString("tice","0"));
    }

    public Bitmap stringToBitmap(String string) {
        // 将字符串转换成Bitmap类型
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
                    bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public String bitmapToString(Bitmap bitmap){
        //将Bitmap转换成字符串
        String string=null;
        ByteArrayOutputStream bStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,90,bStream);
        byte[]bytes=bStream.toByteArray();
        string=Base64.encodeToString(bytes,Base64.DEFAULT);
        return string;
    }
}