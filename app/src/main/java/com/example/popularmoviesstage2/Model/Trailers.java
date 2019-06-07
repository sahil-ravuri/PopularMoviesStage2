package com.example.popularmoviesstage2.Model;

import android.support.v7.widget.RecyclerView;

public class Trailers {
    private String name;
    private String site;
    private String key;
    private String url;

    public Trailers(String name, String site, String key) {
        this.name = name;
        this.site = site;
        this.key = key;
        this.url = "https://www.youtube.com/watch?v=" + key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
