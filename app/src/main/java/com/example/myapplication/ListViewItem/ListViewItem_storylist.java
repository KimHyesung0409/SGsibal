package com.example.myapplication.ListViewItem;

import java.util.Date;

/*
 * 스토리 목록 요소 ListViewItem 클래스
 * 예약 id
 * 스토리 id
 * 스토리 제목
 * 스토리 내용
 * 스토리 이미지 id
 * 스토리 업로드 시간.
 */

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
