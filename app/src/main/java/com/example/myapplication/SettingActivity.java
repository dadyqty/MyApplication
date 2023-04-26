package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends BasicActivity implements View.OnClickListener{
    RelativeLayout apikey,option2,option3,option4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
         apikey = findViewById(R.id.apikey);
         option2 = findViewById(R.id.option2);
         option3 = findViewById(R.id.option3);
         option4 = findViewById(R.id.option4);

        apikey.setOnClickListener(this);
        option2.setOnClickListener(this);
        option3.setOnClickListener(this);
        option4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.apikey:
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setIcon(R.drawable.option);
                dialog.setTitle("Option");
                View view1 = LayoutInflater.from(this).inflate(R.layout.option, null);
                dialog.setView(view1);
                EditText editText = (EditText)view1.findViewById(R.id.set_apikey);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String apikey =  editText.getText().toString();
                        if(apikey.length()==0)
                        {
                            Toast.makeText(SettingActivity.this,"apikey不能为空",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            SaveString(apikey,BasicActivity.UrlApiKey);
                            Toast.makeText(SettingActivity.this,"apikey设置成功！",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(SettingActivity.this, "Canceled!", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();
                break;
            case R.id.option2:
                Toast.makeText(this, "暂未开发", Toast.LENGTH_SHORT).show();
                break;
            case R.id.option3:
                Toast.makeText(this, "暂未开发", Toast.LENGTH_SHORT).show();
                break;
            case R.id.option4:
                Toast.makeText(this, "暂未开发", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}