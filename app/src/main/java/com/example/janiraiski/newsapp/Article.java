package com.example.janiraiski.newsapp;

public class Article {

    private String mTitle;
    private String mSection;
    private String mUrl;
    private String mAuthor;
    private String mDate;

    public Article(String title, String section, String url, String author, String date) {
        mTitle = title;
        mSection = section;
        mUrl = url;
        mAuthor = author;
        mDate = date;
    }

    public Article(String title, String section, String url) {
        mTitle = title;
        mSection = section;
        mUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSection() {
        return mSection;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getDate() {
        return mDate;
    }
}

