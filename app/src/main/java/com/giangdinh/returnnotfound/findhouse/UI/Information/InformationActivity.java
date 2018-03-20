package com.giangdinh.returnnotfound.findhouse.UI.Information;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.giangdinh.returnnotfound.findhouse.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InformationActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        ButterKnife.bind(this);

        initViews();
    }

    private void initViews() {
        initToolbar();
    }

    private void initToolbar() {
        toolbar.setTitle("Giới thiệu");
        setSupportActionBar(toolbar);
    }

}
