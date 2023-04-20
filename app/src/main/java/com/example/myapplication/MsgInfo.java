package com.example.myapplication;


public class MsgInfo  {
    private int id;
    private String name;
    private String msg;
    private String sex;
    private int position;
    private int chara_photo;
    private boolean is_send_by_me;


    public MsgInfo( String name, String msg, String sex, int position, int chara_photo , boolean is_send_by_me) {
        this.name = name;
        this.msg = msg;
        this.sex = sex;
        this.position = position;
        this.chara_photo = chara_photo;
        this.is_send_by_me = is_send_by_me;
    }

    protected MsgInfo() {

    }

    public int getId() {
        return id;
    }

    public boolean getIs_send_by_me()
    {
        return is_send_by_me;
    }

    public void getIs_send_by_me(boolean is_send_by_me) {
        this.is_send_by_me = is_send_by_me;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getChara_photo() {
        return chara_photo;
    }

    public void setChara_photo(int chara_photo) {
        this.chara_photo = chara_photo;
    }

    @Override
    public String toString() {
        return "MsgInfo{" +
                "id=" + id +
                "name"+name+
                "sex"+sex+
                "msg"+msg+
                "position"+position+
                "chara_photo"+chara_photo+
                '}';
    }

}
