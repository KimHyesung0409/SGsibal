package com.example.myapplication;

public class ListViewItem_petlist extends ListViewItem {

    private String name;
    private String species;
    private String age;
    // 추가해야함.

    public void setName(String name) {
        this.name = name;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public void setAge(String age) { this.age = age;}

    public String getName() {
        return name;
    }

    public String getSpecies() {
        return species;
    }

    public String getAge() {return age;}

}
