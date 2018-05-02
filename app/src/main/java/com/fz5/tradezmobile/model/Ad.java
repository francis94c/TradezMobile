package com.fz5.tradezmobile.model;

import java.util.ArrayList;

public class Ad {

    private int id;
    private String title;
    private String location;
    private int userId;
    private int categoryId;
    private int subCategoryId;
    private ArrayList<String> images = new ArrayList<>();

    public Ad(int id, String title, String location, int userId) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.userId = userId;
    }

    public void addImage(String image) {
        images.add(image);
    }

    public String getImage(int index) {
        return images.get(index);
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(int subCategoryId) {
        this.subCategoryId = subCategoryId;
    }
}
