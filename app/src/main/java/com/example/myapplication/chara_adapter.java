package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class chara_adapter extends RecyclerView.Adapter<chara_adapter.View_holder> {

    private List<chara> insidelist;

     static class View_holder extends RecyclerView.ViewHolder{
         View chara_view;
        chara_temp charaTemp;
        public View_holder(@NonNull View itemView) {
            super(itemView);
            chara_view = itemView;
            charaTemp = new chara_temp();
            charaTemp.chara_img = itemView.findViewById(R.id.chara_img);
            charaTemp.char_name = itemView.findViewById(R.id.char_name);
            charaTemp.char_sex = itemView.findViewById(R.id.char_sex);
            charaTemp.char_personality = itemView.findViewById(R.id.char_personality);
            charaTemp.chat_button = itemView.findViewById(R.id.chat_button);
            /*charaTemp.char_introduction = itemView.findViewById(R.id.char_introduction);*/
        }
    }

    public chara_adapter(List<chara> charalist)
    {
        insidelist = charalist;
    }

    @NonNull
    @Override
    public View_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chara_item,parent,false);
        final View_holder view_holder = new View_holder(view);
        view_holder.charaTemp.chara_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = view_holder.getAdapterPosition();
                Intent it1 = new Intent(view.getContext(),MainActivity4.class);
                it1.putExtra("name",insidelist.get(position).getName());
                it1.putExtra("introduction",insidelist.get(position).getIntroduction());
                view.getContext().startActivity(it1);
            }
        });
        view_holder.charaTemp.chat_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = view_holder.getAdapterPosition();
                Intent it2 = new Intent(view.getContext(),TalkingActivity.class);
                it2.putExtra("name",insidelist.get(position).getName());
                it2.putExtra("sex",insidelist.get(position).getSex());
                it2.putExtra("chara_photo",insidelist.get(position).getImage_id());
                it2.putExtra("Personality",insidelist.get(position).getPersonality());
                view.getContext().startActivity(it2);
            }
        });
        return view_holder;
    }

    @Override
    public void onBindViewHolder(@NonNull View_holder holder, int position) {
        chara chara = insidelist.get(position);
        holder.charaTemp.chara_img.setImageResource(chara.getImage_id());
        holder.charaTemp.char_name.setText("姓名："+chara.getName());
        holder.charaTemp.char_sex.setText("性别："+chara.getSex());
        holder.charaTemp.char_personality.setText("特性："+chara.getPersonality());
        /*holder.charaTemp.char_introduction.setText("简介：\n"+chara.getIntroduction());*/
    }

    @Override
    public int getItemCount() {
        return insidelist.size();
    }

/*public int get_position()
{
    return position;
}*/
/*    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        chara chara = getItem(position);
        View view;
        view_holder viewHolder;
        if(convertView == null)
        {
            view = LayoutInflater.from(getContext()).inflate(resourse_id,parent,false);
            viewHolder = new view_holder();
            viewHolder.chara_img = (ImageView) view.findViewById(R.id.chara_img);
            viewHolder.char_name = (TextView) view.findViewById(R.id.char_name);
            viewHolder.char_sex = (TextView) view.findViewById(R.id.char_sex);
            viewHolder.char_personality = (TextView) view.findViewById(R.id.char_personality);
            viewHolder.char_introduction = (TextView) view.findViewById(R.id.char_introduction);
            view.setTag(viewHolder);
        }
        else
        {
            view = convertView;
            viewHolder = (view_holder)view.getTag();
        }
        viewHolder.chara_img.setImageResource(chara.getImage_id());
        viewHolder.char_name.setText("姓名："+chara.getName());
        viewHolder.char_sex.setText("性别："+chara.getSex());
        viewHolder.char_personality.setText("品质："+chara.getPersonality());
        viewHolder.char_introduction.setText("简介："+chara.getIntroduction());
        return view;
    }*/

}

class chara_temp{
    public TextView char_name;
    public TextView char_sex;
    public TextView char_personality;
   // public TextView char_introduction;

    public ImageView chara_img;
    public Button chat_button;
}