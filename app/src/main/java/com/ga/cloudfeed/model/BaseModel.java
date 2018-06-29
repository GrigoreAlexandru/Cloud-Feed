package com.ga.cloudfeed.model;

import java.util.Date;

public class BaseModel {
    public String link1;
    public final String title, description, image;
    public final Date date;

    public BaseModel(String link, String title, String description, String image, Date date) {
        this.link1 = link;
        this.title = title;
        this.description = description;
        this.image = image;
        this.date = date;
    }
}
