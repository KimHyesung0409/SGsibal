package com.example.myapplication;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {

    // 지금은 1개만 정의했지만 즐겨찾기, 검색 등등 다양한 종류의 아이템이 있을것이다.
    private static final int ITEM_TYPE_ENTRUST = 0 ;
    private static final int ITEM_TYPE_SEARCH_ADDRESS = 1;
    private static final int ITEM_TYPE_SEARCH_ESTIMATE = 2;
    private static final int ITEM_TYPE_CHATROOM = 3;
    private static final int ITEM_TYPE_CHAT = 4;

    private ArrayList<ListViewItem> listViewItemList = new ArrayList<>();
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private int images[];

    public void clearListView()
    {
        listViewItemList = new ArrayList<>();
    }

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
                    ListViewItem_reserve_entrust listViewItem_reserve_entrust = (ListViewItem_reserve_entrust) listViewItemList.get(i);

                    TextView titleTextView = (TextView) view.findViewById(R.id.textview_reserve_entrust_1);
                    titleTextView.setText(listViewItem_reserve_entrust.getTitle());

                    viewPager = (ViewPager) view.findViewById(R.id.viewpager_reserve_entrust);
                    textView = (TextView) view.findViewById(R.id.viewpager_indicator);
                    viewPagerAdapter = new ViewPagerAdapter(context, listViewItem_reserve_entrust.getImages());
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

                case ITEM_TYPE_SEARCH_ADDRESS:

                    view = inflater.inflate(R.layout.search_address_listview_items, viewGroup, false);

                    // 형변환하여 자식 클래스 기능 사용.
                    ListViewItem_search_address listViewItem_search_address = (ListViewItem_search_address) listViewItemList.get(i);

                    TextView textView_postal_code = (TextView) view.findViewById(R.id.textview_postal_code);
                    TextView textView_road_address = (TextView) view.findViewById(R.id.textview_road_address);
                    TextView textView_jibun_address = (TextView) view.findViewById(R.id.textview_jibun_address);

                    textView_postal_code.setText(listViewItem_search_address.getPostal_code());
                    textView_road_address.setText(listViewItem_search_address.getRoad_address());
                    textView_jibun_address.setText(listViewItem_search_address.getJibun_address());

                    break;
                // 다른 케이스가 있다면 추가

                case ITEM_TYPE_SEARCH_ESTIMATE:

                    view = inflater.inflate(R.layout.search_estimate_listview_items, viewGroup, false);

                    // 형변환하여 자식 클래스 기능 사용.
                    ListViewItem_search_estimate listViewItem_search_estimate = (ListViewItem_search_estimate)listViewItemList.get(i);

                    TextView textView_estimate_address = (TextView)view.findViewById(R.id.textview_estimate_address);
                    TextView textView_estunate_price = (TextView)view.findViewById(R.id.textview_estimate_price);
                    textView_estimate_address.setText(listViewItem_search_estimate.getAddress());
                    textView_estunate_price.setText(listViewItem_search_estimate.getPrice());

                    break;

                case ITEM_TYPE_CHATROOM:

                    view = inflater.inflate(R.layout.chatroom_listview_items, viewGroup, false);

                    // 형변환하여 자식 클래스 기능 사용.
                    ListViewItem_chatroom listViewItem_chatroom = (ListViewItem_chatroom)listViewItemList.get(i);

                    TextView textView_chatroom_opponent = (TextView)view.findViewById(R.id.textview_chatroom_opponent);

                    textView_chatroom_opponent.setText(listViewItem_chatroom.getOpponent_name());

                    break;

                case ITEM_TYPE_CHAT:

                    view = inflater.inflate(R.layout.chat_listview_items, viewGroup, false);

                    // 형변환하여 자식 클래스 기능 사용.
                    ListViewItem_chat listViewItem_chat = (ListViewItem_chat)listViewItemList.get(i);

                    TextView textView_chat_name = (TextView)view.findViewById(R.id.textview_chat_name);
                    TextView textView_chat_text = (TextView)view.findViewById(R.id.textview_chat_text);
                    TextView textView_chat_timestamp = (TextView)view.findViewById(R.id.textview_chat_timestamp);

                    // 본인일 경우.
                    if(listViewItem_chat.getSelf())
                    {
                        textView_chat_name.setVisibility(View.GONE);
                        textView_chat_text.setText(listViewItem_chat.getText());
                        textView_chat_timestamp.setText(listViewItem_chat.getTimestamp());
                    }
                    else // 아닐경우
                    {
                        LinearLayout linearlayout_chat = (LinearLayout)view.findViewById(R.id.linearlayout_chat);
                        linearlayout_chat.setGravity(Gravity.LEFT);
                        textView_chat_name.setText(listViewItem_chat.getName());
                        textView_chat_text.setText(listViewItem_chat.getText());
                        textView_chat_timestamp.setText(listViewItem_chat.getTimestamp());
                        textView_chat_timestamp.setX(textView_chat_name.getX() + textView_chat_name.getWidth());
                    }

                    break;
            }
        }

        return view;
    }

    // 각 아이템에 대한 addItem 메소드를 구현하는 곳.
    // 함수(메소드) 오버라이딩이 가능하기 때문에 인자(파라미터)만 다르게하여 구현하면된다.

    // ListViewItem_reserve_entrust 아이템에 대한 addItem 메소드

    // 2021/04/23 추가 ListViewItem_아이템명으로 된 파라미터를 넘겨야 할 것 같다.
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

    public void addItem(String postal_code, String road_address, String jibun_address)
    {
        ListViewItem_search_address item = new ListViewItem_search_address();

        item.setType(ITEM_TYPE_SEARCH_ADDRESS);

        item.setPostal_code(postal_code);
        item.setRoad_address(road_address);
        item.setJibun_address(jibun_address);

        listViewItemList.add(item);
    }

    public void addItem(String address, String price)
    {
        ListViewItem_search_estimate item = new ListViewItem_search_estimate();

        item.setType(ITEM_TYPE_SEARCH_ESTIMATE);

        item.setAddress(address);
        item.setPrice(price);

        listViewItemList.add(item);
    }

    public void addItem(ListViewItem_chatroom item)
    {
        item.setType(ITEM_TYPE_CHATROOM);

        listViewItemList.add(item);
    }

    public void addItem(ListViewItem_chat item)
    {
        item.setType(ITEM_TYPE_CHAT);

        listViewItemList.add(item);
    }

}
