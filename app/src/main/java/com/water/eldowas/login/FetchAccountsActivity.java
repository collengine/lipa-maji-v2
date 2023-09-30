package com.water.eldowas.login;




import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.SSLCertificateSocketFactory;
import android.net.SSLSessionCache;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ProgressBar;

import android.widget.TextView;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.water.eldowas.R;
import com.water.eldowas.model.AccountObject;
import com.water.eldowas.model.UserAccount;
import com.water.eldowas.ui.activity.PhoneVerificationActivity;
import com.water.eldowas.ui.activity.SummaryActivity;
import com.water.eldowas.util.Helper;
import com.water.eldowas.util.Item_Model;
import com.water.eldowas.util.RecyclerClick_Listener;
import com.water.eldowas.util.RecyclerTouchListener;
import com.water.eldowas.util.RecyclerView_Adapter;
import com.water.eldowas.util.TokenObject;
import com.water.eldowas.util.Toolbar_ActionMode_Callback;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.io.IOException;
import java.lang.reflect.Type;
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

/**
 * Created by SONU on 27/03/16.
 */
public class FetchAccountsActivity extends AppCompatActivity  {
    private static View view;
    private static RecyclerView recyclerView;
    private static ArrayList<Item_Model> item_models;
    private static RecyclerView_Adapter adapter;
    private ActionMode mActionMode;
    private  FloatingActionButton fabAddAccount;
    private DatabaseReference mFirebaseDatabaseReference;
    private String uiduserx;
    private static com.water.eldowas.RecyclerView_Fragment fragment;

    private String user_name;
    private String firstname;
    private String lastname;
    public String accountNumber;
    private String uid;

