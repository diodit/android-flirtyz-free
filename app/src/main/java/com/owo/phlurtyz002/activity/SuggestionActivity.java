package com.owo.phlurtyz002.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.owo.phlurtyz002.R;
import com.owo.phlurtyz002.core.BaseActivity;

public class SuggestionActivity extends BaseActivity {


    private TextView suggestFlirtyz;

    private TextView suggestionDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_suggestion);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initView();

        Typeface faceBigSurprise = Typeface.createFromAsset(getAssets(), "font/bigSurprise.ttf");

        suggestFlirtyz.setTypeface(faceBigSurprise);

        Typeface faceMyriadSemiBold = Typeface.createFromAsset(getAssets(), "font/myriadProSemibold.otf");

        suggestionDescription.setTypeface(faceMyriadSemiBold);


    }

    private void initView() {

        suggestFlirtyz = (TextView) findViewById(R.id.suggestion_header);

        suggestionDescription = (TextView) findViewById(R.id.suggestion_description);

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
