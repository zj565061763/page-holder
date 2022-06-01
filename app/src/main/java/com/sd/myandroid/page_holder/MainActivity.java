package com.sd.myandroid.page_holder;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.sd.lib.holder.page.FPageHolder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testRefresh();
    }

    private void testRefresh() {
        final FPageHolder holder = new FPageHolder();

        // test init
        assert holder.getCurrentPage() == 0;
        assert holder.getPageForRefresh() == 1;
        assert holder.getPageForLoadMore() == 1;
        assert !holder.hasNextPage();


        // test setHasNextPage(true)
        holder.onSuccess(false)
                .setHasNextPage(true)
                .setHasData(false)
                .update();
        assert holder.getCurrentPage() == 0;
        assert holder.getPageForLoadMore() == 1;
        assert holder.hasNextPage();


        // test setHasNextPage(false)
        holder.onSuccess(false)
                .setHasNextPage(false)
                .setHasData(false)
                .update();
        assert holder.getCurrentPage() == 0;
        assert holder.getPageForLoadMore() == 1;
        assert !holder.hasNextPage();


        // test setHasData(true)
        holder.onSuccess(false)
                .setHasNextPage(true)
                .setHasData(true)
                .update();
        assert holder.getCurrentPage() == 1;
        assert holder.getPageForLoadMore() == 2;


        // test setHasData(false)
        holder.onSuccess(false)
                .setHasNextPage(true)
                .setHasData(false)
                .update();
        assert holder.getCurrentPage() == 0;
        assert holder.getPageForLoadMore() == 1;


        // test setHasData(Collection)
        holder.onSuccess(false)
                .setHasNextPage(true)
                .setHasData(new ArrayList<>())
                .update();
        assert holder.getCurrentPage() == 0;
        assert holder.getPageForLoadMore() == 1;


        // test setHasData(Collection)
        final List<String> list1 = new ArrayList<>();
        list1.add("");
        holder.onSuccess(false)
                .setHasNextPage(true)
                .setHasData(list1)
                .update();
        assert holder.getCurrentPage() == 1;
        assert holder.getPageForLoadMore() == 2;


        // test setHasData(Collection)
        final List<String> list2 = new ArrayList<>();
        list2.add("");
        holder.onSuccess(false)
                .setHasNextPage(true)
                .setHasData(list2)
                .update();
        assert holder.getCurrentPage() == 1;
        assert holder.getPageForLoadMore() == 2;
    }
}
