package com.guo.news.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.guo.news.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new MainFragment(),"main").commit();

    }
}
