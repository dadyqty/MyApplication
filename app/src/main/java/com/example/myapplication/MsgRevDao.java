package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MsgRevDao {
    private MsgRevHelper msgRevHelper;
    public MsgRevDao(Context context){
        msgRevHelper=new MsgRevHelper(context);
    }
    /**
     * 添加一条记录
     */
    public void add(MsgInfo msgInfo){
        //1.得到连接
        SQLiteDatabase database = msgRevHelper.getReadableDatabase();
        //2.执行Insert  insert into black_number (number) values (xxx)
        ContentValues values = new ContentValues();
        values.put("name",msgInfo.getName());
        values.put("msg", msgInfo.getMsg());
        values.put("sex",msgInfo.getSex());
        values.put("position",msgInfo.getPosition());
        values.put("chara_photo",msgInfo.getChara_photo());
        if(msgInfo.getIs_send_by_me())
            values.put("is_send_by_me","true");
        else
            values.put("is_send_by_me","false");
        long id = database.insert("Chat_info", null, values);
        Log.e("TAG", "id=" + id);
        //设置id
        msgInfo.setId((int) id);
        //3.关闭
        database.close();
    }
    /**
     * 根据id删除一条记录
     */
    public void deleteByName(String name) {
        //1.得到连接
        SQLiteDatabase database = msgRevHelper.getReadableDatabase();
        //2.执行delete delete from black_number where _id=id
        int deleteCount = database.delete("Chat_info", "name=?", new String[]{name});
        Log.e("TAG", "deleteCount=" + deleteCount);
        //3.关闭
        database.close();
    }
    /***
     * 更新一条记录
     *
     */
    public void update(MsgInfo msgInfo) {
        //1.得到连接
        SQLiteDatabase database = msgRevHelper.getReadableDatabase();
        //2.执行update update black_number set number=xxx where _id=id
        ContentValues values = new ContentValues();
        values.put("name",msgInfo.getName());
        values.put("msg", msgInfo.getMsg());
        values.put("sex",msgInfo.getSex());
        values.put("position",msgInfo.getPosition());
        values.put("chara_photo",msgInfo.getChara_photo());
        if(msgInfo.getIs_send_by_me())
            values.put("is_send_by_me","true");
        else
            values.put("is_send_by_me","false");
        int updateCount = database.update("Chat_info", values, "_id=" + msgInfo.getId(), null);
        Log.e("TAG", "updateCount=" + updateCount);
        //3.关闭
        database.close();
    }
    /**
     * 查询所有记录封装成List<MsgInfo>
     */
    public List<MsgInfo> getChat(String name) {
        List<MsgInfo> list= new ArrayList<MsgInfo>();
        //1.得到连接
        SQLiteDatabase database = msgRevHelper.getReadableDatabase();
        //2.执行query select * from black number
        //查询根据_id 实现倒序
        Cursor cursor = database.query("Chat_info",null, "name=?", new String[]{name}, null, null, null);
        //3.从cursor中取出所有数据并封装到List中
        while (cursor.moveToNext()){
            //id
            int id=cursor.getInt(0);
            //number
            @SuppressLint("Range") String name1=cursor.getString(cursor.getColumnIndex("name"));
            @SuppressLint("Range") String msg=cursor.getString(cursor.getColumnIndex("msg"));
            @SuppressLint("Range") String sex=cursor.getString(cursor.getColumnIndex("sex"));
            @SuppressLint("Range") int position=cursor.getInt(cursor.getColumnIndex("position"));
            @SuppressLint("Range") int chara_photo=cursor.getInt(cursor.getColumnIndex("chara_photo"));
            @SuppressLint("Range") String is_send_by_me = cursor.getString(cursor.getColumnIndex("is_send_by_me"));
            boolean issendbyme;
            if(is_send_by_me.equals("true"))
                issendbyme = true;
            else
                issendbyme = false;
            list.add(new MsgInfo(name1,msg,sex,position,chara_photo,issendbyme));
        }
        //3.关闭
        cursor.close();
        database.close();
        return list;
    }

}
