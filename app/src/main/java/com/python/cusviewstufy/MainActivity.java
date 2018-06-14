package com.python.cusviewstufy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

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
    private Button mNumberProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
    }

    private void initListener() {

        mNumberProgressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ProgressBarActivity.class));
            }
        });

    }

    private void initView() {
        mPagerLayout = findViewById(R.id.id_pl);
        mPagerLayout.setLayout(MainActivity.this, R.layout.slide_layout);
        mNumberProgressBar = (Button) findViewById(R.id.id_number_progress_bar);


    }


}
