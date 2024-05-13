package com.blog.bean;

public class Article {
    public int id;
    private String title;
    private String descr;
    private String time;

    public Article() { super(); }

    public void setArticle(int id, String title, String descr, String time) {
        if (descr.length() > 150) {
            descr = descr.substring(0, 147) + "...";
        }
        if (title.length() > 35) {
            title = title.substring(0, 32) + "...";
        }
        this.id = id;
        this.title = title;
        this.descr = descr;
        this.time = time;
    }

    public int getId() { return this.id; }
    public String getTitle() { return this.title; }
    public String getDescription() { return this.descr; }
    public String getArticleTime() { return this.time; }
}
