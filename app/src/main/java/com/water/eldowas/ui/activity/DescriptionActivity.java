package com.water.eldowas.ui.activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.water.eldowas.R;

public class DescriptionActivity extends AppCompatActivity {

    private Button finishBtn;
    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        editText = (EditText)findViewById(R.id.description_txt);
        finishBtn = (Button)findViewById(R.id.finish_btn);


        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DescriptionActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });



    }
}
