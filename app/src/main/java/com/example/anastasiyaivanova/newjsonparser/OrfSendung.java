package com.example.anastasiyaivanova.newjsonparser;

/**
 * Created by anastasiyaivanova on 15.12.17.
 */

public class OrfSendung {

    private String name;
    private String url;


    public OrfSendung (String name, String url){
        this.name = name;
        this.url = url;

    }

    public String getUrl() {
        return url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
