package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Switch;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class activity_reserve_entrust extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_entrust);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.layout_actionbar);

        Switch switch_change_mode = (Switch)findViewById(R.id.switch_change_mode);
        switch_change_mode.setVisibility(View.INVISIBLE);

        ListView listview = (ListView)findViewById(R.id.listview_reserve_entrust);

        ListViewAdapter adapter = new ListViewAdapter();
        listview.setAdapter(adapter);

        int images[] ={
                R.drawable.radiobutton_checked,
                R.drawable.radiobutton_unchecked,
                R.drawable.radiobutton_checked,
                R.drawable.radiobutton_unchecked,
                R.drawable.radiobutton_checked
        };

        adapter.addItem(images ,"위탁_1");
        adapter.addItem(images ,"위탁_2");

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                ListViewItem_reserve_entrust data = (ListViewItem_reserve_entrust)adapterView.getItemAtPosition(i);

                int images[] = data.getImages();

                Intent intent = new Intent(activity_reserve_entrust.this,activity_reserve_entrust_detail.class);
                intent.putExtra("images", images);
                startActivity(intent);

            }
        });

    }

}