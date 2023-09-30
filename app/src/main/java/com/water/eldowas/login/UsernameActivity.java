package com.water.eldowas.login;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


import com.water.eldowas.R;

public class UsernameActivity extends AppCompatActivity {
    private EditText phoneNumberEv;
    private EditText firstnameEv;
    private EditText lastnameEv;
    private Button nextBtn;

    private String uid;
    private String phoneNumber;
    private String firstname;
    private String lastname;
    private String phoneNumbers;

    private FirebaseAuth firebaseAuth;
    public static final String USERS_CHILD = "Vaultusers";
    //defining a database reference
    private DatabaseReference databaseReference ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username);

        Intent myIntent = getIntent();
        uid = myIntent.getStringExtra("USER_ID");
        phoneNumbers = myIntent.getStringExtra("PHONE");

        databaseReference = FirebaseDatabase.getInstance().getReference ();

        phoneNumberEv = (EditText)findViewById(R.id.login_user_username);
        phoneNumberEv.setText(phoneNumbers);
        phoneNumberEv.setEnabled(false);
        firstnameEv = (EditText)findViewById(R.id.loging_user_firstname);
        lastnameEv = (EditText)findViewById(R.id.login_user_lstname);
        nextBtn = (Button)findViewById(R.id.next_button);




        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInformation();
            }
        });


    }

    public void saveUserInformation( ) {

        phoneNumber = phoneNumberEv.getText().toString ().trim();
        firstname = firstnameEv.getText().toString ().trim();
        lastname = lastnameEv.getText().toString ().trim();

        if (TextUtils.isEmpty(phoneNumber)) {
//            Toast.makeText(getApplicationContext(), "Enter user name!", Toast.LENGTH_SHORT).show();
            phoneNumberEv.setError("Enter phone number!");

            return;
        }
        if (TextUtils.isEmpty(firstname)) {
//            Toast.makeText(getApplicationContext(), "Enter your first name!", Toast.LENGTH_SHORT).show();
            firstnameEv.setError("Enter First name!");
            return;
        }
        if (TextUtils.isEmpty(lastname)) {
//            Toast.makeText(getApplicationContext(), "Enter last name!", Toast.LENGTH_SHORT).show();
            lastnameEv.setError("Enter Last name!");
            return;
        }


        // (String userName, String status, String photoUrl, String uid, String userGroup, String firstName, String lastName, String county, String bio) {

//        Intent myIntent = new Intent(EditUserName.this, collengine.com.ojblack.ChatActivity.class);
////
//
//
//        startActivity(myIntent);

        Map<String, Object> userData = new HashMap<String, Object>();
        userData.put("userName",phoneNumber );
        userData.put("firstName",firstname );
        userData.put("lastName",lastname );

        databaseReference.child(USERS_CHILD).child(uid).updateChildren(userData);

        Toast.makeText( this , "Information Saved..." ,
                Toast.LENGTH_LONG ). show ();

        Intent intent = new Intent(UsernameActivity.this, FetchAccountsActivity.class);
        // intent.putExtra("PHONE", "+254710180942"); user.getPhoneNumber()
        intent.putExtra("PHONE", phoneNumbers);
        intent.putExtra("USER_ID",uid );
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);//Revisit this
       // intent.addFlags(IN)
        startActivity(intent);
        finish();
//
//        Intent myIntent = new Intent(UsernameActivity.this, MainActivity.class);
//        myIntent.putExtra("USER_ID", uid);
//
//        startActivity(myIntent);
//
//        finish();
        //return;



    }
}
