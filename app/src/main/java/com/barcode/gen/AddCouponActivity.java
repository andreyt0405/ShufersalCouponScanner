package com.barcode.gen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


import static android.content.ContentValues.TAG;

public class AddCouponActivity extends AppCompatActivity {
    private static final int FILE_SELECT_CODE = 0;
    private ImageView CheckCoupon;
    private TextView TextCoupon;
    private String FilePath,couponCode,couponAmount;
    private Button LoadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_coupon);
        ActionBar actionBar = getSupportActionBar();
        SpannableString s = new SpannableString(actionBar.getTitle());
        s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, actionBar.getTitle().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.red)));
            getSupportActionBar().setTitle(s);

        }
        CheckCoupon = findViewById(R.id.ImageViewCheckCoupon);
        TextCoupon = findViewById(R.id.text_to_show_coupon_loaded);
        LoadButton = findViewById(R.id.button_load_coupon);
        LoadButton.setSelected(false);
        CheckCoupon.setVisibility(View.INVISIBLE);
        TextCoupon.setVisibility(View.INVISIBLE);
    }

    public void loadPdfFromDocument(View view) {
        CheckCoupon.setVisibility(View.INVISIBLE);
        TextCoupon.setVisibility(View.INVISIBLE);
        LoadButton.setBackground(getResources().getDrawable(R.drawable.button_off_xml));
        LoadButton.setEnabled(false);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(
                    Intent.createChooser(intent, getResources().getString(R.string.titleBrowse)),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    FilePath = createCopyAndReturnRealPath(this,Uri.parse(uri.toString()));
                }
        }
        extractPDF(FilePath);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void extractPDF(String FilePath)
    {
        try {
            String extractedText = "";
//            PdfReader reader = new PdfReader("res/raw/sample.pdf");
            @SuppressLint("SdCardPath") PdfReader reader = new PdfReader(FilePath);
            int n = reader.getNumberOfPages();
            for (int i = 0; i < n; i++) {
                extractedText = extractedText + PdfTextExtractor.getTextFromPage(reader, i + 1).trim() + "\n";
                // to extract the PDF content from the different pages
            }
            couponCode=StringParse.stringParseByRegex(extractedText,"\\d+");
            couponAmount = extractedText.contains("40")?"40":
                    extractedText.contains("50")?"50": extractedText.contains("30")?"30":"100";
            Log.i("amount",couponAmount);
            if(extractedText.contains("100"))
            {
                Log.i("YES",extractedText);
            }
            if(couponCode.matches("\\d+"))
            {
                CheckCoupon.setVisibility(View.VISIBLE);
                TextCoupon.setVisibility(View.VISIBLE);
                LoadButton.setEnabled(true);
                LoadButton.setBackground(getResources().getDrawable(R.drawable.button_xml));
            }
            reader.close();
        } catch (Exception e) {
            Log.d(TAG, "File Input: "+"Failed to extract");
        }
    }
    @Nullable
    public static String createCopyAndReturnRealPath(
            @NonNull Context context, @NonNull Uri uri) {
        final ContentResolver contentResolver = context.getContentResolver();
        if (contentResolver == null)
            return null;

        // Create file path inside app's data dir
        String filePath = context.getApplicationInfo().dataDir + File.separator
                + System.currentTimeMillis();

        File file = new File(filePath);
        try {
            InputStream inputStream = contentResolver.openInputStream(uri);
            if (inputStream == null)
                return null;

            OutputStream outputStream = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0)
                outputStream.write(buf, 0, len);

            outputStream.close();
            inputStream.close();
        } catch (IOException ignore) {
            return null;
        }

        return file.getAbsolutePath();
    }
    public void LoadCodeNumberToShared(View view)
    {
        boolean savedSharedPref;
        Log.d(TAG, "File Loading to SharedPref: ");
        File dir = getFilesDir();
        File file = new File(dir, FilePath);
        boolean deleted = file.delete();
        Log.d(TAG, "fileRemove"+String.valueOf(deleted));
        savedSharedPref = SharedPreference.saveData(this,"codeBarcode",couponCode);
        savedSharedPref = SharedPreference.saveData(this,"codeAmount",couponAmount);
        if (savedSharedPref)
        {
            Toast.makeText(this, getResources().getString(R.string.couponSaveSharedPref),
                    Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, getResources().getString(R.string.couponExistSharedPref),
                    Toast.LENGTH_SHORT).show();
        }
        Log.i("LoadSharedPref",String.valueOf(SharedPreference.loadData(this,"codeBarcode")));
        Log.i("LoadSharedPref",String.valueOf(SharedPreference.loadData(this,"codeAmount")));
    }
}