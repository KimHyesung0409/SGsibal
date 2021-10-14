package com.example.myapplication.ListViewItem;

public class ListViewItem_entrust_detail_reviewlist extends ListViewItem {
    private String review_user_id;
    private String name;
    private String review_id;
    private String title;
    private double rating;

    public void setReview_user_id(String review_user_id){ this.review_user_id = review_user_id;}
    public void setName(String name){ this.name = name;}
    public void setReview_id(String review_id){ this.review_id = review_id;}
    public void setTitle(String title){ this.title = title;}
    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getReview_user_id(){ return review_user_id; }
    public String getName(){ return name; }
    public String getReview_id(){ return review_id; }
    public String getTitle(){ return title; }
    public Double getRating(){ return rating;}

}
