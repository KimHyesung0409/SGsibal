package com.example.myapplication;

// ListViewItem 은 부모클래스로 해당 프로젝트는 ListView에 다양한 형태의 Item 을 출력해야한다.
// 이 클래스는 다양한 형태의 여러 Item 들에 공통적으로 들어갈 필드(속성)와 메소드를 지정한다.

public class ListViewItem {

    // 해당 클래스가 무슨 타입인지 정의해야한다.
    public static int type;
    // 타이틀은 공통적으로 들어갈 것 같다.
    public String title;
    // 아래에 공통적으로 들어갈 속성들이 있다면 추가

    //--------------------- 세터 --------------------------

    public static void setType(int i) { type = i; }

    public void setTitle(String title) {
        this.title = title;
    }

    //--------------------- 게터 --------------------------

    public static int getType() { return type; }

    public String getTitle() {
        return this.title ;
    }

}
