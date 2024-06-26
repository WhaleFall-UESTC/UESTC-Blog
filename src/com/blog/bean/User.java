package com.blog.bean;

public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private int authority;

    public User(){
        super();
    }

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }
    public String getSId() {
        String sid = Integer.toString(id);
        String pad = "";
        if (id < 10) pad = "0";
        return pad + sid;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getConsole() { return name + "'s Console"; }

    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }

    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }

    public int getAuthority() {return authority;}
    public void setAuthority(int authority) {this.authority = authority;}
}
