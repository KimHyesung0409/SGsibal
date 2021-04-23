package com.example.myapplication;

public class ListViewItem_chat extends ListViewItem{

    private String text;
    private String name;
    private String timestamp;
    private boolean self;

    public void setText(String text) {
        this.text = text;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setSelf(boolean self)
    {
        this.self = self;
    }

    public String getText() {
        return text;
    }

    public String getName() {
        return name;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public boolean getSelf()
    {
        return this.self;
    }

}
