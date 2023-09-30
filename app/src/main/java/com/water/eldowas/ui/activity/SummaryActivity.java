package com.water.eldowas.ui.activity;


import android.content.Intent;
import android.net.SSLCertificateSocketFactory;
import android.net.SSLSessionCache;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;


import android.app.DatePickerDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
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
import com.water.eldowas.util.Helper;
import com.water.eldowas.ui.adapter.BillSummaryAdapter;
import com.water.eldowas.model.BillSummary;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.water.eldowas.R;

public class SummaryActivity extends AppCompatActivity{



    Button btnDatePicker, btnTimePicker,btnDatePickerb, btnTimePickerb, checkout, getBill, btnDone;
    private LinearLayout linearCont;
    EditText txtDate, txtTime, txtDateb, txtTimeb, mpesacodeText;
    //    private ProgressBar responseProgBar;
    private TextView mpesaResponse;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private int mYearb, mMonthb, mDayb, mHourb, mMinuteb;
    private int startYearb, startMonthb, startDayb;
    private int endYearb, endMonthb, endDayb;
    private long millisStart;
    private long millisEnd, millisDiff, exmillisDiff;
    private TextView timediff;
    private CharSequence timeAgo1, timeAgo2;
    private  Calendar cal1 ;
    private Calendar cal2 ;
    private   DatePickerDialog datePickerDialog1;
    private   DatePickerDialog datePickerDialog2;
    private long milliscal1;
    private long milliscal2;
    public final int PREMIUM = 5;
    public final int SILVER = 3;
    public final int GOLD = 1;
    private int charge;
    private String charged;


    private RequestQueue queue;
    private String mpesacoded;
    private String TOKENS = "tokens";


    private String theResponse;
    private String theValidator;
    private String megaResponse;
    public String adState;
    DatabaseReference mFirebaseDatabaseReference;
    private static final String ADVERTS_CHILD = "adverts";
    private static final String USERS_CHILD = "Vaultusers";
    private String date1;
    private String date2;
    private String mpesaCode;


    private String companyName;
    private String companyIconUrl;
    private String productimageUrl;
    private String TproductDescription;
    private String TproductTitle;
    private String TproductLink;
    private String CashPlan;
    private String buttonType;
    private String index;
    private String myindex;
    private String uides;
    private String TbtnType;
    private  String account_id = null;
    private List<BillSummary> BillSummaryList = new ArrayList<BillSummary>();
    private BillSummaryAdapter billSummaryAdapter;
    private RecyclerView billRecyclerView;
    private TableLayout table;

