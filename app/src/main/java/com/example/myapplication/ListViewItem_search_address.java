package com.example.myapplication;

public class ListViewItem_search_address extends ListViewItem{

    private String postal_code;
    private String road_address;
    private String jibun_address;

    public void setPostal_code(String postal_code)
    {
        this.postal_code = postal_code;
    }

    public void setRoad_address(String road_address)
    {
        this.road_address= road_address;
    }

    public void setJibun_address(String jibun_address)
    {
        this.jibun_address = jibun_address;
    }

    public String getPostal_code()
    {
        return this.postal_code;
    }

    public String getRoad_address()
    {
        return this.road_address;
    }

    public String getJibun_address()
    {
        return this.jibun_address;
    }
    

}
