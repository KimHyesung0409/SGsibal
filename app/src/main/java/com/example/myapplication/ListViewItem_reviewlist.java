package com.example.myapplication;

public class ListViewItem_reviewlist extends ListViewItem {

    private String review_title;
    private String review_content;
    private String review_id;
    private String sitter_id;
    private String sitter_name;
    private double rating;



    public void setReview_title(String review_title) { this.review_title = review_title; }

    public void setReview_content(String review_content) { this.review_content = review_content; }

    public void setReview_id(String review_id) {
        this.review_id = review_id;
    }

    public void setSitter_id(String sitter_id) {
        this.sitter_id = sitter_id;
    }

    public void setSitter_name(String sitter_name) {
        this.sitter_name = sitter_name;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }



    public String getReview_title() { return review_title; }

    public String getReview_content() { return review_content; }

    public String getReview_id() {
        return review_id;
    }

    public String getSitter_id() {
        return sitter_id;
    }

    public String getSitter_name() {
        return sitter_name;
    }

    public double getRating() {
        return rating;
    }

    }












