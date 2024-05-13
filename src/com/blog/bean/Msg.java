package com.blog.bean;

public class Msg {
    private int id;
    private String msg;
    public Msg(){super();}

    public String getMsg(){
        return msg;
    }
    public void setMsg(String msg){
        this.msg = msg;
    }

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }

}
