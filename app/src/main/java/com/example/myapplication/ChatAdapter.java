package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.Spanned;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private List<Message> messages;
    private String sex;
    private int chara_photo;
    public String name;
    public String userphoto = "";
    public static Bitmap bitmap;
    public boolean bitmapisok = false;
    public Bitmap userphotobitmap;
    public static Resources resources;
    public static Drawable drawable;
    public Button button_send ;
    public Activity activity;
    private View.OnLongClickListener longClickListener;

    public ChatAdapter(List<Message> messages, String sex, String name,int chara_photo,View.OnLongClickListener longClickListener,Resources resources) {
        this.messages = messages;
        this.sex = sex;
        this.chara_photo = chara_photo;
        this.name = name;
        this.longClickListener = longClickListener;
        this.resources = resources;
    }

/*    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if (message.isSentByMe()) {
            return R.layout.sent_message_layout;
        } else {
            return R.layout.received_message_layout;
        }
    }*/

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view_send = LayoutInflater.from(parent.getContext()).inflate(R.layout.sent_message_layout, parent, false);
            final ViewHolder view_holder = new ViewHolder(view_send);
            view_holder.chara_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "哎哟，你干嘛！", Toast.LENGTH_SHORT).show();
            }
        });
        view_holder.huaji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),UserActivity.class);
                view.getContext().startActivity(intent);
            }
        });
        return new ViewHolder(view_send);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.messageTextView.setGravity(Gravity.LEFT);
        holder.chara_photo.setImageResource(chara_photo);
        if (message.isSentByMe() == true) {
            holder.send_layout.setGravity(Gravity.RIGHT);
            holder.chara_photo.setVisibility(View.INVISIBLE);
            holder.huaji.setVisibility(View.VISIBLE);
            holder.huaji.setImageBitmap(userphotobitmap);
            holder.messageTextView.setBackgroundResource(R.drawable.send);
        }
        else {
            holder.send_layout.setGravity(Gravity.LEFT);
            holder.chara_photo.setVisibility(View.VISIBLE);
            holder.huaji.setVisibility(View.INVISIBLE);
            if(sex.equals("男")) {
                holder.messageTextView.setBackgroundResource(R.drawable.man);
            }
            else {
                holder.messageTextView.setBackgroundResource(R.drawable.woman);
            }
        }
                if(message.getimgflg() == true)
                {
                    holder.url_img.setBackgroundResource(R.drawable.man);
                    holder.url_img.setImageBitmap(message.getBitmap());
                    bitmap = message.getBitmap();
                    holder.url_img.setOnCreateContextMenuListener(activity);
                }
                    holder.messageTextView.setText(message.getText());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView messageTextView;
        public LinearLayout send_layout;
        public ImageView huaji;
        public ImageView chara_photo;
        public ImageView url_img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.message_text_view);
            send_layout = itemView.findViewById(R.id.send_layout);
            huaji = itemView.findViewById(R.id.huaji);
            url_img = itemView.findViewById(R.id.url_img);
            chara_photo = itemView.findViewById(R.id.char_photo);
            messageTextView.setOnLongClickListener(longClickListener);


        }
    }


    public static Bitmap getBitmap(String netUrl){
        URL url = null;
        try {
            url = new URL(netUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
                InputStream inputStream = conn.getInputStream();
                byte[] data = getBytes(inputStream);
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                return bitmap;
            }else {
                Bitmap bitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Bitmap bitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            return bitmap;
        }
    }

    public static byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream outstream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024]; // 用数据装
        int len = -1;
        while ((len = is.read(buffer)) != -1) {
            outstream.write(buffer, 0, len);
        }
        outstream.close();
        // 关闭流一定要记得。
        return outstream.toByteArray();
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
    }
