package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class activity_popup_address extends AppCompatActivity {

    private String key = "devU01TX0FVVEgyMDIxMDQxOTEyNTMwMzExMTA2NjU=";
    private EditText edit_search_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup_address);

        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        edit_search_address = (EditText)findViewById(R.id.edit_search_address);

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
                    String keyword = edit_search_address.getText().toString();
                    getAddressData(keyword);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

            }
        }).start();
    }


    private void getAddressData(String keyword) throws Exception
    {
        StringBuilder urlBuilder = new StringBuilder("https://www.juso.go.kr/addrlink/addrLinkApi.do"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("confmKey","UTF-8") + "=" + URLEncoder.encode(key, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("currentPage","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("countPerPage","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8"));
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
        System.out.println(sb.toString());
    }

}