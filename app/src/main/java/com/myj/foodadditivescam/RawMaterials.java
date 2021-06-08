package com.myj.foodadditivescam;

public class RawMaterials {
    private int id;
    private String name;
    private String description;
    private String tag1;
    private String tag2;
    private String tag3;
    private String tag4;
    private String tag5;
    private String reference;
    private String link;

    RawMaterials(int id, String name, String description, String tag1, String tag2, String tag3, String tag4, String tag5, String reference, String link){
        this.id = id;
        this.name = name;
        this.description = description;
        this.tag1 = tag1;
        this.tag2 = tag2;
        this.tag3 = tag3;
        this.tag4 = tag4;
        this.tag5 = tag5;
        this.reference = reference;
        this.link = link;
    }

    String getName(){
        return name;
    }

    String getDescription(){
        return description;
    }

    String getReference(){
        return reference;
    }

    String getLink(){
        return link;
    }

    String[] getTags(){
        return new String[]{tag1, tag2, tag3, tag4, tag5};
    }
}
