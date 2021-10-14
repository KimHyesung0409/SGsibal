package com.example.myapplication.ListViewItem;

/*
 * 후기 요소 ListViewItem 클래스
 * 후기 제목
 * 후기 내용
 * 후기 id
 * 펫시터 user_id
 * 펫시터 이름
 * 후기 평점
 */

public class ListViewItem_reviewlist extends ListViewItem {

    private String review_title;
    private String review_content;
    private String review_id;
    private String user_id;
    private String user_name;
    private double rating;
    private boolean readOnly = false;



    public void setReview_title(String review_title) { this.review_title = review_title; }

    public void setReview_content(String review_content) { this.review_content = review_content; }

    public void setReview_id(String review_id) {
        this.review_id = review_id;
    }

    public void setUser_id(String sitter_id) {
        this.user_id = sitter_id;
    }

    public void setUser_name(String sitter_name) {
        this.user_name = sitter_name;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setReadOnly(boolean readOnly)
    {
        this.readOnly = readOnly;
    }



    public String getReview_title() { return review_title; }

    public String getReview_content() { return review_content; }

    public String getReview_id() {
        return review_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public double getRating() {
        return rating;
    }

    public Boolean getReadOnly()
    {
        return readOnly;
    }

    }












