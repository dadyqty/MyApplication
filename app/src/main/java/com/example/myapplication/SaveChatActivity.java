package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SaveChatActivity extends BasicActivity {

    private String name;
    private ChatAdapter adapter;
    private String sex;
    private int chara_photo;
    private List<Message> messages = new ArrayList<>();
    private MsgRevDao msgRevDao;

    private final View.OnLongClickListener lc = v -> {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", ((TextView) v).getText());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getApplicationContext(), "复制成功", Toast.LENGTH_SHORT).show();
        return false;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talking);
        msgRevDao = new MsgRevDao(SaveChatActivity.this);
        TextView textView = findViewById(R.id.chat_name);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        sex = intent.getStringExtra("sex");
        chara_photo = intent.getIntExtra("chara_photo",R.drawable.athor);
        textView.setText("与"+name+"的聊天记录");
        EditText editText =findViewById(R.id.send_message);
        editText.setVisibility(View.INVISIBLE);
        Button button = findViewById(R.id.send_button);
        button.setVisibility(View.INVISIBLE);
        button = findViewById(R.id.save_button);
        button.setBackgroundResource(R.drawable.delete);


        RecyclerView recyclerView;
        recyclerView = findViewById(R.id.chat_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ChatAdapter(messages,sex,name,chara_photo,lc,this.getResources());
        adapter.userphoto = Load_String(UrlUserPhoto);
        adapter.activity = SaveChatActivity.this;
        recyclerView.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SaveChatActivity.this);
                builder.setTitle("删除聊天记录");
                builder.setMessage("确定删除聊天记录吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        msgRevDao.deleteByName(name);
                        messages.clear();
                        recyclerView.scrollToPosition(messages.size());
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(SaveChatActivity.this, "Canceled!", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });

        List<MsgInfo> msgInfo = msgRevDao.getChat(name);

        for (int i = 0; i < msgInfo.size(); i++) {
            messages.add(new Message(msgInfo.get(i).getMsg(),msgInfo.get(i).getIs_send_by_me(),false));
        }

        adapter.notifyItemInserted(messages.size()-1);
        recyclerView.scrollToPosition(messages.size()-1);

    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.userphotobitmap = adapter.stringToBitmap(adapter.userphoto);
    }
}