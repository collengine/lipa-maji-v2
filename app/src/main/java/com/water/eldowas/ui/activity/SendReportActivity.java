package com.water.eldowas.ui.activity;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.water.eldowas.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SendReportActivity extends AppCompatActivity {
    private static final int REQUEST_CAPTURE_IMAGE = 100;
    private ImageView post_image_review;
    private String imageFilePath;
    private ImageView captureBtn;
    private CardView postImegeCv;
    private CardView preImegeCv;
    private Button verifyLocationBtn;
    private String imageUrl;
    private String index;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_report);

        captureBtn = (ImageView)findViewById(R.id.capture_image_btn);
        post_image_review = (ImageView)findViewById(R.id.post_image_review);
        postImegeCv = (CardView)findViewById(R.id.post_image_cv);
        preImegeCv = (CardView)findViewById(R.id.pre_image_cv);
        verifyLocationBtn = (Button)findViewById(R.id.verify_location_btn);

       /* verifyLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SendReportActivity.this, MapsActivity.class);
                intent.putExtra("INDEX", index);
                intent.putExtra("IMAGEURL", imageUrl);
                startActivity(intent);
            }
        });*/


        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCameraIntent();
            }
        });


    }


    private void openCameraIntent() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if(pictureIntent.resolveActivity(getPackageManager()) != null){
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File...
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,"com.water.eldowas.provider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        photoURI);
                startActivityForResult(pictureIntent,
                        REQUEST_CAPTURE_IMAGE);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            preImegeCv.setVisibility(CardView.GONE);
            postImegeCv.setVisibility(CardView.VISIBLE);
            Glide.with(this).load(imageFilePath).into(post_image_review);
        }
        else if(resultCode == Activity.RESULT_CANCELED) {
            // User Cancelled the action
        }
    }


    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }
}
