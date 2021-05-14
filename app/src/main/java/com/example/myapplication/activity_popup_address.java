package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class activity_popup_address extends AppCompatActivity implements OnCustomClickListener {

    private EditText edit_search_address;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup_address);

        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        edit_search_address = (EditText)findViewById(R.id.edit_search_address);

        recyclerView = findViewById(R.id.recyclerview_search_address);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((OnCustomClickListener) this);

    }

    //리스트뷰 아이템 클릭 리스너. onCreate 내부에 작성하면 가독성이 떨어져 외부에 따로 만들어주었음.
    @Override
    public void onItemClick(View view, int position) throws InterruptedException {

            ListViewItem_search_address data = (ListViewItem_search_address) adapter.getItem(position);

            Intent intent = new Intent();//startActivity()를 할것이 아니므로 그냥 빈 인텐트로 만듦

            SearchCoordinate searchCoordinate = new SearchCoordinate(data.getRoad_address());
            searchCoordinate.start();
            searchCoordinate.join();

            intent.putExtra("postal_code", data.getPostal_code());
            intent.putExtra("road_address", data.getRoad_address());
            //intent.putExtra("jibun_address", data.getJibun_address());
            intent.putExtra("lat", searchCoordinate.getLat());
            intent.putExtra("lon", searchCoordinate.getLon());

            setResult(RESULT_OK,intent);
            finish();

    }

    @Override
    public void onItemLongClick(View view, int position) throws InterruptedException {

    }
    // html 컨넥션을 사용할 땐 무조건 쓰레드를 생성해서 처리해야 한다.
    // 주소, 좌표를 얻는 방법이 html 통신을 통해 일어나므로 별도의 쓰레드로 만들어 처리해줘야 한다.
    // 따라서 내부 쓰레드 클래스를 생성하여 처리하였다.

    // 주소 검색 메소드
    public void onClickSearchAddress(View view)  {

        try
        {
            String keyword = edit_search_address.getText().toString();
            SearchAddress searchAddress = new SearchAddress(keyword); // 주소 검색 쓰레드 객체 생성

            adapter.clear(); // 리사이클러뷰 내용 초기화
            searchAddress.start(); // 주소 검색 실행.
            searchAddress.join(); // 주소 검색 쓰레드가 모든 작업을 완료할 때 까지 대기.
            // 아래 주소 검색용 쓰레드 클래스 내부에서 adapter 갱신을 시도했으나
            // 메인 쓰레드에서 실행해야 갱신이 수행됨. 따라서 join으로 결과를 기다렸다가 이후에 작업을 수행.
            hideKeyboard(activity_popup_address.this);
            adapter.notifyDataSetChanged();
        }
        catch (Exception e)
        {

        }

    }

    // 검색 버튼을 누르면 listview에 정보가 출력되어야 하지만 가상키보드가 활성화 되어있으면 listview에 정보가 출력이 안된다.
    // 따라서 가상키보드를 지우는 메소드를 사용하여 강제로 변경했다.
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);

        View view = activity.getCurrentFocus();

        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    // 주소 검색을 위한 inner class
    public class SearchAddress extends Thread
    {
        private String keyword;

        public SearchAddress(String keyword)
        {
            this.keyword = keyword;
        }

        public void run()
        {
            try
            {
                parseJson(getAddressData());
            }
            catch (Exception e)
            {

            }
        }

        private String getAddressData() throws Exception
        {
            //StringBuilder를 사용하는 이유는 단일 쓰레드에서 작동할 때 빠르고
            //append, delete 같은 메소드로 간단하게 문자열을 변경할 수 있다.
            //String 객체는 불변이므로 + 연산등으로 문자열을 변경할 경우 더해진 문자열에 대한 정보도 관리해야 하므로
            //적합하지 않다.

            StringBuilder urlBuilder = new StringBuilder(getString(R.string.Load_Address_Url)); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("confmKey","UTF-8") + "=" + URLEncoder.encode(getString(R.string.Load_Address_Key), "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("currentPage","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("countPerPage","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("keyword","UTF-8") + "=" + URLEncoder.encode(keyword, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("resultType","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8"));
            //URL을 생성하고 Http 컨넥션으로 연결을 시도한다.
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // GET 방식으로 호출
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            System.out.println("Response code: " + conn.getResponseCode());
            // 버퍼 리더, 인풋 스트림 리더로 컨넥션에서 정보를 받아온다.
            BufferedReader rd;
            if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            //String 변수를 만들고 해당 변수에 위에서 받은 컨넥션정보를 받아오고 null이 아니면 StringBuilder에 append한다.
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();
            System.out.println(sb.toString());
            return sb.toString();
        }

        // json 파싱.
        private void parseJson(String data) throws Exception
        {
            JSONObject jsonObject = new JSONObject(data).getJSONObject("results");
            JSONArray juso = jsonObject.getJSONArray("juso");

            for (int i=0; i < juso.length(); i++) {

                ListViewItem_search_address address_data = new ListViewItem_search_address();

                JSONObject subJsonObject = juso.getJSONObject(i);
                String zipNo = subJsonObject.getString("zipNo");
                String jibunAddr = subJsonObject.getString("jibunAddr");
                String roadAddr = subJsonObject.getString("roadAddr");

                address_data.setPostal_code(zipNo);
                address_data.setRoad_address(roadAddr);
                address_data.setJibun_address(jibunAddr);

                adapter.addItem(address_data);
            }
        }

    }
    // 좌표 검색을 위한 inner class
    public class SearchCoordinate extends Thread
    {
        private String address;

        private String lat;
        private String lon;

        public SearchCoordinate(String address)
        {
            this.address = address;

        }

        public void run()
        {
            try
            {
                parseJson(getCoordinateData());
            }
            catch (Exception e)
            {

            }
        }

        private String getCoordinateData() throws Exception
        {
            //StringBuilder를 사용하는 이유는 단일 쓰레드에서 작동할 때 빠르고
            //append, delete 같은 메소드로 간단하게 문자열을 변경할 수 있다.
            //String 객체는 불변이므로 + 연산등으로 문자열을 변경할 경우 더해진 문자열에 대한 정보도 관리해야 하므로
            //적합하지 않다.


            StringBuilder urlBuilder = new StringBuilder(getString(R.string.Address_Coord_Url)); /*URL*/
            urlBuilder.append("&" + URLEncoder.encode("crs","UTF-8") + "=" + URLEncoder.encode("epsg:4326", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("address","UTF-8") + "=" + URLEncoder.encode(address, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("refine","UTF-8") + "=" + URLEncoder.encode("true", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("simple","UTF-8") + "=" + URLEncoder.encode("true", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("format","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("type","UTF-8") + "=" + URLEncoder.encode("road", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("key","UTF-8") + "=" + URLEncoder.encode(getString(R.string.Address_Coord_Key), "UTF-8"));
            //URL을 생성하고 Http 컨넥션으로 연결을 시도한다.
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // GET 방식으로 호출
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            System.out.println("Response code: " + conn.getResponseCode());
            // 버퍼 리더, 인풋 스트림 리더로 컨넥션에서 정보를 받아온다.
            BufferedReader rd;
            if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            //String 변수를 만들고 해당 변수에 위에서 받은 컨넥션정보를 받아오고 null이 아니면 StringBuilder에 append한다.
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();
            System.out.println(sb.toString());
            return sb.toString();
        }

        // json 파싱.
        private void parseJson(String data) throws Exception
        {
            JSONObject jsonObject = new JSONObject(data).getJSONObject("response");
            JSONObject result = jsonObject.getJSONObject("result");
            JSONObject point = result.getJSONObject("point");

            lat = point.getString("y");
            lon = point.getString("x");
        }

        public String getLat() {
            return lat;
        }

        public String getLon() {
            return lon;
        }


    }

}