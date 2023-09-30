package com.water.eldowas.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.SSLCertificateSocketFactory;
import android.net.SSLSessionCache;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.water.eldowas.model.SearchResults;

import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;


import java.util.HashMap;
import java.util.Map;


import com.water.eldowas.R;

import com.water.eldowas.ui.adapter.SearchAdapter;
import com.water.eldowas.util.Helper;
import com.water.eldowas.util.MyDividerItemDecoration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class SearchActivity extends AppCompatActivity implements SearchAdapter.ContactsAdapterListener {
    private static final String TAG = SearchActivity.class.getSimpleName();
    //    private RecyclerView recyclerView;
    private List<SearchResults> contacts = new ArrayList<>();

    private RequestQueue queue;
    //    private SearchAdapter contactsAdapter;
    private  static String uiduserx;
    public static final String USERS_CHILD = "Vaultusers";
    private List<SearchResults> contactsKlone = new ArrayList<>();
    private SearchAdapter mAdapter;
    private SearchView searchView;
    private Context context;

    private List<SearchResults> BillSummaryList = new ArrayList<SearchResults>();

    private DatabaseReference mFirebaseDatabaseReference;
    private DatabaseReference messagereference;
    private DatabaseReference followmessagereference;
    private ProgressBar mProgressBar;
    private SharedPreferences mSharedPreferences;
    private RecyclerView mMessageRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;

    // url to fetch contacts json
    private static final String URL = "https://api.androidhive.info/json/contacts.json";
    private EditText searchEdit;
    private ImageButton searchBtn;
    private ProgressBar searchPb;
    private TextView searchNoResults;
    private String phoneNumber;
    private Button searchViewBill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search2);
//        Toolbar toolbar = findViewById(R.id.toolbar_search);
//        setSupportActionBar(toolbar);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });

        // toolbar fancy stuff
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("Contacts");
        context = getApplicationContext();



        Intent myIntent = getIntent();//mIntent.putExtra("USER_ID", uid);
        uiduserx = myIntent.getStringExtra("USER_ID");
        phoneNumber = myIntent.getStringExtra("PHONE");

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        mMessageRecyclerView = (RecyclerView)findViewById(R.id.recycler_view_search);
        mMessageRecyclerView.setLayoutManager(mLayoutManager);//(DatabaseReference ref,Context context, List<Contacts> contactList, ContactsAdapterListener listener) {


        mMessageRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mMessageRecyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));

        searchPb = (ProgressBar)findViewById(R.id.search_progress_bar);
        searchBtn = (ImageButton) findViewById(R.id.search_img_btn);
        searchEdit = (EditText)findViewById(R.id.search_edit_text);
        searchNoResults = (TextView)findViewById(R.id.search_no_results);
        searchViewBill=(Button) findViewById(R.id.btn_search_bill);








        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 3) {
                    searchBtn.setEnabled(true);
                    searchNoResults.setVisibility(TextView.GONE);
                    contacts.clear();
                    mAdapter.notifyDataSetChanged();
                  //  searchBtn.setBackgroundTintList(getColorStateList(getColor(R.color.White_full)));
                } else {
                    searchBtn.setEnabled(false);
                 //   searchBtn.setBackgroundTintList(getColorStateList(getColor(R.color.grey)));
                    searchNoResults.setVisibility(TextView.GONE);
                    contacts.clear();
                    mAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConnectivityManager ConnectionManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo=ConnectionManager.getActiveNetworkInfo();
                if(networkInfo != null && networkInfo.isConnected()==true )
                {


               String searchItem = searchEdit.getText().toString ().trim();

                if (TextUtils.isEmpty(searchItem)) {
//            Toast.makeText(getApplicationContext(), "Enter user name!", Toast.LENGTH_SHORT).show();
                    searchEdit.setError("Enter Search details!");

                    return;
                }else{
                    getstations(searchItem);
                }



        }
        else
        {
            Toast.makeText(SearchActivity.this, "No Internet connection", Toast.LENGTH_LONG).show();

        }


            }
        });

//        mMessageRecyclerView.setAdapter(mAdapter);


        whiteNotificationBar(mMessageRecyclerView);
        contacts = new ArrayList<>();
        contactsKlone = new ArrayList<>();
        mAdapter = new SearchAdapter(this, contacts, this);



        mMessageRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public void onBackPressed() {
        // close search view on back button pressed
//        if (!searchView.isIconified()) {
//            searchView.setIconified(true);
//            return;
//        }
        super.onBackPressed();
    }

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.BLUE);
        }
    }



