package com.example.myapplication;

public class ListViewItem_search_address extends ListViewItem{

    private String postal_code;
    private String road_address;
    private String jibun_address;
    private String admcd;
    private String rnMgtSn;
    private String udrtYn;
    private String buldMnnm;
    private String buldSlno;

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

    public void setAdmcd(String admcd) {
        this.admcd = admcd;
    }

    public void setRnMgtSn(String rnMgtSn) {
        this.rnMgtSn = rnMgtSn;
    }

    public void setUdrtYn(String udrtYn) {
        this.udrtYn = udrtYn;
    }

    public void setBuldMnnm(String buldMnnm) {
        this.buldMnnm = buldMnnm;
    }

    public void setBuldSlno(String buldSlno) {
        this.buldSlno = buldSlno;
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

    public String getAdmcd() {
        return this.admcd;
    }

    public String getRnMgtSn() {
        return this.rnMgtSn;
    }

    public String getUdrtYn() {
        return this.udrtYn;
    }

    public String getBuldMnnm() {
        return this.buldMnnm;
    }

    public String getBuldSlno() {
        return this.buldSlno;
    }

}
