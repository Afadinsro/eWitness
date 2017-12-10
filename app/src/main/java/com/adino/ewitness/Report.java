package com.adino.ewitness;

/**
 * Created by afadinsro on 12/6/17.
 */

public class Report {
    private ReportCategory category;
    private  String caption;
    private String date;
    private String imageURL;
    private String location;

    public Report(String caption, String date, ReportCategory category, String imageURL, String location) {
        this.date = date;
        this.category = category;
        this.caption = caption;
        this.imageURL = imageURL;
        this.location = location;
    }
    /****************************SETTER METHODS***********************************/
    public void setCategory(ReportCategory category) {
        this.category = category;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    /****************************GETTER METHODS***********************************/
    public ReportCategory getCategory() {
        return category;
    }

    public String getCaption() {
        return caption;
    }

    public String getDate() {
        return date;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getLocation() {
        return location;
    }
}
