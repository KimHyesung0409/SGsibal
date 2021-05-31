package com.example.myapplication;

/*
 * 예약 요소 ListViewItem 클래스
 * 예약 상대의 이름
 * 예약 상대의 user_id
 * 예약 시간
 * 반려동물 id
 * 반려동물 이름
 * 예약 id
 */

public class ListViewItem_reserve extends ListViewItem {
    private String user_name;
    private String datetime;
    private String petname;
    // 추가해야함.
    private String reserve_id;
    private String user_id;
    private String pet_id;

    public void setUser_name(String sittername) {
        this.user_name = sittername;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public void setPetname(String petname) { this.petname = petname; }

    public void setReserve_id(String reserve_id)
    {
        this.reserve_id = reserve_id;
    }

    public void setUser_id(String user_id)
    {
        this.user_id = user_id;
    }

    public void setPet_id(String pet_id)
    {
        this.pet_id = pet_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getDatetime() {
        return datetime;
    }

    public String getPetname() { return petname; }

    public String getReserve_id()
    {
        return reserve_id;
    }

    public String getUser_id()
    {
        return user_id;
    }

    public String getPet_id()
    {
        return pet_id;
    }
}

