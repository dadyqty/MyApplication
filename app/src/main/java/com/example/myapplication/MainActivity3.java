package com.example.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity3 extends BasicActivity {

    private List<chara> chara_list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.third_layout);
/*        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.hide();
        }*/
        init_chara();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list_item);
       // StaggeredGridLayoutManager linearLayoutManager = new  StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.HORIZONTAL);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        //添加Android自带的分割线
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        chara_adapter charaAdapter = new chara_adapter(chara_list);
        recyclerView.setAdapter(charaAdapter);
/*        Button bt = (Button) findViewById(R.id.chat_button);
        bt.setOnClickListener(this);*/

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /*    @Override
    public void onClick(View view) {
        Activity_contorller.end_the_application();
    }*/

    private void init_chara()
    {
        chara athor = new chara("亚瑟·摩根","男","基于gpt-3.5-turbo模型",chara.information[0], R.drawable.athor);
        chara_list.add(athor);
        chara john = new chara("约翰·马斯顿","男","基于text-davinci-003模型",chara.information[2],R.drawable.john);
        chara_list.add(john);
        chara dc = new chara("达奇·范德林德","男","基于text-davinci-edit-001模型",chara.information[3],R.drawable.dc);
        chara_list.add(dc);
        chara he = new chara("何西阿·马修斯","男","Creates image",chara.information[4],R.drawable.he);
        chara_list.add(he);
        chara mk = new chara("迈卡·贝尔","男","whisper-1",chara.information[5],R.drawable.mk);
        chara_list.add(mk);
        chara cs = new chara("查尔斯·史密斯","男","基于gpt-3.5-turbo模型",chara.information[6],R.drawable.cs);
        chara_list.add(cs);
        chara bill = new chara("比尔·威廉姆森","男","基于gpt-3.5-turbo模型",chara.information[1],R.drawable.bill);
        chara_list.add(bill);
        chara ps = new chara("史密斯·皮尔逊","男","基于gpt-3.5-turbo模型",chara.information[7],R.drawable.ps);
        chara_list.add(ps);
        chara uc = new chara("大叔","男","基于gpt-3.5-turbo模型",chara.information[8],R.drawable.uc);
        chara_list.add(uc);
        chara ln = new chara("蓝尼·萨默斯","男","基于gpt-3.5-turbo模型",chara.information[9],R.drawable.ln);
        chara_list.add(ln);
        chara hw = new chara("哈维尔·埃斯奎","男","基于gpt-3.5-turbo模型",chara.information[10],R.drawable.hw);
        chara_list.add(hw);
        chara so = new chara("西恩·马奎尔","男","基于gpt-3.5-turbo模型",chara.information[22],R.drawable.so);
        chara_list.add(so);
        chara tr = new chara("约西亚·特里劳尼","男","基于gpt-3.5-turbo模型",chara.information[11],R.drawable.tr);
        chara_list.add(tr);
        chara st = new chara("利奥波德·施特劳斯","男","基于gpt-3.5-turbo模型",chara.information[12],R.drawable.st);
        chara_list.add(st);
        chara ss = new chara("奥维尔·斯旺森","男","基于gpt-3.5-turbo模型",chara.information[13],R.drawable.ss);
        chara_list.add(ss);
        chara jack = new chara("杰克·马斯顿","男","基于gpt-3.5-turbo模型",chara.information[14],R.drawable.jack);
        chara_list.add(jack);
        chara gs = new chara("苏珊·格里姆肖","女","基于gpt-3.5-turbo模型",chara.information[15],R.drawable.gs);
        chara_list.add(gs);
        chara sa = new chara("莎迪·阿德勒","女","基于gpt-3.5-turbo模型",chara.information[16],R.drawable.sa);
        chara_list.add(sa);
        chara ab = new chara("阿比盖尔·罗伯茨","女","基于gpt-3.5-turbo模型",chara.information[17],R.drawable.ab);
        chara_list.add(ab);
        chara mb = new chara("玛丽·贝思","女","基于gpt-3.5-turbo模型",chara.information[18],R.drawable.mb);
        chara_list.add(mb);
        chara kl = new chara("凯伦·琼斯","女","基于gpt-3.5-turbo模型",chara.information[19],R.drawable.kl);
        chara_list.add(kl);
        chara tl = new chara("蒂莉·杰克逊","女","基于gpt-3.5-turbo模型",chara.information[20],R.drawable.tl);
        chara_list.add(tl);
        chara ma = new chara("茉莉·奥谢","女","基于gpt-3.5-turbo模型",chara.information[21],R.drawable.ma);
        chara_list.add(ma);
    }

}