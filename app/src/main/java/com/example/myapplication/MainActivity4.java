package com.example.myapplication;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity4 extends BasicActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.introduction_layout);
        TextView textView = (TextView)findViewById(R.id.introduction);
        ImageView imageView = findViewById(R.id.introductionpic);
        Intent intent = getIntent();
        String introduction = intent.getStringExtra("introduction");
        String name = intent.getStringExtra("name");
        int Image_id = intent.getIntExtra("Image_id",R.drawable.athor);
        imageView.setImageResource(Image_id);

        textView.setText(name+"的人物简介：\n\n"+introduction);
    }

}