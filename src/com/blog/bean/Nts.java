package com.blog.bean;

public class Nts {
    private int id;
    private String nts;
    public Nts(){super();}

    public String getNts(){
        return nts;
    }
    public void setNts(String nts){
        this.nts = nts;
    }

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }

}