package com.example.mohamed_araby.newsappupgrade;

/**
 * Created by Mohamed_Araby on 4/6/2018.
 */
public class NewsData {
    private String title;
    private String type;
    private String date;
    private String author;
    private String webUrl;

    public NewsData(String title, String type, String date, String author, String webUrl) {
        this.title = title;
        this.type = type;
        this.date = date;
        this.author = author;
        this.webUrl = webUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public String getauthor() {
        return author;
    }

    public String getWebUrl() {
        return webUrl;
    }
}
