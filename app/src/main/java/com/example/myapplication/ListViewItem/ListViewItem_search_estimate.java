package com.example.myapplication.ListViewItem;

import java.util.Date;

/*
 * 견적서 요소 ListViewItem 클래스
 * 주소
 * 가격
 * 반려동물 나이
 * 반려동물 종류
 * 반려동물 상세종류
 * 고객 user_id
 * 반려동물 id
 * 주의사항
 * 예약 날짜
 * 반려동물 이름
 */

public class ListViewItem_search_estimate extends ListViewItem {

    private String estimate_id;
    private String address;
    private String price;
    private String pet_age;
    private String species;
    private String species_detail;
    private String user_id;
    private String pet_id;
    private String info;
    private Date datetime;
    private String pet_name;

    public void setEstimate_id(String estimate_id) {
        this.estimate_id = estimate_id;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public void setPrice(String price)
    {
        this.price = price;
    }

    public void setPet_age(String pet_age) {
        this.pet_age = pet_age;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public void setSpecies_detail(String species_detail) {
        this.species_detail = species_detail;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setPet_id(String pet_id) {
        this.pet_id = pet_id;
    }

    public void setInfo(String info)
    {
        this.info = info;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public void setPet_name(String pet_name)
    {
        this.pet_name = pet_name;
    }

    public String getEstimate_id() {
        return estimate_id;
    }

    public String getAddress()
    {
        return this.address;
    }

    public String getPrice()
    {
        return this.price;
    }

    public String getPet_age() {
        return pet_age;
    }

    public String getSpecies() {
        return species;
    }

    public String getSpecies_detail() {
        return species_detail;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getPet_id() {
        return pet_id;
    }

    public String getInfo()
    {
        return info;
    }

    public Date getDatetime() {
        return datetime;
    }

    public String getPet_name()
    {
        return pet_name;
    }

}
