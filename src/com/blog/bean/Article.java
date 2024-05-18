package com.blog.bean;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Article {
    public int id;
    private String title;
    private String descr;
    private String time;
    private String content;

    public Article() { super(); }

    public void newArticle(String title, String descr, String content) {
        this.title = title;
        this.descr = descr;
        this.content = content;
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.time = now.format(formatter);
    }

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

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() { return this.id; }
    public String getTitle() { return this.title; }
    public String getDescription() { return this.descr; }
    public String getArticleTime() { return this.time; }
    public String getContent() { return this.content; }
}
