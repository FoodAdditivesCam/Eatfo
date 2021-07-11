package com.myj.foodadditivescam;

import java.io.Serializable;

public class RawMaterials implements Serializable {
    private static final long serialVersionUID = 1L;
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

    public RawMaterials(int id, String name, String description, String tag1, String tag2, String tag3, String tag4, String tag5, String reference, String link){
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

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getDescription(){
        return description;
    }

    public String getReference(){
        return reference;
    }

    public String getLink(){
        return link;
    }

    public String getTags(){
        String res="";

        if(!tag1.equals("null")&&!tag1.equals("")){
            res+=tag1;
            res+=" ";
        }
        if(!tag2.equals("null")&&!tag2.equals("")){
            res+=tag2;
            res+=" ";
        }
        if(!tag3.equals("null")&&!tag3.equals("")){
            res+=tag3;
            res+=" ";
        }
        if(!tag4.equals("null")&&!tag4.equals("")){
            res+=tag4;
            res+=" ";
        }
        if(!tag5.equals("null")&&!tag5.equals("")){
            res+=tag5;
        }
        return res;
    }
}
