package com.example.myapplication;

import java.util.Date;

public class ListViewItem_storylist extends ListViewItem {

    private String reserve_id;
    private String story_id;
    private String story_title;
    private String story_content;
    private String image_num;
    private Date timestamp;

    public void setReserve_id(String reserve_id)
    {
        this.reserve_id = reserve_id;
    }

    public void setStory_id(String story_id) {
        this.story_id = story_id;
    }

    public void setStory_title(String story_title) {
        this.story_title = story_title;
    }

    public void setStory_content(String story_content) {
        this.story_content = story_content;
    }

    public void setImage_num(String image_num) {
        this.image_num = image_num;
    }

    public void setTimestamp(Date timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getStory_id() {
        return story_id;
    }

    public String getStory_title() {
        return story_title;
    }

    public String getStory_content() {
        return story_content;
    }

    public String getImage_num() {
        return image_num;
    }

    public String getReserve_id()
    {
        return reserve_id;
    }

    public Date getTimestamp()
    {
        return timestamp;
    }

}
