package com.water.eldowas.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.SSLCertificateSocketFactory;
import android.net.SSLSessionCache;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.water.eldowas.R;
import com.water.eldowas.model.AccountObject;
import com.water.eldowas.model.BillSummary;
import com.water.eldowas.util.Helper;
import com.water.eldowas.util.TokenObject;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class AddAccountActivity extends AppCompatActivity {

    private EditText accountNameEv;
    private EditText accountNumberEv;
    private Button addBtn;

    private String uid;
    private String user_name;
    private String firstname;
    private String lastname;

    private String userName;
    private String userAccountNumber;
    private String accountName;
    private String accountNumber;
    private TokenObject tokenObject;
    public static final String USERS_ACCOUNTS = "CustomersAccount";

    private FirebaseAuth firebaseAuth;
    public static final String USERS_CHILD = "Vaultusers";
    //defining a database reference
    private DatabaseReference databaseReference ;
    private String accountIndex;
    private RequestQueue queue;
    private List<BillSummary> BillSummaryList = new ArrayList<BillSummary>();
    private String subZone;
    private String phoneNumber;


    private String TAG = "AddAcountActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);

        Intent myIntent = getIntent();
        uid = myIntent.getStringExtra("USER_ID");
        phoneNumber = myIntent.getStringExtra("PHONE");
        user_name = myIntent.getStringExtra("USER_NAME");
        firstname = myIntent.getStringExtra("USER_FIRST_NAME");
        lastname = myIntent.getStringExtra("USER_LAST_NAME");
        userName  = myIntent.getStringExtra("USER_NAME");
        userAccountNumber  = myIntent.getStringExtra("ACCOUNT_NUMBER");
        subZone = myIntent.getStringExtra("SUB_ZONE");








        databaseReference = FirebaseDatabase.getInstance().getReference ();

        accountNameEv = (EditText)findViewById(R.id.add_account_name);
        accountNumberEv = (EditText)findViewById(R.id.add_account_number);
        accountNameEv.setText(userName);
        accountNumberEv.setText(userAccountNumber);
        addBtn = (Button)findViewById(R.id.add_account_btn);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager ConnectionManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo=ConnectionManager.getActiveNetworkInfo();
                if(networkInfo != null && networkInfo.isConnected()==true )
                {

                saveUserInformation();
            }
                else
            {
                Toast.makeText(AddAccountActivity.this, "No Internet connection", Toast.LENGTH_LONG).show();

            }
            }
        });
    }


    public void saveUserInformation( ) {

        accountName = accountNameEv.getText().toString ().trim();
        accountNumber = accountNumberEv.getText().toString ().trim();
        getstations(accountNumber, accountName, accountNumber);




    }

    public class ClientSSLSocketFactory extends SSLCertificateSocketFactory {
        private SSLContext sslContext;

        /**
         * @param handshakeTimeoutMillis
         * @deprecated Use {@link #getDefault(int)} instead.
         */
        public ClientSSLSocketFactory(int handshakeTimeoutMillis) {
            super(handshakeTimeoutMillis);
        }

        public SSLSocketFactory getSocketFactory(){
            try
            {
                X509TrustManager tm = new X509TrustManager() {
                    public void checkClientTrusted
                            (X509Certificate[] xcs, String string)
                            throws CertificateException {}
                    public void checkServerTrusted
                            (X509Certificate[] xcs, String string)
                            throws CertificateException {}
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                };
                sslContext =
                        SSLContext.getInstance( "TLS" );
                sslContext.init( null, new
                        TrustManager[] { tm }, null);
                SSLSocketFactory ssf =
                        SummaryActivity.ClientSSLSocketFactory.getDefault
                                (10000 , new SSLSessionCache
                                        (AddAccountActivity.this));
                return ssf;
            } catch ( Exception ex) {
                return null;
            }
        }
        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException,
                UnknownHostException {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }
        @Override
        public Socket createSocket() throws
                IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }


    private void getstations(final String accountId, String accountName, String accountNumber) {
        {

            ClientSSLSocketFactory cssl = new ClientSSLSocketFactory(30000);

            queue = Volley.newRequestQueue(this, new HurlStack(null,
                    cssl.getSocketFactory()));



            StringRequest stringPostRequest = new StringRequest(Request.Method.POST, Helper.PATH_TO_VERIFY_ACCOUNT_NUMBER, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {


                    GsonBuilder builder = new GsonBuilder();
                    Gson gson = builder.create();
                    tokenObject = gson.fromJson(response, TokenObject.class);

                    if(subZone== null){
                        subZone = tokenObject.getToken();
                    }

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
                    if (tokenObject.getToken().equalsIgnoreCase("0" ) || tokenObject.getToken().equalsIgnoreCase("1" ) ) {
//            Toast.makeText(getApplicationContext(), "Enter your first name!", Toast.LENGTH_SHORT).show();
                        accountNumberEv.setError("The account number does not exist!");
                        return;//|| tokenObject.getToken().equalsIgnoreCase("0")
                    }



// String uid, String firstName, String lastName, String meterNumber, String plotNumber, String meterName

                    Map<String, Object> userData = new HashMap<String, Object>();
                    userData.put("userName",accountName );
                    userData.put("firstName",accountNumber );



                    accountIndex =  databaseReference.child(USERS_ACCOUNTS).child(uid).push().getKey();
                    AccountObject accountObject = new AccountObject(accountName, accountId ,subZone, phoneNumber, uid,accountIndex);

                    //  Contacts contacts = new Contacts("0",uid, firstname, lastname, accountNumber, "0", accountName , accountIndex, phoneNumber, subZone);
                    databaseReference.child(USERS_ACCOUNTS).child(uid).child(accountIndex).setValue(accountObject).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.e(TAG, "on complet : " + task.toString());
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.e(TAG, " successful");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "Exception: "  + e.toString());
                        }
                    });
                    addToLocal(accountObject);

                    Toast.makeText( AddAccountActivity.this , "Information Saved..." , Toast.LENGTH_LONG ). show ();

                    Intent myIntent = new Intent();
                    // myIntent.putExtra("USER_ID", uid);
                    myIntent.putExtra("USER_ID",uid );
                    myIntent.putExtra("PHONE",phoneNumber );

                    setResult(RESULT_OK, myIntent);
                    finish();






                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            //                             responseProgBar.setVisibility(ProgressBar.GONE);
                            Toast.makeText(AddAccountActivity.this, volleyError.toString(), Toast.LENGTH_LONG).show();



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
      /*  if(getSupportFragmentManager().getBackStackEntryCount()>0)getSupportFragmentManager().popBackStack();
        else */
        finish();

    }

    private void addToLocal(AccountObject accountObject) {
        {

            AddAccountActivity.ClientSSLSocketFactory cssl = new AddAccountActivity.ClientSSLSocketFactory(30000);

            queue = Volley.newRequestQueue(this, new HurlStack(null,
                    cssl.getSocketFactory()));



            StringRequest stringPostRequest = new StringRequest(Request.Method.POST, Helper.PATH_TO_INSERT_LOCAL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                 return;

}
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            //                             responseProgBar.setVisibility(ProgressBar.GONE);
                            Toast.makeText(AddAccountActivity.this, volleyError.toString(), Toast.LENGTH_LONG).show();



                        }
                    }) {
//(final String uid,  final String userName, final String comments, final String likes,  final String retweets, final String cash, final String phone)

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("AccountNumber", accountObject.getAccountNumber());
                    params.put("AccountRoute", accountObject.getAccountRoute());
                    params.put("AccountPhoneNumber", accountObject.getAccountPhoneNumber());
                    params.put("userId", accountObject.getUserId());



                    return params;
                }
            };
//        stringPostRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            stringPostRequest.setRetryPolicy(new DefaultRetryPolicy(10* DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 0));
            queue.add(stringPostRequest);
        }

    }
}
