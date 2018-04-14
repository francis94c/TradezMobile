package com.fz5.tradezmobile.model;

/**
 * Created by Francis Ilechukwu 03/04/2018.
 */

public class SubCategory {

    private int id;
    private int categoryId;
    private String name;

    public SubCategory(int id, int categoryId, String name) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

}
