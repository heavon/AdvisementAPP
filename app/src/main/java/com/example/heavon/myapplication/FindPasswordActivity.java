package com.example.heavon.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FindPasswordActivity extends BasicActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);

        initToolBar(getString(R.string.find_password_title));
    }
}
