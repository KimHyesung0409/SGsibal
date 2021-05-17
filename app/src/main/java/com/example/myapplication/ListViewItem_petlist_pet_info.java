package com.example.myapplication;

public class ListViewItem_petlist_pet_info {
    int image;
    String name;
    int age;

    public ListViewItem_petlist_pet_info(int image, String name, int age){
        this.image=image;
        this.name=name;
        this.age=age;
    }

    public  int getImage(){
        return image;
    }

    public void setImage(int image){
        this.image= image;
    }

    public  String getName(){
        return name;
    }

    public void  setName(String name){
        this.name = name;
    }

    public int getAge(){
        return age;
    }

    public void setAge(int age){
        this.age=age;
    }
}
