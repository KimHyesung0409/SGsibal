package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class activity_popup_address extends AppCompatActivity {

    private String key = "devU01TX0FVVEgyMDIxMDQxOTEyNTMwMzExMTA2NjU=";
    private EditText edit_search_address;
    private ListView listview;
    private ListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup_address);

        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        edit_search_address = (EditText)findViewById(R.id.edit_search_address);
        listview = (ListView)findViewById(R.id.listview_search_address);

        adapter = new ListViewAdapter();
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                ListViewItem_search_address data = (ListViewItem_search_address) adapterView.getItemAtPosition(i);

                Intent intent = new Intent();//startActivity()를 할것이 아니므로 그냥 빈 인텐트로 만듦

                intent.putExtra("postal_code", data.getPostal_code());
                intent.putExtra("road_address", data.getRoad_address());
                //intent.putExtra("jibun_address", data.getJibun_address());

                setResult(RESULT_OK,intent);
                finish();
            }
        });

    }

    // 주소 검색 메소드
    public void onClickSearchAddress(View view)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    adapter.clearListView();
                    String keyword = edit_search_address.getText().toString();
                    parseJson(getAddressData(keyword));
                    hideKeyboard(activity_popup_address.this);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

            }
        }).start();
    }


    private String getAddressData(String keyword) throws Exception
    {
        //StringBuilder를 사용하는 이유는 단일 쓰레드에서 작동할 때 빠르고
        //append, delete 같은 메소드로 간단하게 문자열을 변경할 수 있다.
        //String 객체는 불변이므로 + 연산등으로 문자열을 변경할 경우 더해진 문자열에 대한 정보도 관리해야 하므로
        //적합하지 않다.
        StringBuilder urlBuilder = new StringBuilder("https://www.juso.go.kr/addrlink/addrLinkApi.do"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("confmKey","UTF-8") + "=" + URLEncoder.encode(key, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("currentPage","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("countPerPage","UTF-8") + "=" + URLEncoder.encode("5", "UTF-8"));
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
        return sb.toString();
    }

    // json 파싱.
    private void parseJson(String data)
    {
        try
        {
            JSONObject jsonObject = new JSONObject(data).getJSONObject("results");
            JSONArray juso = jsonObject.getJSONArray("juso");

            for (int i=0; i < juso.length(); i++) {
                JSONObject subJsonObject = juso.getJSONObject(i);
                String zipNo = subJsonObject.getString("zipNo");
                String jibunAddr = subJsonObject.getString("jibunAddr");
                String roadAddr= subJsonObject.getString("roadAddr");

                adapter.addItem(zipNo, roadAddr, jibunAddr);
            }
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

}