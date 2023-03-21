package com.example.myapplication;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private List<Message> messages;
    private String sex;
    private int chara_photo;
    View.OnLongClickListener onLongClickListener;

    public ChatAdapter(List<Message> messages, String sex, int chara_photo,View.OnLongClickListener longClickListener) {
        this.messages = messages;
        this.sex = sex;
        this.onLongClickListener = longClickListener;
        this.chara_photo = chara_photo;
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
        return new ViewHolder(view_send);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.messageTextView.setGravity(Gravity.LEFT);
        holder.chara_photo.setImageResource(chara_photo);
        if (message.isSentByMe() == true) {
            holder.send_layout.setGravity(Gravity.RIGHT);
            holder.chara_photo.setVisibility(View.INVISIBLE);
            /*LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.messageTextView.getLayoutParams();
            lp.gravity = Gravity.RIGHT;  //这才是布局文件中的Android:layout_gravity属性
            holder.messageTextView.setLayoutParams(lp);*/
/*            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            int width = displayMetrics.widthPixels;
            ViewGroup.LayoutParams params = holder.messageTextView.getLayoutParams();
            params.width = width/3;
            params.height = (int)(200f/600 *params.width);*/
            holder.messageTextView.setBackgroundResource(R.drawable.send);
        }
        else {
            holder.send_layout.setGravity(Gravity.LEFT);
            holder.huaji.setVisibility(View.INVISIBLE);
/*            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            int width = displayMetrics.widthPixels;
            ViewGroup.LayoutParams params = holder.messageTextView.getLayoutParams();
            params.width = width/3;*/
            if(sex.equals("男")) {
                holder.messageTextView.setBackgroundResource(R.drawable.man);
            }
            else {
                holder.messageTextView.setBackgroundResource(R.drawable.woman);
            }
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.message_text_view);
            send_layout = itemView.findViewById(R.id.send_layout);
            huaji = itemView.findViewById(R.id.huaji);
            chara_photo = itemView.findViewById(R.id.char_photo);
            messageTextView.setOnLongClickListener(onLongClickListener);
        }
    }
}
