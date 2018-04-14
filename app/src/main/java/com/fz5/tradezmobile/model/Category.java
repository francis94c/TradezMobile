package com.fz5.tradezmobile.model;

/**
 * Created by Francis Ilechukwu 02/04/2018.
 */

public class Category {

    int id;
    String name;

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
