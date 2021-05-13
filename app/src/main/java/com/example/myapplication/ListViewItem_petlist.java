package com.example.myapplication;

public class ListViewItem_petlist extends ListViewItem {

    private String name;
    private String species;
    private String age;
    private String detail_species;
    private String mbti;
    private String info;
    private String pet_id;
    // 추가해야함.

    public void setName(String name) {
        this.name = name;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public void setAge(String age) { this.age = age;}

    public void setDetail_species(String detail_species) {
        this.detail_species = detail_species;
    }

    public void setMbti(String mbti) {
        this.mbti = mbti;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void  setPet_id(String pet_id) {this.pet_id = pet_id;}

    public String getName() {
        return name;
    }

    public String getSpecies() {
        return species;
    }

    public String getAge() {return age;}

    public String getDetail_species() {
        return detail_species;
    }

    public String getMbti() {
        return mbti;
    }

    public String getInfo() {
        return info;
    }

    public String getPet_id() { return pet_id;}

}