    private String phoneNumber;
    private String validPhoneNumber;
    private String TAG = "FetchAccount";
    public static final String USERS_CHILD = "Vaultusers";
    //defining a database reference
    private DatabaseReference databaseReference ;
    private String accountIndex;
    private RequestQueue queue;
    public static final String USERS_ACCOUNTS = "CustomersAccount";
    private TokenObject tokenObject;
    private TextView searchNoResults;
    private ProgressBar progressBar;
    PhoneVerificationActivity phoneSingout;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_accounts);
        searchNoResults = (TextView)findViewById(R.id.account_search_no_results);

        Intent intent = getIntent();
        phoneNumber = intent.getStringExtra("PHONE");
        uiduserx=  intent.getStringExtra("USER_ID");


        validPhoneNumber = "0" + phoneNumber.substring(4);

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference().child(USERS_ACCOUNTS).child(uiduserx);

        databaseReference = FirebaseDatabase.getInstance().getReference ();

        fabAddAccount = (FloatingActionButton)findViewById(R.id.fab_add_new_account);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        fabAddAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( adapter.getSelectedCount() < 1){
                    Toast.makeText(FetchAccountsActivity.this, "Please select an account(s) to proceed",Toast.LENGTH_SHORT).show();
                }

                SparseBooleanArray sparseBooleanArray =  adapter.getSelectedIds();

                int sizeSelected = adapter.getSelectedCount();

                for (int i = 0 ; i < sparseBooleanArray.size(); i++){
                    AccountObject accountObject = new AccountObject(item_models.get(sparseBooleanArray.keyAt(i)).getTitle(), item_models.get(sparseBooleanArray.keyAt(i)).getSubTitle() ,item_models.get(sparseBooleanArray.keyAt(i)).getIndex(), phoneNumber, uiduserx, item_models.get(sparseBooleanArray.keyAt(i)).getDbIndex());

                    accountIndex =  databaseReference.child(USERS_ACCOUNTS).child(uiduserx).push().getKey();

                    databaseReference.child(USERS_ACCOUNTS).child(uiduserx).child(accountIndex).setValue(accountObject).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            task.addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    ;
                                }
                            });
                            task.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                        }
                    });

                    addToLocal(accountObject);
                }



                Intent intent1 = new Intent(FetchAccountsActivity.this, LoginActivity.class);
                intent1.putExtra("USER_ID", uiduserx );
                startActivity(intent1);
                finish();





            }
        });








        FirebaseDatabase.getInstance().getReference().child(USERS_CHILD).child(uiduserx).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {// mFirebaseDatabaseReference.child(USERS_CHILD).child(uiduserx).child(FOLLOWING)

                user_name = dataSnapshot.child("userName").getValue(String.class);
                uid = dataSnapshot.child("uid").getValue(String.class);
                firstname = dataSnapshot.child("firstName").getValue(String.class);
                lastname = dataSnapshot.child("lastName").getValue(String.class);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        getstations(validPhoneNumber);
        populateRecyclerView();
        implementRecyclerViewClickListeners();
    }



    //Populate ListView with dummy data account_search_no_results
    private void populateRecyclerView() {
        item_models = new ArrayList<>();





        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(FetchAccountsActivity.this));


        adapter = new RecyclerView_Adapter(FetchAccountsActivity.this, item_models, "Account");

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }

    //Implement item click and long click over recycler view
    private void implementRecyclerViewClickListeners() {
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(FetchAccountsActivity.this, recyclerView, new RecyclerClick_Listener() {
            @Override
            public void onClick(View view, int position) {
                //If ActionMode not null select item
                if (mActionMode != null){
                    onListItemSelect(position);
                }else if(mActionMode == null){
                    onListItemSelect(position);

                }

            }

            @Override
            public void onLongClick(View view, int position) {
                //Select item on long click
                onListItemSelect(position);
            }

            public void onSingleTab(View view, int position) {
                //Select item on long click
                onListItemSelect(position);
            }
        }));
    }

    //List item select method
    private void onListItemSelect(int position) {
        adapter.toggleSelection(position);//Toggle the selection

        boolean hasCheckedItems = adapter.getSelectedCount() > 0;//Check if any items are already selected or not

        if( hasCheckedItems ){
            fabAddAccount.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.claim_bg)));
        }else{
            fabAddAccount.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));

        }
        if (hasCheckedItems && mActionMode == null){
            mActionMode = ((AppCompatActivity) FetchAccountsActivity.this).startSupportActionMode(new Toolbar_ActionMode_Callback(FetchAccountsActivity.this,adapter, null, item_models, false, uiduserx));


        }
        // there are some selected items, start the actionMode
        else if (!hasCheckedItems && mActionMode != null)
            // there no selected items, finish the actionMode
            mActionMode.finish();

        if (mActionMode != null)
            //set action mode title on item selection
            mActionMode.setTitle(String.valueOf(adapter
                    .getSelectedCount()) + " selected");


    }
    //Set action mode null after use
    public void setNullToActionMode() {
        if (mActionMode != null)
            mActionMode = null;
    }

    //Delete selected rows
    public void deleteRows() {
        SparseBooleanArray selected = adapter
                .getSelectedIds();//Get selected ids

        //Loop all selected ids
        for (int i = (selected.size() - 1); i >= 0; i--) {
            if (selected.valueAt(i)) {
                //If current id is selected remove the item via key
                item_models.remove(selected.keyAt(i));
                adapter.notifyDataSetChanged();//notify adapter

            }
        }
        Toast.makeText(FetchAccountsActivity.this, selected.size() + " item deleted.", Toast.LENGTH_SHORT).show();//Show Toast
        mActionMode.finish();//Finish action mode after use

    }

    public void itemRemoved(int position){


//        messagereference = FirebaseDatabase.getInstance().getReference().child(MESSAGES_CHILD);
//        DatabaseReference ref = mFirebaseAdapter.getRef(position);
//        String key = ref.getKey();
//        messagereference.child(key).removeValue();
//        Toast.makeText(getContext(),"Item deleted :", Toast.LENGTH_SHORT).show();
//        formAdapter.notifyDataSetChanged();

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
                                        (FetchAccountsActivity.this));
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


    private void addToLocal(AccountObject accountObject) {
        {

            FetchAccountsActivity.ClientSSLSocketFactory cssl = new FetchAccountsActivity.ClientSSLSocketFactory(30000);

            queue = Volley.newRequestQueue(this, new HurlStack(null,
                    cssl.getSocketFactory()));



            StringRequest stringPostRequest = new StringRequest(Request.Method.POST, Helper.PATH_TO_INSERT_LOCAL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //            responseProgBar.setVisibility(ProgressBar.GONE);






                    return;





                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            //                             responseProgBar.setVisibility(ProgressBar.GONE);
                            Toast.makeText(FetchAccountsActivity.this, volleyError.toString(), Toast.LENGTH_LONG).show();



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

    private void getstations(final String validPhoneNumber) {
        {

            FetchAccountsActivity.ClientSSLSocketFactory cssl = new FetchAccountsActivity.ClientSSLSocketFactory(30000);

            queue = Volley.newRequestQueue(this, new HurlStack(null,
                    cssl.getSocketFactory()));



            StringRequest stringPostRequest = new StringRequest(Request.Method.POST, Helper.PATH_TO_GET_USER_ACCOUNTS, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //            responseProgBar.setVisibility(ProgressBar.GONE);
                    Gson gsons = new Gson();
                    AccountObject[] weathers = gsons.fromJson(response, AccountObject[].class);

                    Type types = new TypeToken<List<UserAccount>>() {
                    }.getType();

                    List<UserAccount> userAccountes = gsons.fromJson(response, types);


                    if (userAccountes.size() == 0) {
                        //    searchNoResults.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);


                        new SweetAlertDialog(FetchAccountsActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Accounts")
                                .setContentText("No account associated with this phone number \n You will not be able to use this application").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.cancel();
                                //   databaseReference.child(POINTS_CHILD).child(userUid).updateChildren(userData);

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                       mFirebaseAuth = FirebaseAuth.getInstance();
                                       mFirebaseAuth.signOut();
                                       startActivity(new Intent(FetchAccountsActivity.this, PhoneAuthActivity.class));
                                       finish();
                                    }
                                }, 3000);

                            }
                        })
                                .show();


                    }else if(!response.toString().equalsIgnoreCase("null")){
                        searchNoResults.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        Gson gson = new Gson();
                        AccountObject[] weather = gson.fromJson(response, AccountObject[].class);
                        {
                            searchNoResults.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);

                            Type type = new TypeToken<List<UserAccount>>() {
                            }.getType();

                            List<UserAccount> userAccounts = gson.fromJson(response, type);



                            for (UserAccount userAccount : userAccounts) {

                                item_models.add(new Item_Model(userAccount.getCustomerName(), userAccount.getAccountNo(), userAccount.getSubZone(),"Empty"));
                                recyclerView.setVisibility(View.VISIBLE);

                                progressBar.setVisibility(View.GONE);
                                fabAddAccount.setVisibility(View.VISIBLE);
                                adapter.notifyDataSetChanged();
                            }


                            return;





                        }}}
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            //                             responseProgBar.setVisibility(ProgressBar.GONE);
                            Toast.makeText(FetchAccountsActivity.this, volleyError.toString(), Toast.LENGTH_LONG).show();



                        }
                    }) {
//(final String uid,  final String userName, final String comments, final String likes,  final String retweets, final String cash, final String phone)

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("phoneNumber", validPhoneNumber);



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
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseAuth.signOut();
        startActivity(new Intent(FetchAccountsActivity.this, PhoneAuthActivity.class));
        finish();

        super.onBackPressed();
    }




}