package com.example.myapplication;

public class ListViewItem_chatroom extends ListViewItem {

    private String opponent_id;
    private String opponent_name;
    private String chatroom;

    public void setOpponent_id(String opponent_id) {
        this.opponent_id = opponent_id;
    }

    public void setOpponent_name(String opponent_name) {
        this.opponent_name = opponent_name;
    }

    public void setChatroom(String chatroom) {
        this.chatroom = chatroom;
    }

    public String getOpponent_id() {
        return opponent_id;
    }

    public String getOpponent_name() {
        return opponent_name;
    }

    public String getChatroom() {
        return chatroom;
    }

}
