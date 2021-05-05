package com.example.myapplication;

import android.view.View;

// 리사이클러뷰 어뎁터 내부에 커스텀 ViewHolder 클래스를 정의하고(inner class)
// 상위 클래스를 계속해서 참조하게 되면 메모리 힙 영역에 상위클래스의 맴버 인스턴스 만큼
// 메모리를 더 할당한다고 한다..
// 그래서 커스텀 ViewHolder class를 따로 만들어서 클릭 이벤트를 적용해야하는데
// 이 인터페이스를 통해 클릭 이벤트를 전달한다.

public interface OnCustomClickListener {

    public void onItemClick(View view, int position) throws InterruptedException;

}
