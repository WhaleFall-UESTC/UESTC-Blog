package com.blog.bean;

import com.blog.bean.User;
import com.blog.dao.UserDAO;

public class Msg {
    private int id;
    private String msg;
    private int userId;
    public Msg(){super();}

    public String getMsg(){
        return this.msg;
    }
    public void setMsg(String Msg){
        this.msg = Msg;
    }

    public int getId(){ return this.id; }
    public void setId(int Id){
        this.id = Id;
    }

    public int getUserId() { return this.userId; }
    public void setUserId(int UserId) { this.userId = UserId; }

    public User getTheUser() { return new UserDAO().getUserById(this.userId); }
    public String getUserName() { return getTheUser().getName(); }
    public String getImgPath() { return "img/" + getUserName() + ".jpg"; }
}
