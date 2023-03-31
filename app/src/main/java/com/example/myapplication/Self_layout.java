package com.example.myapplication;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.io.BufferedWriter;
import java.io.FileOutputStream;

public class Self_layout extends LinearLayout {

    public static EditText editText;
    public static String apikey;
    public static Boolean api_changed = false;
    public Self_layout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.title,this);
/*        Button bt0 = (Button) findViewById(R.id.titleBar_left_btn);
        bt0.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity)getContext()).finish();
            }
        });*/
        Button bt1 = (Button) findViewById(R.id.titleBar_left_btn);
        bt1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("Quit");
                dialog.setMessage("确定退出吗？");
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Activity_contorller.end_the_application();
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(context, "Canceled!", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();
            }
        });

        Button bt2 = (Button) findViewById(R.id.titleBar_right_btn);
        bt2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setIcon(R.drawable.option);
                dialog.setTitle("Option");
                View view1 = LayoutInflater.from(context).inflate(R.layout.option, null);
                dialog.setView(view1);
                editText = (EditText)view1.findViewById(R.id.apikey);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        apikey =  editText.getText().toString();
                        if(apikey.length()==0)
                        {
                            Toast.makeText(context,"apikey不能为空",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            api_changed = true;
                            Toast.makeText(context,"apikey设置成功！",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        api_changed = false;
                        Toast.makeText(context, "Canceled!", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();
            }
        });
    }

}

