package com.example.myapplication;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OnCustomClickListener {

    // 지금은 1개만 정의했지만 즐겨찾기, 검색 등등 다양한 종류의 아이템이 있을것이다.
    private static final int ITEM_TYPE_ENTRUST = 0 ;
    private static final int ITEM_TYPE_SEARCH_ADDRESS = 1;
    private static final int ITEM_TYPE_SEARCH_ESTIMATE = 2;
    private static final int ITEM_TYPE_CHATROOM = 3;
    private static final int ITEM_TYPE_CHAT = 4;
    private static final int ITEM_TYPE_PET = 5;
    private static final int ITEM_TYPE_RESERVE = 6;
    private static final int ITEM_TYPE_RESERVE_AUTO = 7;

    private ArrayList<ListViewItem> listViewItemList = new ArrayList<>();

    // 리스너 객체 참조를 저장하는 변수
    private OnCustomClickListener onItemClickListener;

    private Context context;

    public RecyclerViewAdapter(Context context)
    {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        switch(viewType)
        {
            case ITEM_TYPE_ENTRUST:

                view = inflater.inflate(R.layout.reserve_entrust_listview_items, parent, false);
                viewHolder = new ViewHolder_reserve_entrust(view, this);

                break;

            case ITEM_TYPE_SEARCH_ADDRESS:

                view = inflater.inflate(R.layout.search_address_listview_items, parent, false);
                viewHolder = new ViewHolder_search_address(view, this);

                break;

            case ITEM_TYPE_SEARCH_ESTIMATE:

                view = inflater.inflate(R.layout.search_estimate_listview_items, parent, false);
                viewHolder = new ViewHolder_search_estimate(view, this);

                break;

            case ITEM_TYPE_CHATROOM:

                view = inflater.inflate(R.layout.chatroom_listview_items, parent, false);
                viewHolder = new ViewHolder_chatroom(view, this);

                break;

            case ITEM_TYPE_CHAT :

                view = inflater.inflate(R.layout.chat_listview_items, parent, false);
                viewHolder = new ViewHolder_chat(view);

                break;

            case ITEM_TYPE_PET :

                view = inflater.inflate(R.layout.pet_listview_items, parent, false);
                viewHolder = new ViewHolder_petlist(view, this);

                break;

            case ITEM_TYPE_RESERVE :

                view = inflater.inflate(R.layout.reserve_listview_items, parent, false);
                viewHolder = new ViewHolder_reservelist(view, this);

                break;

            case ITEM_TYPE_RESERVE_AUTO :

                view = inflater.inflate(R.layout.reserve_auto_listview_items, parent, false);
                viewHolder = new ViewHolder_reserve_auto(view, this);

                break;

        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ListViewItem item = listViewItemList.get(position);

        switch(item.getType())
        {
            case ITEM_TYPE_ENTRUST:

                ListViewItem_reserve_entrust listViewItem_reserve_entrust = (ListViewItem_reserve_entrust)item;

                ViewHolder_reserve_entrust viewHolder_reserve_entrust = (ViewHolder_reserve_entrust)holder;

                ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(context, listViewItem_reserve_entrust.getImages_num());
                viewHolder_reserve_entrust.viewPager_reserve_entrust.setAdapter(viewPagerAdapter);
                //viewHolder_reserve_entrust.viewPager_reserve_entrust.getLayoutParams().width = 500;
                viewHolder_reserve_entrust.viewPager_reserve_entrust.getLayoutParams().height = 500;

                viewHolder_reserve_entrust.viewPager_reserve_entrust.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        viewHolder_reserve_entrust.viewPager_indicator.setText((position + 1) + "/5");
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });

                viewHolder_reserve_entrust.textView_reserve_entrust_title.setText(listViewItem_reserve_entrust.getTitle());
                viewHolder_reserve_entrust.textView_reserve_entrust_address.setText(listViewItem_reserve_entrust.getAddress());
                viewHolder_reserve_entrust.textView_reserve_entrust_name.setText(listViewItem_reserve_entrust.getUser_name());
                viewHolder_reserve_entrust.textView_reserve_entrust_price.setText(listViewItem_reserve_entrust.getPrice());


                break;

            case ITEM_TYPE_SEARCH_ADDRESS:

                ListViewItem_search_address listViewItem_search_address = (ListViewItem_search_address)item;

                ViewHolder_search_address viewHolder_search_address = (ViewHolder_search_address)holder;

                viewHolder_search_address.textView_postal_code.setText(listViewItem_search_address.getPostal_code());
                viewHolder_search_address.textView_road_address.setText(listViewItem_search_address.getRoad_address());
                viewHolder_search_address.textView_jibun_address.setText(listViewItem_search_address.getJibun_address());

                break;

            case ITEM_TYPE_SEARCH_ESTIMATE:

                ListViewItem_search_estimate listViewItem_search_estimate = (ListViewItem_search_estimate)item;

                ViewHolder_search_estimate viewHolder_search_estimate = (ViewHolder_search_estimate)holder;

                viewHolder_search_estimate.textView_estimate_price.setText(listViewItem_search_estimate.getPrice());
                viewHolder_search_estimate.textView_estimate_address.setText(listViewItem_search_estimate.getAddress());

                break;

            case ITEM_TYPE_CHATROOM:

                ListViewItem_chatroom listViewItem_chatroom = (ListViewItem_chatroom)item;

                ViewHolder_chatroom viewHolder_chatroom = (ViewHolder_chatroom)holder;

                viewHolder_chatroom.textview_chatroom_opponent.setText(listViewItem_chatroom.getOpponent_name());

                break;

            case ITEM_TYPE_CHAT:

                ListViewItem_chat listViewItem_chat = (ListViewItem_chat)item;

                ViewHolder_chat viewHolder_chat = (ViewHolder_chat) holder;

                viewHolder_chat.textView_chat_text.setText(listViewItem_chat.getText());
                viewHolder_chat.textView_chat_timestamp_left.setText(listViewItem_chat.getTimestamp());
                viewHolder_chat.textView_chat_timestamp_right.setText(listViewItem_chat.getTimestamp());

                // 본인일 경우.
                if(listViewItem_chat.getSelf())
                {
                    viewHolder_chat.linearlayout_chat.setGravity(Gravity.RIGHT);
                    viewHolder_chat.textView_chat_name.setVisibility(View.GONE);
                    viewHolder_chat.textView_chat_timestamp_left.setVisibility(View.VISIBLE);
                    viewHolder_chat.textView_chat_timestamp_right.setVisibility(View.GONE);
                    viewHolder_chat.textView_chat_text.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.chat_color_self)));
                    viewHolder_chat.textView_chat_text.setTextColor(context.getColor(R.color.chat_color_text_self));
                }
                else // 아닐경우
                {
                    viewHolder_chat.linearlayout_chat.setGravity(Gravity.LEFT);
                    viewHolder_chat.textView_chat_name.setVisibility(View.VISIBLE);
                    viewHolder_chat.textView_chat_name.setText(listViewItem_chat.getName());
                    viewHolder_chat.textView_chat_timestamp_left.setVisibility(View.GONE);
                    viewHolder_chat.textView_chat_timestamp_right.setVisibility(View.VISIBLE);
                    viewHolder_chat.textView_chat_text.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.chat_color_opponent)));
                    viewHolder_chat.textView_chat_text.setTextColor(context.getColor(R.color.chat_color_text_opponent));
                }

                break;

            case ITEM_TYPE_PET :

                ListViewItem_petlist listViewItem_petlist = (ListViewItem_petlist)item;

                ViewHolder_petlist viewHolder_petlist = (ViewHolder_petlist)holder;

                viewHolder_petlist.textView_pet_name.setText("이름 : " + listViewItem_petlist.getName());
                viewHolder_petlist.textView_pet_species.setText("종류 : " + listViewItem_petlist.getSpecies());
                viewHolder_petlist.textview_pet_detail_species.setText("세부 종류 : " + listViewItem_petlist.getDetail_species());
                viewHolder_petlist.textview_pet_age.setText("나이 : " + listViewItem_petlist.getAge() + "개월");

                break;

            case ITEM_TYPE_RESERVE :

                ListViewItem_reserve listViewItem_reserve = (ListViewItem_reserve)item;

                ViewHolder_reservelist viewHolder_reservelist = (ViewHolder_reservelist)holder;

                viewHolder_reservelist.textview_datetime.setText(listViewItem_reserve.getDatetime());
                viewHolder_reservelist.textview_petname.setText(listViewItem_reserve.getPetname());
                viewHolder_reservelist.textview_sittername.setText(listViewItem_reserve.getSitterName());

                break;

            case ITEM_TYPE_RESERVE_AUTO :

                ListViewItem_reserve_auto listViewItem_reserve_auto = (ListViewItem_reserve_auto)item;

                ViewHolder_reserve_auto viewHolder_reserve_auto = (ViewHolder_reserve_auto)holder;

                viewHolder_reserve_auto.textView_reserve_auto_user_name.setText(listViewItem_reserve_auto.getUser_name());
                viewHolder_reserve_auto.textView_reserve_auto_address.setText(listViewItem_reserve_auto.getAddress());
                viewHolder_reserve_auto.textView_reserve_auto_address_detail.setText(listViewItem_reserve_auto.getAddress_detail());
                viewHolder_reserve_auto.textView_reserve_auto_distance.setText(listViewItem_reserve_auto.getDistance());

                break;

        }

    }

    /*
        - 리사이클러뷰는 리스트뷰와는 다르게 자체적으로 클릭이벤트를 처리하는 메소드가 없다.
        - 뷰홀더가 생성되는 시점에서 클릭이벤트를 넣어도 되지만
        - 각자의 엑티비티, 프래그먼트에서 처리하고 싶기 때문에 인터페이스를 정의하고
        - onItemClick 메소드를 구현하여 해당 뷰와 포지션을 액티비티, 프래그먼트로 보낸다.
        - 이때 onItemClick 메소드는 해당 Viewholder 클래스의 생성자에 구현한다.
     */

    @Override
    public void onItemClick(View view, int position) throws InterruptedException {
        if(onItemClickListener != null)
        {
            onItemClickListener.onItemClick(view, position);
        }
    }

    @Override
    public void onItemLongClick(View view, int position) throws InterruptedException {
        if(onItemClickListener != null)
        {
            onItemClickListener.onItemLongClick(view, position);
        }
    }

    // OnItemClickListener 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnCustomClickListener listener)
    {
        this.onItemClickListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        ListViewItem item = listViewItemList.get(position);

        return item.getType();
    }

    @Override
    public int getItemCount() {
        return listViewItemList.size();
    }

    public void clear()
    {
        listViewItemList.clear();
    }

    public Object getItem(int i) {
        return listViewItemList.get(i);
    }

    // addItem

    public void addItem(ListViewItem_reserve_entrust item)
    {
        // 부모 클래스의 메소드 (공통)
        item.setType(ITEM_TYPE_ENTRUST);

        listViewItemList.add(item);
    }

    public void addItem(ListViewItem_search_address item)
    {
        item.setType(ITEM_TYPE_SEARCH_ADDRESS);

        listViewItemList.add(item);
    }

    public void addItem(ListViewItem_search_estimate item)
    {
        item.setType(ITEM_TYPE_SEARCH_ESTIMATE);

        listViewItemList.add(item);
    }

    public void addItem(ListViewItem_chat item)
    {
        item.setType(ITEM_TYPE_CHAT);

        listViewItemList.add(item);
    }

    public void addItem(ListViewItem_chatroom item)
    {
        item.setType(ITEM_TYPE_CHATROOM);

        listViewItemList.add(item);
    }

    public void addItem(ListViewItem_petlist item)
    {
        item.setType(ITEM_TYPE_PET);

        listViewItemList.add(item);
    }

    public void addItem(ListViewItem_reserve item)
    {
        item.setType(ITEM_TYPE_RESERVE);

        listViewItemList.add(item);
    }

    public void addItem(ListViewItem_reserve_auto item)
    {
        item.setType(ITEM_TYPE_RESERVE_AUTO);

        listViewItemList.add(item);
    }

}
