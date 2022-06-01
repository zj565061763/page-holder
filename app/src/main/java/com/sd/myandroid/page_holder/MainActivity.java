package com.sd.myandroid.page_holder;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.sd.lib.holder.page.FPageHolder;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        test();
    }

    private void test() {
        final FPageHolder holder = new FPageHolder();

        assert holder.getCurrentPage() == 0;
        assert holder.getPageForRefresh() == 1;
        assert holder.getPageForLoadMore() == 1;
        assert holder.getPageForRequest(false) == 1;
        assert holder.getPageForRequest(true) == 1;
        assert !holder.hasNextPage();

        holder.onSuccess(false)
                .setHasData(true)
                .setHasNextPage(true)
                .update();

        assert holder.getCurrentPage() == 1;
        assert holder.getPageForRefresh() == 1;
        assert holder.getPageForLoadMore() == 2;
        assert holder.getPageForRequest(false) == 1;
        assert holder.getPageForRequest(true) == 2;
        assert holder.hasNextPage();
    }
}
