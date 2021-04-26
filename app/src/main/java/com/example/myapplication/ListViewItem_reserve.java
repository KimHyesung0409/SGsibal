package com.example.myapplication;

public class ListViewItem_reserve extends ListViewItem {
    private String sittername;
    private String datetime;
    private String petname;
    // 추가해야함.

    public void setSittername(String sittername) {
        this.sittername = sittername;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public void setPetname(String petname) { this.petname = petname; }



    public String getSitterName() {
        return sittername;
    }

    public String getDatetime() {
        return datetime;
    }

    public String getPetname() { return petname; }
}

