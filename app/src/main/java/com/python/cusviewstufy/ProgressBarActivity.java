package com.python.cusviewstufy;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.python.cusviewstufy.view.NumberProgressBar;

public class ProgressBarActivity extends AppCompatActivity {

    private static final String TAG = "ProgressBarActivity";
    private NumberProgressBar mNumberProgressBar;
    private Button mBtnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_bar);

        initView();

        initListener();


    }

    private void initListener() {

        mNumberProgressBar.setOnProgressBarListener(new NumberProgressBar.OnProgressBarListener() {
            @Override
            public void onProgressChange(int progress, int max) {
                Log.e(TAG, "onProgressChange: progress = " + progress + " max = " + max);
            }
        });


        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 100; i++) {
                            SystemClock.sleep(100);
                            mNumberProgressBar.incrementProgressBy(1);
                        }
                    }
                }).start();
            }
        });
    }

    private void initView() {
        mNumberProgressBar = (NumberProgressBar) findViewById(R.id.id_number_progress_bar);
        mNumberProgressBar.setProgress(0);
        mNumberProgressBar.setMaxProgress(100);

        mBtnStart = (Button) findViewById(R.id.id_btn_start);

    }
}
