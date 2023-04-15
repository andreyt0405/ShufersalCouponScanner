package com.barcode.gen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "WindowWight";
    Context context;
    List<String> codeCoupon;
    List<String> codeAmount;
    public RecyclerViewAdapter(Context context, List<String> codeCoupon, List<String> codeAmount)
    {
        this.context = context;
        this.codeCoupon = codeCoupon;
        this.codeAmount = codeAmount;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recyclerview_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String codeAmText =  holder.couponAmountText.getText().toString();
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        holder.couponImageView.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            //
            if (holder.couponImageView.getWidth() > 0 && holder.couponImageView.getHeight() > 0) {
                try {
                    if (codeCoupon != null && codeAmount != null) {
                        BitMatrix bitMatrix = multiFormatWriter.encode(codeCoupon.get(position), BarcodeFormat.CODE_128,
                                holder.couponImageView.getWidth(), holder.couponImageView.getHeight());
                        Bitmap bitmap = Bitmap.createBitmap(holder.couponImageView.getWidth(), holder.couponImageView.getHeight(),
                                Bitmap.Config.RGB_565);
                        for (int i = 0; i < holder.couponImageView.getWidth(); i++) {
                            for (int j = 0; j < holder.couponImageView.getHeight(); j++) {
                                bitmap.setPixel(i, j, bitMatrix.get(i, j) ? Color.BLACK : Color.WHITE);
                            }
                        }
                        holder.couponImageView.setImageBitmap(bitmap);
                        holder.TextCouponPolicy.setVisibility(View.VISIBLE);
                        holder.codeCouponTextView.setVisibility(View.VISIBLE);
                        holder.noCodeCoupon.setVisibility(View.INVISIBLE);
                        holder.couponAmountText.setVisibility(View.VISIBLE);
                        holder.couponAmountText.setText("סכום הניצול בשובר הינו: "+codeAmount.get(position)+" ש'ח");
                        holder.codeCouponTextView.setText(codeCoupon.get(position));
                    }
                }
                catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(codeCoupon == null ||codeCoupon.size() == 0)
        {
            return 1;
        }
        else {
            Log.i("ssda",String.valueOf(codeAmount.size()));
            return codeCoupon.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView couponImageView;
        TextView codeCouponTextView,TextCouponPolicy,noCodeCoupon,couponAmountText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            couponImageView = itemView.findViewById(R.id.ImageRecyclerView);
            codeCouponTextView = itemView.findViewById(R.id.recyclerTextCode);
            TextCouponPolicy = itemView.findViewById(R.id.recyclerTextCoupon);
            noCodeCoupon = itemView.findViewById(R.id.recyclerTextNoCoupon);
            couponAmountText = itemView.findViewById(R.id.recyclerCouponAmount);
        }
    }
}

