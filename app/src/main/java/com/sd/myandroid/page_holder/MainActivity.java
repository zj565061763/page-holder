package com.sd.myandroid.page_holder;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.sd.lib.holder.page.FPageHolder;

public class MainActivity extends AppCompatActivity {
    private final FPageHolder mPageHolder = new FPageHolder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        assert mPageHolder.getPageForRefresh() == 1;
        assert mPageHolder.getPageForLoadMore() == 1;
    }
}
