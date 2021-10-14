package com.example.myapplication.ViewHolder;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.myapplication.DateString;
import com.example.myapplication.ListViewItem.ListViewItem;
import com.example.myapplication.ListViewItem.ListViewItem_chat;
import com.example.myapplication.ListViewItem.ListViewItem_chatroom;
import com.example.myapplication.ListViewItem.ListViewItem_estimate_offer;
import com.example.myapplication.ListViewItem.ListViewItem_petlist;
import com.example.myapplication.ListViewItem.ListViewItem_reserve;
import com.example.myapplication.ListViewItem.ListViewItem_reserve_auto;
import com.example.myapplication.ListViewItem.ListViewItem_reserve_entrust;
import com.example.myapplication.ListViewItem.ListViewItem_reviewlist;
import com.example.myapplication.ListViewItem.ListViewItem_search_address;
import com.example.myapplication.ListViewItem.ListViewItem_search_estimate;
import com.example.myapplication.ListViewItem.ListViewItem_searchlist;
import com.example.myapplication.ListViewItem.ListViewItem_storylist;
import com.example.myapplication.OnCustomClickListener;
import com.example.myapplication.R;
import com.example.myapplication.ViewPagerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

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
    private static final int ITEM_TYPE_STORY = 8;
    private static final int ITEM_TYPE_ESTIMATE = 9;
    private static final int ITEM_TYPE_ESTIMATE_OFFER = 10;
    private static final int ITEM_TYPE_REVIEW = 11;
    private static final int ITEM_TYPE_SEACHLIST = 12;

    private static final double RATINGBAR_GONE = 100.0;

    private ArrayList<ListViewItem> listViewItemList = new ArrayList<>();

    // 리스너 객체 참조를 저장하는 변수
    private OnCustomClickListener onItemClickListener;

    private Context context;

    public RecyclerViewAdapter(Context context)
    {
        this.context = context;
    }


    // 아이템 타입에 따라 inflate 하는 layout과 viewHolder 타입이 달라진다.
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

            case ITEM_TYPE_STORY :

                view = inflater.inflate(R.layout.story_listview_items, parent, false);
                viewHolder = new ViewHolder_storylist(view, this);

                break;

            case ITEM_TYPE_ESTIMATE:

                view = inflater.inflate(R.layout.estimate_listview_items, parent, false);
                viewHolder = new ViewHolder_estimate(view ,this);

                break;

            case ITEM_TYPE_ESTIMATE_OFFER:

                view = inflater.inflate(R.layout.estimate_offer_listview_items, parent, false);
                viewHolder = new ViewHolder_estimate_offer(view ,this);

                break;

            case ITEM_TYPE_REVIEW:

                view = inflater.inflate(R.layout.review_items, parent, false);
                viewHolder = new ViewHolder_reviewlist(view ,this);

                break;

            case ITEM_TYPE_SEACHLIST:

                view = inflater.inflate(R.layout.searchlist_listview_items, parent, false);
                viewHolder = new ViewHolder_searchlist(view, this);

        }

        return viewHolder;
    }

    // 아이템 타입에 따라 binding 내용이 달라진다.
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
                viewHolder_reserve_entrust.textView_reserve_entrust_price.setText("가격 : " + listViewItem_reserve_entrust.getPrice() + "원");


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

                viewHolder_search_estimate.textView_search_estimate_address.setText(listViewItem_search_estimate.getAddress());
                viewHolder_search_estimate.textView_search_estimate_species.setText(listViewItem_search_estimate.getSpecies() + ", ");
                viewHolder_search_estimate.textView_search_estimate_species_detail.setText(listViewItem_search_estimate.getSpecies_detail() + ", ");
                viewHolder_search_estimate.textView_search_estimate_pet_age.setText(listViewItem_search_estimate.getPet_age() + "개월");
                viewHolder_search_estimate.textView_search_estimate_price.setText(listViewItem_search_estimate.getPrice() + "원");

                break;

            case ITEM_TYPE_CHATROOM:

                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();

                ListViewItem_chatroom listViewItem_chatroom = (ListViewItem_chatroom)item;

                ViewHolder_chatroom viewHolder_chatroom = (ViewHolder_chatroom)holder;

                viewHolder_chatroom.textview_chatroom_opponent.setText(listViewItem_chatroom.getOpponent_name());

                String path = "images_profile/";
                String format = ".jpg";

                // 파이어 스토리지 레퍼런스로 파일을 참조한다.
                StorageReference child = storageRef.child(path + listViewItem_chatroom.getOpponent_id() + format);

                // 참조한 파일의 다운로드 링크가 성공적으로 구해지면 Glide를 이용해 이미지뷰에 로딩한다.
                child.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            // Glide 이용하여 이미지뷰에 로딩
                            Glide.with(context)
                                    .load(task.getResult())
                                    .fitCenter()
                                    .into(viewHolder_chatroom.circleimageview_chatroom_image);
                        } else {

                        }
                    }
                });

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

                if(listViewItem_petlist.getName() == null)
                {
                    LinearLayout parent_text = (LinearLayout) viewHolder_petlist.textView_pet_name.getParent();
                    parent_text.setVisibility(View.GONE);

                    LinearLayout parent_image = (LinearLayout)viewHolder_petlist.circleImageView_pet_image.getParent();
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) parent_image.getLayoutParams();
                    params.rightMargin = 0;

                    viewHolder_petlist.circleImageView_pet_image.setImageResource(R.drawable.ic_baseline_add_48);

                }
                else {
                    storage = FirebaseStorage.getInstance();
                    storageRef = storage.getReference();

                    path = "images_pet/";
                    format = ".jpg";

                    // 파이어 스토리지 레퍼런스로 파일을 참조한다.
                    child = storageRef.child(path + listViewItem_petlist.getPet_id() + format);

                    // 참조한 파일의 다운로드 링크가 성공적으로 구해지면 Glide를 이용해 이미지뷰에 로딩한다.
                    child.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                // Glide 이용하여 이미지뷰에 로딩
                                Glide.with(context)
                                        .load(task.getResult())
                                        .fitCenter()
                                        .into(viewHolder_petlist.circleImageView_pet_image);
                            } else {

                            }
                        }
                    });
                }

                break;

            case ITEM_TYPE_RESERVE :

                ListViewItem_reserve listViewItem_reserve = (ListViewItem_reserve)item;

                ViewHolder_reservelist viewHolder_reservelist = (ViewHolder_reservelist)holder;

                viewHolder_reservelist.textview_datetime.setText(listViewItem_reserve.getDatetime());
                viewHolder_reservelist.textview_petname.setText(listViewItem_reserve.getPetname());
                viewHolder_reservelist.textview_sittername.setText(listViewItem_reserve.getUser_name());

                break;

            case ITEM_TYPE_RESERVE_AUTO :

                ListViewItem_reserve_auto listViewItem_reserve_auto = (ListViewItem_reserve_auto)item;

                ViewHolder_reserve_auto viewHolder_reserve_auto = (ViewHolder_reserve_auto)holder;

                viewHolder_reserve_auto.textView_reserve_auto_user_name.setText("이름 : " + listViewItem_reserve_auto.getUser_name());
                viewHolder_reserve_auto.textView_reserve_auto_address.setText("주소 : " + listViewItem_reserve_auto.getAddress());
                viewHolder_reserve_auto.textView_reserve_auto_address_detail.setText("주소 상세 : " + listViewItem_reserve_auto.getAddress_detail());
                viewHolder_reserve_auto.textView_reserve_auto_distance.setText("거리 : " + listViewItem_reserve_auto.getDistance());
                viewHolder_reserve_auto.ratingBar_reserve_auto.setRating((float)listViewItem_reserve_auto.getRating());

                break;

            case ITEM_TYPE_STORY :

                storage = FirebaseStorage.getInstance();
                storageRef = storage.getReference();

                ListViewItem_storylist listViewItem_storylist = (ListViewItem_storylist)item;

                ViewHolder_storylist viewHolder_storylist = (ViewHolder_storylist)holder;

                viewHolder_storylist.textView_story_title.setText(listViewItem_storylist.getStory_title());
                viewHolder_storylist.textView_story_content.setText(listViewItem_storylist.getStory_content());


                path = "images_story/";
                format = ".jpg";

                // 파이어 스토리지 레퍼런스로 파일을 참조한다.
                child = storageRef.child(path + listViewItem_storylist.getImage_num() + format);

                // 참조한 파일의 다운로드 링크가 성공적으로 구해지면 Glide를 이용해 이미지뷰에 로딩한다.
                child.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            // Glide 이용하여 이미지뷰에 로딩
                            Glide.with(context)
                                    .load(task.getResult())
                                    .fitCenter()
                                    .into(viewHolder_storylist.imageView_story);
                        } else {

                        }
                    }
                });

                break;

            case ITEM_TYPE_ESTIMATE:

                ListViewItem_search_estimate listViewItem_estimate = (ListViewItem_search_estimate)item;

                ViewHolder_estimate viewHolder_estimate = (ViewHolder_estimate)holder;

                viewHolder_estimate.textView_estimate_pet_name.setText(listViewItem_estimate.getPet_name());
                viewHolder_estimate.textView_estimate_datetime.setText(DateString.DateToString(listViewItem_estimate.getDatetime()));
                viewHolder_estimate.textView_estimate_species.setText(listViewItem_estimate.getSpecies() + ", ");
                viewHolder_estimate.textView_estimate_species_detail.setText(listViewItem_estimate.getSpecies_detail() + ", ");
                viewHolder_estimate.textView_estimate_pet_age.setText(listViewItem_estimate.getPet_age() + "개월");
                viewHolder_estimate.textView_estimate_price.setText(listViewItem_estimate.getPrice() + "원");

                break;

            case ITEM_TYPE_ESTIMATE_OFFER:

                ListViewItem_estimate_offer listViewItem_estimate_offer = (ListViewItem_estimate_offer) item;

                ViewHolder_estimate_offer viewHolder_estimate_offer = (ViewHolder_estimate_offer) holder;

                viewHolder_estimate_offer.textView_estimate_offer_price.setText(listViewItem_estimate_offer.getPrice() + "원");
                viewHolder_estimate_offer.textview_estimate_offer_timestamp.setText(DateString.DateToString(listViewItem_estimate_offer.getTimestamp()));

                break;

            case ITEM_TYPE_REVIEW:

                storage = FirebaseStorage.getInstance();
                storageRef = storage.getReference();

                ListViewItem_reviewlist listViewItem_reviewlist = (ListViewItem_reviewlist)item;

                ViewHolder_reviewlist viewHolder_reviewlist = (ViewHolder_reviewlist)holder;

                path = "images_profile/";
                format = ".jpg";

                if(listViewItem_reviewlist.getReadOnly() == true)
                {
                    viewHolder_reviewlist.textView_review_title.setText("제목 : " + listViewItem_reviewlist.getReview_title());
                    viewHolder_reviewlist.textView_review_content.setText("내용 : " + listViewItem_reviewlist.getReview_content());
                    viewHolder_reviewlist.textView_review_target_name.setText("이름 : " + listViewItem_reviewlist.getUser_name());
                    viewHolder_reviewlist.ratingBar_review.setRating((float)listViewItem_reviewlist.getRating());

                    // 파이어 스토리지 레퍼런스로 파일을 참조한다.
                    child = storageRef.child(path + listViewItem_reviewlist.getUser_id() + format);

                    // 참조한 파일의 다운로드 링크가 성공적으로 구해지면 Glide를 이용해 이미지뷰에 로딩한다.
                    child.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                // Glide 이용하여 이미지뷰에 로딩
                                Glide.with(context)
                                        .load(task.getResult())
                                        .fitCenter()
                                        .into(viewHolder_reviewlist.circleimageview_review_profile_image);
                            } else {

                            }
                        }
                    });
                }
                else
                {
                    viewHolder_reviewlist.textView_review_title.setText("제목 : " + listViewItem_reviewlist.getReview_title());
                    viewHolder_reviewlist.textView_review_content.setText("내용 : " + listViewItem_reviewlist.getReview_content());
                    viewHolder_reviewlist.textView_review_target_name.setText("리뷰 대상 : " + listViewItem_reviewlist.getUser_name());
                    viewHolder_reviewlist.ratingBar_review.setRating((float)listViewItem_reviewlist.getRating());
                    viewHolder_reviewlist.circleimageview_review_profile_image.setVisibility(View.GONE);
                }


                break;

            case ITEM_TYPE_SEACHLIST:

                ListViewItem_searchlist listViewItem_searchlist = (ListViewItem_searchlist)item;

                ViewHolder_searchlist viewHolder_searchlist = (ViewHolder_searchlist)holder;

                float rating = (float)listViewItem_searchlist.getRating();

                viewHolder_searchlist.textView_searchlist_user_name.setText("이름 : " + listViewItem_searchlist.getUser_name());
                viewHolder_searchlist.textView_searchlist_birth.setText("생년월일 : " + listViewItem_searchlist.getBirth());
                viewHolder_searchlist.textView_searchlist_gender.setText("성별 : " + listViewItem_searchlist.getGender());
                viewHolder_searchlist.ratingBar_searchlist.setRating(rating);

                if(rating == RATINGBAR_GONE)
                {
                    viewHolder_searchlist.ratingBar_searchlist.setVisibility(View.GONE);
                }

                if(listViewItem_searchlist.getDatetime() != null)
                {
                    viewHolder_searchlist.textView_searchlist_datetime.setText("최근 예약 : " + DateString.DateToString(listViewItem_searchlist.getDatetime()));
                }



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


    /*
     * addItem 메소드들.
     */
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

    public void addItem(ListViewItem_search_estimate item, boolean bool)
    {
        if(bool)
        {
            item.setType(ITEM_TYPE_SEARCH_ESTIMATE);
        }
        else
        {
            item.setType(ITEM_TYPE_ESTIMATE);
        }
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

    public void addItem(ListViewItem_storylist item)
    {
        item.setType(ITEM_TYPE_STORY);

        listViewItemList.add(item);
    }

    public void addItem(ListViewItem_estimate_offer item)
    {
        item.setType(ITEM_TYPE_ESTIMATE_OFFER);

        listViewItemList.add(item);
    }

    public void addItem(ListViewItem_reviewlist item)
    {
        item.setType(ITEM_TYPE_REVIEW);

        listViewItemList.add(item);
    }

    public void addItem(ListViewItem_searchlist item)
    {
        item.setType(ITEM_TYPE_SEACHLIST);

        listViewItemList.add(item);
    }
}
