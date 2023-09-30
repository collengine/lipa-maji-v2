package com.water.eldowas.ui.activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.water.eldowas.R;
import com.water.eldowas.model.BillSummary;
import com.water.eldowas.model.Contacts;
import com.water.eldowas.util.Helper;
import com.water.eldowas.util.TokenObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditAccountActivity extends AppCompatActivity {

    private EditText accountNameEv;
    private EditText accountNumberEv;
    private Button addBtn;
    private DatabaseReference messagereference;
    private String uid;
    private String user_name;
    private String firstname;
    private String lastname;

    private String userName;
    private String userAccountNumber;
    private String accountName;
    private String accountNumber;
    private TokenObject tokenObject;
    public static final String USERS_ACCOUNTS = "UsersAccount";

    private String myAccountName;
    private String myAccountNumber;

    private FirebaseAuth firebaseAuth;
    public static final String USERS_CHILD = "Vaultusers";
    //defining a database reference
    private DatabaseReference databaseReference ;
    private String accountIndex;
    private List<BillSummary> BillSummaryList = new ArrayList<BillSummary>();
    private RequestQueue queue;
    private String TAG = "AddAcountActivity";
    private String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);

        Intent myIntent = getIntent();
        uid = myIntent.getStringExtra("USER_ID");
        user_name = myIntent.getStringExtra("USER_NAME");
        firstname = myIntent.getStringExtra("USER_FIRST_NAME");
        lastname = myIntent.getStringExtra("USER_LAST_NAME");
        userName  = myIntent.getStringExtra("USER_NAME");
        userAccountNumber  = myIntent.getStringExtra("ACCOUNT_NUMBER");
        key = myIntent.getStringExtra("KEY");

        databaseReference = FirebaseDatabase.getInstance().getReference ();

        accountNameEv = (EditText)findViewById(R.id.add_account_name);
        accountNumberEv = (EditText)findViewById(R.id.add_account_number);
        accountNameEv.setText(userName);
        accountNumberEv.setText(userAccountNumber);
        addBtn = (Button)findViewById(R.id.add_account_btn);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInformation();
            }
        });




        messagereference = FirebaseDatabase.getInstance().getReference().child(USERS_ACCOUNTS).child(uid);
        // final DatabaseReference dinosaursRef = messagereference.getReference("dinosaurs");
        messagereference.orderByKey().equalTo(key).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


               Contacts contacts = dataSnapshot.getValue(Contacts.class);
               myAccountName = contacts.getMeterName();
               myAccountNumber = contacts.getMeterNumber();
               accountNameEv.setText(myAccountName);
               accountNumberEv.setText(myAccountNumber);
       }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {


            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //3  return true;// this is where

    }


    public void saveUserInformation( ) {

        accountName = accountNameEv.getText().toString ().trim();
        accountNumber = accountNumberEv.getText().toString ().trim();
        getstations(accountNumber, accountName, accountNumber);




    }



    private void getstations(final String accountId, String accountName, String accountNumber) {
        {

            StringRequest stringPostRequest = new StringRequest(Request.Method.POST, Helper.PATH_TO_VERIFY_ACCOUNT_NUMBER, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {


                    GsonBuilder builder = new GsonBuilder();
                    Gson gson = builder.create();
                    tokenObject = gson.fromJson(response, TokenObject.class);


                    if (TextUtils.isEmpty(accountName)) {
//            Toast.makeText(getApplicationContext(), "Enter user name!", Toast.LENGTH_SHORT).show();
                        accountNameEv.setError("Enter account name!");

                        return;
                    }
                    if (TextUtils.isEmpty(accountNumber) ) {
//            Toast.makeText(getApplicationContext(), "Enter your first name!", Toast.LENGTH_SHORT).show();
                        accountNumberEv.setError("Enter account number!");
                        return;//||
                    }
                    if (tokenObject.getToken().equalsIgnoreCase("0")  ) {
//            Toast.makeText(getApplicationContext(), "Enter your first name!", Toast.LENGTH_SHORT).show();
                        accountNumberEv.setError("The account number does not exist!");
                        return;//|| tokenObject.getToken().equalsIgnoreCase("0")
                    }


                    Map<String, Object> userData = new HashMap<String, Object>();
                    userData.put("accountName",accountName );
                    userData.put("accountNumber",accountNumber );




                   // Contacts contacts = new Contacts("0",uid, firstname, lastname, accountNumber, "0", accountName , accountIndex);
                    databaseReference.child(USERS_ACCOUNTS).child(uid).child(key).updateChildren(userData);

                    Toast.makeText( EditAccountActivity.this , "Information Updated..." , Toast.LENGTH_LONG ). show ();

                    Intent myIntent = new Intent(EditAccountActivity.this, MainActivity.class);
                    myIntent.putExtra("USER_ID", uid);

                    startActivity(myIntent);

                    finish();
                    return;





                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            //                             responseProgBar.setVisibility(ProgressBar.GONE);
                            Toast.makeText(EditAccountActivity.this, volleyError.toString(), Toast.LENGTH_LONG).show();



                        }
                    }) {
//(final String uid,  final String userName, final String comments, final String likes,  final String retweets, final String cash, final String phone)

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("accountNumber", accountId);



                    return params;
                }
            };
//        stringPostRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            stringPostRequest.setRetryPolicy(new DefaultRetryPolicy(10* DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 0));
            queue.add(stringPostRequest);
        }

    }


    @Override
    public void onBackPressed(){
        if(getSupportFragmentManager().getBackStackEntryCount()>0)getSupportFragmentManager().popBackStack();
        else finish();

    }
}
