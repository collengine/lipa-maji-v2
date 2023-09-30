package com.water.eldowas.ui.activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.water.eldowas.R;

public class AboutActivity extends AppCompatActivity {
    private String userUid;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Intent myIntent = getIntent();//mIntent.putExtra("USER_ID", uid);
        userUid = myIntent.getStringExtra("USER_ID");

        btnBack = (ImageButton) findViewById(R.id.about_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mIntent2 = new Intent(AboutActivity.this, MainActivity.class);
                mIntent2.putExtra("USER_ID", userUid);
                mIntent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);//Revisit this
                startActivity(mIntent2);
                finish();
            }
        });
    }
}
