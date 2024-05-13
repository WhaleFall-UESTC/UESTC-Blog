package com.blog.bean;

public class PreInviter {
    private int id;
    private String name;
    private String email;
    private int userid;

    public PreInviter(){
        super();
    }

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }

    public int getUserid() {return userid;}
    public void setUserid(int userid) {this.userid = userid;}
}
