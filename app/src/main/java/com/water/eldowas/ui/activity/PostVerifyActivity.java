package com.water.eldowas.ui.activity;

import android.content.Intent;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.water.eldowas.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostVerifyActivity extends AppCompatActivity {


    private TextView nameTx;
    private TextView positionTx;
    private CircleImageView imageAvartar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_verify);

        nameTx = (TextView)findViewById(R.id.post_verify_name);

        imageAvartar = (CircleImageView)findViewById(R.id.post_verify_icon);
        positionTx = (TextView)findViewById(R.id.post_verify_position) ;

        Intent intent =  getIntent();

        String verified = intent.getStringExtra("VERIFICATION");
        String name = intent.getStringExtra("NAME");
        String position = intent.getStringExtra("POSITION");


        if(verified.equalsIgnoreCase("TRUE")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                imageAvartar.setImageDrawable(getResources().getDrawable(R.drawable.personnel_verified, getApplicationContext().getTheme()));
                positionTx.setText(position);
                nameTx.setText(name);
            } else {
                imageAvartar.setImageDrawable(getResources().getDrawable(R.drawable.personnel_verified));
                positionTx.setText(position);
                nameTx.setText(name);
            }
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                imageAvartar.setImageDrawable(getResources().getDrawable(R.drawable.personnel_not_verified, getApplicationContext().getTheme()));
            } else {
                imageAvartar.setImageDrawable(getResources().getDrawable(R.drawable.personnel_not_verified));
            }
        }



    }
}
