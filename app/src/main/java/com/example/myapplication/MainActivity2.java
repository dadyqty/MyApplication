package com.example.myapplication;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity2 extends BasicActivity implements View.OnClickListener {
    private final String TAG = "MainActivity2";
    private static int imgcount = 10;
    private static int cnt = 0;
    private static boolean finish = false;
    private int[] imgid = {R.drawable.rdr1,R.drawable.rdr2,R.drawable.rdr3,R.drawable.rdr4,R.drawable.rdr5,R.drawable.rdr6,R.drawable.rdr7,R.drawable.rdr8,R.drawable.rdr9,R.drawable.rdr10};

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
}