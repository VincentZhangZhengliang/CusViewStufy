package com.python.cusviewstufy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.python.cusviewstufy.view.PagerLayout;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private PagerLayout mPagerLayout;

    private String[] strs = {"123",
            "123",
            "456",
            "123",
            "456",
            "123",
            "456",
            "123",
            "456",
            "123",
            "456",
            "123",
            "456",
            "123",
            "456", "123",
            "456", "123",
            "456", "123",
            "456",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
    }

    private void initListener() {

    }

    private void initView() {
        mPagerLayout = findViewById(R.id.id_pl);
        mPagerLayout.setLayout(MainActivity.this, R.layout.slide_layout);
    }




}
