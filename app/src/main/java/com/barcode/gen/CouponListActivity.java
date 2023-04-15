package com.barcode.gen;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

public class CouponListActivity extends AppCompatActivity {
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_list);
        ActionBar actionBar = getSupportActionBar();
        SpannableString s = new SpannableString(actionBar.getTitle());
        s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, actionBar.getTitle().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.red)));
            getSupportActionBar().setTitle(s);

        }
        recyclerView = findViewById(R.id.recyclerListCoupon);
        RecyclerViewAdapter recyclerViewAdapter =
                new RecyclerViewAdapter(this,SharedPreference.loadData(this,"codeBarcode"),
                        SharedPreference.loadData(this,"codeAmount"));
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}