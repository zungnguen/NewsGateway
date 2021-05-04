package com.example.newsgatewayfinal;

import java.io.Serializable;

public class NewsArticle implements Serializable {

    private String author;
    private String title;
    private String description;
    private String imageurl;
    private String datepub;
    private String url;

    public NewsArticle(String author, String title, String description, String imageurl, String datepub, String url){
        this.author = author;
        this.title = title;
        this.description = description;
        this.imageurl = imageurl;
        this.datepub = datepub;
        this.url = url;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }


    public String getDescription() {
        return description;
    }

    public String getImageurl() {
        return imageurl;
    }


    public String getDatepub() {
        return datepub;
    }


    public String getUrl() {
        return url;
    }


}