    private Button proceedBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_dummy);



        proceedBtn = (Button)findViewById(R.id.home_bill_proceed_to_bill_btn);

        table = (TableLayout) findViewById(R.id.tablelayout1);
       // TextView heading = (TextView) findViewById(R.id.myHeading);

        //    responseProgBar = (ProgressBar)findViewById(R.id.ad_progress_bar);


        Intent i = getIntent();
        account_id = i.getStringExtra("ACCOUNT_NUMBER");
        uides = i.getStringExtra("USER_ID");
        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SummaryActivity.this, MonthlyBillActivity.class);
                intent.putExtra("USER_ID", uides);
                intent.putExtra("ACCOUNT_NUMBER", account_id);
                startActivity(intent);
                finish();
            }
        });

        getstations(account_id);


    }
    private void getstations(final String accountId) {
        {

            ClientSSLSocketFactory cssl = new ClientSSLSocketFactory(30000);

            queue = Volley.newRequestQueue(this, new HurlStack(null,
                    cssl.getSocketFactory()));



            StringRequest stringPostRequest = new StringRequest(Request.Method.POST, Helper.PATH_TO_SERVER_METRO, new Response.Listener<String>() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onResponse(String response) {

                    Gson gson = new Gson();
                    BillSummary[] weather = gson.fromJson(response, BillSummary[].class);
                    BillSummaryList = Arrays.asList(weather);
                    for(BillSummary bs: BillSummaryList){
                        TableRow row = new TableRow(SummaryActivity.this);
                        TableLayout.LayoutParams tableRowParams= new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
                        tableRowParams.setMargins(0, 0, 0, 2);
                        row.setGravity(Gravity.CENTER_HORIZONTAL);
                        row.setLayoutParams(tableRowParams);

                        TextView t1 = new TextView(SummaryActivity.this);
                        TextView t2 = new TextView(SummaryActivity.this);
                        TextView t3 = new TextView(SummaryActivity.this);
                        TextView t4 = new TextView(SummaryActivity.this);
                        TextView t5 = new TextView(SummaryActivity.this);
                        TextView t6 = new TextView(SummaryActivity.this);
                        TextView t7 = new TextView(SummaryActivity.this);
                        TextView t71 = new TextView(SummaryActivity.this);
                        TextView t8 = new TextView(SummaryActivity.this);

                        t1.setText(bs.getBillNo());
                        t2.setText(bs.getBillingPeriod());
                        t3.setText(bs.getConsumption());
                        t4.setText(bs.getAccBalance());
                        t5.setText(bs.getCurrentReading());
                        t6.setText(bs.getDateOfReading());
                        t7.setText(bs.getCurrentBillAmount());
                        t71.setText(bs.getCurrentBillAmount());
                        t8.setText(bs.getDueDate());

                        t1.setGravity(Gravity.CENTER_HORIZONTAL);
                        t2.setGravity(Gravity.CENTER_HORIZONTAL);
                        t3.setGravity(Gravity.CENTER_HORIZONTAL);
                        t4.setGravity(Gravity.CENTER_HORIZONTAL);
                        t5.setGravity(Gravity.CENTER_HORIZONTAL);
                        t6.setGravity(Gravity.CENTER_HORIZONTAL);
                        t7.setGravity(Gravity.CENTER_HORIZONTAL);
                        t71.setGravity(Gravity.CENTER_HORIZONTAL);
                        t8.setGravity(Gravity.CENTER_HORIZONTAL);

                        t1.setBackground(getDrawable(R.drawable.table_border));
                        t2.setBackground(getDrawable(R.drawable.table_border));
                        t3.setBackground(getDrawable(R.drawable.table_border));
                        t4.setBackground(getDrawable(R.drawable.table_border));
                        t5.setBackground(getDrawable(R.drawable.table_border));
                        t6.setBackground(getDrawable(R.drawable.table_border));
                        t7.setBackground(getDrawable(R.drawable.table_border));
                        t71.setBackground(getDrawable(R.drawable.table_border));
                        t8.setBackground(getDrawable(R.drawable.table_border));


                        row.addView(t1);
                        row.addView(t2);
                        row.addView(t3);
                        row.addView(t4);
                        row.addView(t5);
                        row.addView(t6);
                        row.addView(t7);
                        row.addView(t71);
                        row.addView(t8);

                        table.addView(row);
                    }



                    float referenceDistance = 10000;




                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            //                             responseProgBar.setVisibility(ProgressBar.GONE);
                            Toast.makeText(SummaryActivity.this, volleyError.toString(), Toast.LENGTH_LONG).show();



                        }
                    }) {
//(final String uid,  final String userName, final String comments, final String likes,  final String retweets, final String cash, final String phone)

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("account", accountId);



                    return params;
                }
            };
//        stringPostRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            stringPostRequest.setRetryPolicy(new DefaultRetryPolicy(10* DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 0));
            queue.add(stringPostRequest);
        }

    }


    public class ClientSSLSocketFactory extends SSLCertificateSocketFactory {
        private  SSLContext sslContext;

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
                        ClientSSLSocketFactory.getDefault
                                (10000 , new SSLSessionCache
                                        (SummaryActivity.this));
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




    private <T> Iterable<T> iterate(final Iterator<T> i){
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return i;
            }
        };
    }

    @Override
    public void onBackPressed(){
//        if(getSupportFragmentManager().getBackStackEntryCount()>0)getSupportFragmentManager().popBackStack();
//        else finish();
        Intent myIntent = new Intent(SummaryActivity.this, MainActivity.class);
        myIntent.putExtra("USER_ID", uides);
        startActivity(myIntent);
        finish();
        return;
    }

}
