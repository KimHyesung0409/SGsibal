package com.example.myapplication;

public class ListViewItem_storylist extends ListViewItem {

    private String name_customer;
    private String name_pet;
    private String content_sitting;
    private String story_id;

    public void setName_customer(String name_customer) {this.name_customer = name_customer;}

    public void setName_pet(String name_pet) {this.name_pet = name_pet;}

    public void setContent_sitting(String content_sitting) {this.content_sitting = content_sitting;}

    public void setStory_id(String story_id) {this.story_id = story_id;}

    public String getName_customer() { return name_customer;}

    public String getName_pet() { return name_pet;}

    public String getContent_sitting() { return content_sitting;}

    public String getStory_id() { return story_id;}

}
