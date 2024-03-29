package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class BasicActivity extends AppCompatActivity{

    public static int UrlApiKey = 0;
    public static int UrlUserPhoto = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Activity_contorller.add_activities(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Activity_contorller.remove_activities(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.addit:
                Toast.makeText(this,"此功能暂未开放",Toast.LENGTH_SHORT).show();
                break;
            case R.id.removeit:
                Toast.makeText(this,"此功能暂未开放！",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return true;
    }
    public void SaveString(String data,int type){
        FileOutputStream fileOutputStream;
        BufferedWriter bufferedWriter = null;

        try {
            if(type == UrlApiKey)//apikey
                fileOutputStream = openFileOutput("apikey",Context.MODE_PRIVATE);
            else if(type == UrlUserPhoto)//头像
                fileOutputStream = openFileOutput("userphoto",Context.MODE_PRIVATE);
            else
                fileOutputStream = null;
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
            bufferedWriter.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(bufferedWriter!=null)
                    bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String Load_String(int type){
        FileInputStream inputStream = null;
        BufferedReader bufferedReader = null;
        StringBuilder context = new StringBuilder();

        try {
            if(type == UrlApiKey)
                inputStream = openFileInput("apikey");
            else if(type == UrlUserPhoto)
                inputStream = openFileInput("userphoto");
            else
                inputStream = null;
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while((line = bufferedReader.readLine())!=null)
            {
                context.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(bufferedReader!=null)
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return context.toString();
    }
}