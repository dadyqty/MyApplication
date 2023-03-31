package com.example.myapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BasicActivity {

    private final String TAG = "MainActivity";
/*    private static int ran2=14692;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
/*        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.hide();
        }*/
/*        EditText txt1 = (EditText) findViewById(R.id.txt1);*/
        ActivityResultLauncher<Intent> intentActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == RESULT_OK && result.getData() != null)
                {
                    /*ran2 = 14692;*/
                    Toast.makeText(MainActivity.this,result.getData().getStringExtra("two2one"),Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button bt0 = (Button) findViewById(R.id.start);
        bt0.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {
                                       MainActivity2.start_2_activity(MainActivity.this, intentActivityResultLauncher, "hello!", "here is one!");
                                       /*AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                                       dialog.setTitle("开始");
                                       dialog.setMessage("你确定要继续吗？");
                                       dialog.setCancelable(false);
                                       dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                           @Override
                                           public void onClick(DialogInterface dialogInterface, int i) {
                                               MainActivity2.start_2_activity(MainActivity.this, intentActivityResultLauncher, "hello!", "here is one!");
                                           }
                                       });
                                       dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                           @Override
                                           public void onClick(DialogInterface dialogInterface, int i) {
                                               Toast.makeText(MainActivity.this,"Canceled",Toast.LENGTH_SHORT).show();
                                           }
                                       });*/
                                       /*String password = txt1.getText().toString();
                                       try {
                                           if (Integer.parseInt(password) == ran2) {
                                               AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                                               dialog.setTitle("开始");
                                               dialog.setMessage("你确定要继续吗？");
                                               dialog.setCancelable(false);
                                               dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                   @Override
                                                   public void onClick(DialogInterface dialogInterface, int i) {
                                                       MainActivity2.start_2_activity(MainActivity.this, intentActivityResultLauncher, "hello!", "here is one!");
                                                   }
                                               });
                                               dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                   @Override
                                                   public void onClick(DialogInterface dialogInterface, int i) {
                                                       Toast.makeText(MainActivity.this,"Canceled",Toast.LENGTH_SHORT).show();
                                                   }
                                               });
                                               dialog.show();
                                           } else
                                               Toast.makeText(MainActivity.this, "输入错误！", Toast.LENGTH_SHORT).show();
                                       } catch (Exception NumberFormatException) {
                                           Toast.makeText(MainActivity.this, "起码是个数字吧！！", Toast.LENGTH_SHORT).show();
                                       }*/
                                   }
                               });
/*
        Button bt1 = (Button) findViewById(R.id.tishi);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int max=1000,min=1;
                ran2 = (int) (Math.random()*(max-min)+min);
                String ran2_hex = Integer.toHexString(ran2);
                System.out.println(ran2);
                Toast.makeText(MainActivity.this,"密码:"+ran2_hex+"的十进制",Toast.LENGTH_SHORT).show();
            }
        });
*/

/*        Button bt2 = (Button) findViewById(R.id.end);
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Button bt3 = (Button) findViewById(R.id.login);
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });*/
    }


}