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
        StringBuilder urlBuilder = new StringBuilder("https://www.juso.go.kr/addrlink/addrLinkApi.do"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("confmKey","UTF-8") + "=" + URLEncoder.encode(key, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("currentPage","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("countPerPage","UTF-8") + "=" + URLEncoder.encode("5", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("keyword","UTF-8") + "=" + URLEncoder.encode(keyword, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("resultType","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8"));
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        return sb.toString();
    }

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

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}