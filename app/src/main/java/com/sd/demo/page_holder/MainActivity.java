package com.sd.demo.page_holder;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.sd.lib.pholder.FPageHolder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testInit();
        testRefresh();
        testLoadMore();
    }

    private void testInit() {
        final FPageHolder holder = new FPageHolder();

        // test init
        assert holder.getCurrentPage() == 0;
        assert holder.getPageForRefresh() == 1;
        assert holder.getPageForLoadMore() == 1;
        assert !holder.hasNextPage();

        holder.onSuccess(false)
                .setHasNextPage(true)
                .setHasData(true)
                .setPage(10)
                .update();

        assert holder.getCurrentPage() == 10;
    }

    private void testRefresh() {
        final FPageHolder holder = new FPageHolder();

        // test setHasNextPage(true)
        holder.onSuccess(false)
                .setHasNextPage(true)
                .setHasData(false)
                .update();
        assert holder.hasNextPage();


        // test setHasNextPage(false)
        holder.onSuccess(false)
                .setHasNextPage(false)
                .setHasData(false)
                .update();
        assert !holder.hasNextPage();


        // test setHasData(true)
        holder.onSuccess(false)
                .setHasNextPage(true)
                .setHasData(true)
                .update();
        assert holder.getCurrentPage() == 1;


        // test setHasData(false)
        holder.onSuccess(false)
                .setHasNextPage(true)
                .setHasData(false)
                .update();
        assert holder.getCurrentPage() == 0;


        // test setHasData(Collection)
        holder.onSuccess(false)
                .setHasNextPage(true)
                .setHasData(new ArrayList<>())
                .update();
        assert holder.getCurrentPage() == 0;


        // test setHasData(Collection)
        final List<String> list1 = new ArrayList<>();
        list1.add("");
        holder.onSuccess(false)
                .setHasNextPage(true)
                .setHasData(list1)
                .update();
        assert holder.getCurrentPage() == 1;


        // test setHasData(Collection)
        final List<String> list2 = new ArrayList<>();
        list2.add("");
        holder.onSuccess(false)
                .setHasNextPage(true)
                .setHasData(list2)
                .update();
        assert holder.getCurrentPage() == 1;
    }

    private void testLoadMore() {
        final FPageHolder holder = new FPageHolder();

        // test setHasNextPage(true)
        holder.onSuccess(true)
                .setHasNextPage(true)
                .setHasData(false)
                .update();
        assert holder.hasNextPage();


        // test setHasNextPage(false)
        holder.onSuccess(true)
                .setHasNextPage(false)
                .setHasData(false)
                .update();
        assert !holder.hasNextPage();


        // test setHasData(true)
        holder.onSuccess(true)
                .setHasNextPage(true)
                .setHasData(true)
                .update();
        assert holder.getCurrentPage() == 1;


        // test setHasData(false)
        holder.onSuccess(true)
                .setHasNextPage(true)
                .setHasData(false)
                .update();
        assert holder.getCurrentPage() == 1;


        // test setHasData(Collection)
        holder.onSuccess(true)
                .setHasNextPage(true)
                .setHasData(new ArrayList<>())
                .update();
        assert holder.getCurrentPage() == 1;


        // test setHasData(Collection)
        final List<String> list1 = new ArrayList<>();
        list1.add("");
        holder.onSuccess(true)
                .setHasNextPage(true)
                .setHasData(list1)
                .update();
        assert holder.getCurrentPage() == 2;


        // test setHasData(Collection)
        final List<String> list2 = new ArrayList<>();
        list2.add("");
        holder.onSuccess(true)
                .setHasNextPage(true)
                .setHasData(list2)
                .update();
        assert holder.getCurrentPage() == 3;
    }
}
