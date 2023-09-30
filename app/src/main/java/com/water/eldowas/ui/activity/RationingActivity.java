package com.water.eldowas.ui.activity;

import android.content.Intent;
import android.net.SSLCertificateSocketFactory;
import android.net.SSLSessionCache;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.water.eldowas.R;
import com.water.eldowas.model.Rationing;
import com.water.eldowas.util.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class RationingActivity extends AppCompatActivity {
    private String userUid;
    private ImageButton btnBack;

    private String TAG = "RationingActivity";

    private RequestQueue queue;
    private List<Rationing> rationsList = new ArrayList<>();
    private Rationing rationingObject;
    private TableLayout table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rationing);
        Intent myIntent = getIntent();//mIntent.putExtra("USER_ID", uid);
        userUid = myIntent.getStringExtra("USER_ID");
        table = (TableLayout)findViewById(R.id.tablelayout_rationing);
        btnBack = (ImageButton) findViewById(R.id.rationing_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mIntent2 = new Intent(RationingActivity.this, MainActivity.class);
                mIntent2.putExtra("USER_ID", userUid);
                mIntent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);//Revisit this
                startActivity(mIntent2);
                finish();
            }
        });

        sendNotificationRequestToWerlerver();
    }



    private void sendNotificationRequestToWerlerver(){
       RationingActivity.ClientSSLSocketFactory cssl = new ClientSSLSocketFactory(30000);
        Volley.newRequestQueue(RationingActivity.this, new HurlStack(null, cssl.getSocketFactory()));
        queue = Volley.newRequestQueue(this, new HurlStack(null,
                cssl.getSocketFactory()));





        StringRequest stringPostRequest = new StringRequest(com.android.volley.Request.Method.POST, Helper.PATH_TO_SERVER_RATIONING_INTERRUPTION, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onResponse(String response) {

              try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");



                    for(int i = 0 ; i < jsonArray.length(); i++){
                        JSONObject json = jsonArray.getJSONObject(i);
                        String places = json.getString("places");
                        String days_of_week = json.getString("days_of_week");
                        String time_of_day = json.getString("time_of_day");
                        String descr = json.getString("descr");
                        String date_post = json.getString("date_post");
                        //String places, String day, String time, String description, String datePosted
                        Rationing rationing = new Rationing(places, days_of_week, time_of_day, descr,date_post );
                        rationsList.add(rationing);
                    }

                }catch (JSONException e) {
                    e.printStackTrace();
                }


                for(Rationing rl: rationsList){
                    TableRow row = new TableRow(RationingActivity.this);
                    TableLayout.LayoutParams tableRowParams= new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
                    tableRowParams.setMargins(0, 0, 0, 2);
                    row.setGravity(Gravity.CENTER_HORIZONTAL);
                    row.setLayoutParams(tableRowParams);

                    TextView t1 = new TextView(RationingActivity.this);
                    TextView t2 = new TextView(RationingActivity.this);
                    TextView t3 = new TextView(RationingActivity.this);
                    TextView t4 = new TextView(RationingActivity.this);
                    TextView t5 = new TextView(RationingActivity.this);
//                    TextView t6 = new TextView(RationingActivity.this);
//                    TextView t7 = new TextView(RationingActivity.this);
//                    TextView t8 = new TextView(RationingActivity.this);


                    t1.setText(rl.getPlaces());
                    t2.setText(rl.getDay());
                    t3.setText(rl.getTime());
                    t4.setText(rl.getDescription());
                    t5.setText(rl.getDatePosted());
//                    t6.setText(rl.getPlaces());
//                    t7.setText(rl.getDay());
//                    t8.setText(rl.getTime());


                    t1.setGravity(Gravity.CENTER_HORIZONTAL);
                    t2.setGravity(Gravity.CENTER_HORIZONTAL);
                    t3.setGravity(Gravity.CENTER_HORIZONTAL);
                    t4.setGravity(Gravity.CENTER_HORIZONTAL);
                    t5.setGravity(Gravity.CENTER_HORIZONTAL);
//                    t6.setGravity(Gravity.CENTER_HORIZONTAL);
//                    t7.setGravity(Gravity.CENTER_HORIZONTAL);
//                    t8.setGravity(Gravity.CENTER_HORIZONTAL);


                    t1.setBackground(getDrawable(R.drawable.table_border));
                    t2.setBackground(getDrawable(R.drawable.table_border));
                    t3.setBackground(getDrawable(R.drawable.table_border));
                    t4.setBackground(getDrawable(R.drawable.table_border));
                    t5.setBackground(getDrawable(R.drawable.table_border));
//                    t6.setBackground(getDrawable(R.drawable.table_border));
//                    t7.setBackground(getDrawable(R.drawable.table_border));
//                    t8.setBackground(getDrawable(R.drawable.table_border));



                    row.addView(t1);
                    row.addView(t2);
                    row.addView(t3);
                    row.addView(t4);
                    row.addView(t5);
//                    row.addView(t6);
//                    row.addView(t7);
//                    row.addView(t8);


                    table.addView(row);
                }





            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", "token");
                return params;
            }
        };
        stringPostRequest.setRetryPolicy(new DefaultRetryPolicy(10* DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 0));
        queue.add(stringPostRequest);
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
                        RationingActivity.ClientSSLSocketFactory.getDefault
                                (10000 , new SSLSessionCache
                                        (RationingActivity.this));
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
}
