package se.coolaganget.friendzip.model;

import java.io.Serializable;

public class Friend implements Serializable {
    public String id;
    public String name;


    public Friend(String id, String name) {
        this.id = id;
        this.name = name;
    }

}
