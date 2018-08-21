package com.zjl.myfactory;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.zjl.uikit.inputcodeview.InputCodeView;

public class InputCodeActivity extends AppCompatActivity {
    private InputCodeView inputCodeView;
    private TextView tvResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_code);

        inputCodeView = findViewById(R.id.verificationcodeview);
        tvResult = findViewById(R.id.tv_tip);

        inputCodeView.setOnCodeFinishListener(new InputCodeView.OnCodeFinishListener() {
            @Override
            public void onComplete(String content) {
                tvResult.setText(content);
//                startActivity(new Intent(InputCodeActivity.this, TabbedActivity.class));
            }
        });
    }
}
