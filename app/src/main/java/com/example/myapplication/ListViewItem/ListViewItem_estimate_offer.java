package com.example.myapplication.ListViewItem;

import java.util.Date;

/*
 * 역제안서 요소 ListViewItem 클래스
 * 역제안서 id
 * 펫시터 어필 내용
 * 견적서를 업로드한 user_id
 * 역제안서를 업로드한 시간.
 */

public class ListViewItem_estimate_offer extends ListViewItem {

    private String offer_id;
    private String appeal;
    private String price;
    private String user_id;
    private Date Timestamp;

    public void setOffer_id(String offer_id) {
        this.offer_id = offer_id;
    }

    public void setAppeal(String appeal) {
        this.appeal = appeal;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setTimestamp(Date timestamp) {
        Timestamp = timestamp;
    }

    public String getOffer_id() {
        return offer_id;
    }

    public String getAppeal() {
        return appeal;
    }

    public String getPrice() {
        return price;
    }

    public String getUser_id() {
        return user_id;
    }

    public Date getTimestamp() {
        return Timestamp;
    }
}
