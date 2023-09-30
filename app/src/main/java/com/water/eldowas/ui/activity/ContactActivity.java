package com.water.eldowas.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;

import com.water.eldowas.R;

public class ContactActivity extends AppCompatActivity {
    private String userUid;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        Intent myIntent = getIntent();//mIntent.putExtra("USER_ID", uid);
        userUid = myIntent.getStringExtra("USER_ID");
        WebView webView = (WebView) findViewById(R.id.webViewz);

        btnBack = (ImageButton) findViewById(R.id.about_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mIntent2 = new Intent(ContactActivity.this, MainActivity.class);
                mIntent2.putExtra("USER_ID", userUid);
                mIntent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);//Revisit this
                startActivity(mIntent2);
                finish();
            }
        });
        String htmlAsString = getString(R.string.html);

        Spanned htmlAsSpanned = Html.fromHtml(htmlAsString);
    webView.loadDataWithBaseURL(null, htmlAsString, "text/html", "utf-8", null);
    }
}
