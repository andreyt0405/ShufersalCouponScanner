package com.barcode.gen;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText insert_text;
    private TextView instructions,whereCodeDisplay,couponNumbScan;
    private ImageView imageViewBarCode,imageViewArrow,ImageViewScanner;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        insert_text = findViewById(R.id.Text);
        imageViewBarCode = findViewById(R.id.ImageView);
        instructions = findViewById(R.id.text_to_scan);
        imageViewArrow = findViewById(R.id.asset_view);
        whereCodeDisplay = findViewById(R.id.here_code_display);
        couponNumbScan = findViewById(R.id.coupon_numb_scan);
        ImageViewScanner = findViewById(R.id.ImageViewIconScan);
        insert_text.setOnClickListener(this);
        ActionBar actionBar = getSupportActionBar();
        SpannableString s = new SpannableString(actionBar.getTitle());
        s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, actionBar.getTitle().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.red)));
            getSupportActionBar().setTitle(s);

        }
        instructions.setVisibility(View.INVISIBLE);
        imageViewArrow.setVisibility(View.INVISIBLE);
        couponNumbScan.setVisibility(View.INVISIBLE);

    }
    @SuppressLint("SetTextI18n")
    public void barCodeButton(View view) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        if (insert_text.getText().toString().matches("\\d+")) {
            try {
                BitMatrix bitMatrix = multiFormatWriter.encode(insert_text.getText().toString(), BarcodeFormat.CODE_128, imageViewBarCode.getWidth(), imageViewBarCode.getHeight());
                Bitmap bitmap = Bitmap.createBitmap(imageViewBarCode.getWidth(), imageViewBarCode.getHeight(), Bitmap.Config.RGB_565);
                for (int i = 0; i < imageViewBarCode.getWidth(); i++) {
                    for (int j = 0; j < imageViewBarCode.getHeight(); j++) {
                        bitmap.setPixel(i, j, bitMatrix.get(i, j) ? Color.BLACK : Color.WHITE);
                    }
                }
                imageViewBarCode.setImageBitmap(bitmap);
                whereCodeDisplay.setVisibility(View.INVISIBLE);
                ImageViewScanner.setVisibility(View.INVISIBLE);
                String showNumberCoupon = getResources().getString(R.string.coupon_numb) + " ";
                couponNumbScan.setText(showNumberCoupon + insert_text.getText().toString());
                couponNumbScan.setVisibility(View.VISIBLE);
                instructions.setVisibility(View.VISIBLE);
                imageViewArrow.setVisibility(View.VISIBLE);
            } catch (WriterException e) {
                e.printStackTrace();
            }

        }
        else
        {
            Toast.makeText(this, R.string.invalid_code,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {
        insert_text.getText().clear();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                intent=new Intent(this, AddCouponActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.item2:
                intent=new Intent(this, CouponListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}