//
//    @Override
//    public void onContactSelected(Contacts contact) {
//

//
//    }

    private void getstations(final String accountId) {
        {

            searchPb.setVisibility(ProgressBar.VISIBLE);
            contacts.clear();
            SearchActivity.ClientSSLSocketFactory cssl = new SearchActivity.ClientSSLSocketFactory(30000);

            queue = Volley.newRequestQueue(this, new HurlStack(null,
                    cssl.getSocketFactory()));



            StringRequest stringPostRequest = new StringRequest(Request.Method.POST, Helper.PATH_TO_SEARCH_ACCOUNT_NUMBER, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //            responseProgBar.setVisibility(ProgressBar.GONE);
                    searchPb.setVisibility(ProgressBar.GONE);

                    if (response.toString().equalsIgnoreCase("null")) {
                        searchNoResults.setVisibility(TextView.VISIBLE);
                    }else if(!response.toString().equalsIgnoreCase("null")){
                        searchNoResults.setVisibility(TextView.GONE);
                    Gson gson = new Gson();
                    SearchResults[] weather = gson.fromJson(response, SearchResults[].class);
                    if(weather == null){
                        searchNoResults.setVisibility(TextView.VISIBLE);
                    }else{
                        searchNoResults.setVisibility(TextView.GONE);
                        BillSummaryList = Arrays.asList(weather);

                        for (SearchResults sr : weather) {

                        }

                        for (SearchResults bs : BillSummaryList) {
                            contacts.add(bs);
                            mAdapter.notifyDataSetChanged();
                        }
                    }


                }






                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            //                             responseProgBar.setVisibility(ProgressBar.GONE);
                            Toast.makeText(SearchActivity.this, volleyError.toString(), Toast.LENGTH_LONG).show();



                        }
                    }) {
//(final String uid,  final String userName, final String comments, final String likes,  final String retweets, final String cash, final String phone)

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("valz", accountId);



                    return params;
                }
            };
//        stringPostRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            stringPostRequest.setRetryPolicy(new DefaultRetryPolicy(10* DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 0));
            queue.add(stringPostRequest);
        }

    }

    @Override
    public void onContactSelected(SearchResults contact) {

        Intent mIntent2 = new Intent(this, AddAccountActivity.class);
        mIntent2.putExtra("ACCOUNT_NUMBER", contact.getAccount());
        mIntent2.putExtra("USER_NAME", contact.getName());
        mIntent2.putExtra("USER_ID", uiduserx);
        mIntent2.putExtra("SUB_ZONE", contact.getSubzone());
        mIntent2.putExtra("PHONE", phoneNumber);
        mIntent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);//Revisit this
        startActivity(mIntent2);

    }


    public void onAddBtnClicked(SearchResults contact) {

        Intent mIntent2 = new Intent(this, AddAccountActivity.class);
        mIntent2.putExtra("ACCOUNT_NUMBER", contact.getAccount());
        mIntent2.putExtra("USER_NAME", contact.getName());
        mIntent2.putExtra("USER_ID", uiduserx);
        mIntent2.putExtra("SUB_ZONE", contact.getSubzone());
        mIntent2.putExtra("PHONE", phoneNumber);
        mIntent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);//Revisit this
        startActivity(mIntent2);

    }

    public void onViewBtnClicked(SearchResults contact) {

        Intent mIntent2 = new Intent(this, ViewBillActivity.class);
        mIntent2.putExtra("ACCOUNT_NUMBER", contact.getAccount());
        //mIntent2.putExtra("USER_NAME", contact.getName());
        mIntent2.putExtra("USER_ID", uiduserx);
      //  mIntent2.putExtra("SUB_ZONE", contact.getSubzone());
        //mIntent2.putExtra("PHONE", phoneNumber);
        //mIntent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);//Revisit this
        startActivity(mIntent2);

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
                                        (SearchActivity.this));
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


    private void fetchContacts() {
        JsonArrayRequest request = new JsonArrayRequest(URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response == null) {
                            Toast.makeText(getApplicationContext(), "Couldn't fetch the contacts! Pleas try again.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        List<SearchResults> items = new Gson().fromJson(response.toString(), new TypeToken<List<SearchResults>>() {
                        }.getType());

                        // adding contacts to contacts list
                        contacts.clear();
                        contacts.addAll(items);

                        // refreshing recycler view
                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);
    }


}
