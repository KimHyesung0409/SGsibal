package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {

    // 지금은 1개만 정의했지만 즐겨찾기, 검색 등등 다양한 종류의 아이템이 있을것이다.
    private static final int ITEM_TYPE_ENTRUST = 0 ;

    private ArrayList<ListViewItem> listViewItemList = new ArrayList<>();
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private int images[];

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public Object getItem(int i) {
        return listViewItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemViewType(int position) {
        return listViewItemList.get(position).getType() ;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final int pos = i;
        final Context context = viewGroup.getContext();
        final TextView textView;

        if(view == null)
        {
            int type = getItemViewType(i);

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            switch (type)
            {
                case ITEM_TYPE_ENTRUST:

                    view = inflater.inflate(R.layout.reserve_entrust_listview_items, viewGroup, false);

                    // 형변환하여 자식 클래스 기능 사용.
                    ListViewItem_reserve_entrust listViewItem = (ListViewItem_reserve_entrust) listViewItemList.get(i);

                    TextView titleTextView = (TextView) view.findViewById(R.id.textview_reserve_entrust_1);
                    titleTextView.setText(listViewItem.getTitle());

                    viewPager = (ViewPager) view.findViewById(R.id.viewpager_reserve_entrust);
                    textView = (TextView) view.findViewById(R.id.viewpager_indicator);
                    viewPagerAdapter = new ViewPagerAdapter(context, listViewItem.getImages());
                    viewPager.setAdapter(viewPagerAdapter);
                    viewPager.getLayoutParams().width = 500;
                    viewPager.getLayoutParams().height = 500;

                    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        }

                        @Override
                        public void onPageSelected(int position) {
                            textView.setText((position + 1) + "/5");
                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });

                    break;

                // 다른 케이스가 있다면 추가
            }
        }

        return view;
    }

    // 각 아이템에 대한 addItem 메소드를 구현하는 곳.
    // 함수(메소드) 오버라이딩이 가능하기 때문에 인자(파라미터)만 다르게하여 구현하면된다.

    // ListViewItem_reserve_entrust 아이템에 대한 addItem 메소드
    public void addItem(int images[] ,String title)
    {
        ListViewItem_reserve_entrust item = new ListViewItem_reserve_entrust();

        // 부모 클래스의 메소드 (공통)
        item.setType(ITEM_TYPE_ENTRUST);
        item.setTitle(title);

        // 자식 클래스의 메소드
        item.setViewPager(viewPager);
        item.setImages(images);

        listViewItemList.add(item);
    }

}