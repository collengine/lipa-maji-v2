package com.water.eldowas.ui.activity;

import android.content.Intent;
import android.net.SSLCertificateSocketFactory;
import android.net.SSLSessionCache;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.water.eldowas.R;
import com.water.eldowas.util.Helper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class TambuaActivity extends AppCompatActivity {

    private String userUid;

    private EditText pfNrmberET;
    private ImageButton btnBack;
    private Button tambuaBtn;
    private RequestQueue queue;
    private String TAG  = "TambuaActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambua);
        Intent myIntent = getIntent();//mIntent.putExtra("USER_ID", uid);
        userUid = myIntent.getStringExtra("USER_ID");
        tambuaBtn = (Button)findViewById(R.id.tambua_btn);
         btnBack = (ImageButton) findViewById(R.id.tambua_back);
        pfNrmberET = (EditText)findViewById(R.id.pf_number_txt);
         btnBack.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 Intent mIntent2 = new Intent(TambuaActivity.this, MainActivity.class);
                 mIntent2.putExtra("USER_ID", userUid);
                 mIntent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);//Revisit this
                 startActivity(mIntent2);
                 finish();
             }
         });
        tambuaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pfNumber = pfNrmberET.getText().toString();

                if (TextUtils.isEmpty(pfNumber)) {
//            Toast.makeText(getApplicationContext(), "Enter user name!", Toast.LENGTH_SHORT).show();
                    pfNrmberET.setError("Enter PF or ID Number");

                    return;
                }

                sendNotificationRequestToWebServer(pfNumber);

            }
        });
    }
    @Override
    public void onBackPressed(){
//        if(getSupportFragmentManager().getBackStackEntryCount()>0)getSupportFragmentManager().popBackStack();
//        else finish();

            super.onBackPressed();
            return;


    }


    private void sendNotificationRequestToWebServer(final String pfNumber){
        ClientSSLSocketFactory cssl = new ClientSSLSocketFactory(30000);
        Volley.newRequestQueue(TambuaActivity.this, new HurlStack(null, cssl.getSocketFactory()));
        //     queue = Volley.newRequestQueue(getContext().getApplicationContext());
        queue = Volley.newRequestQueue(this, new HurlStack(null,
                cssl.getSocketFactory()));

        StringRequest stringPostRequest = new StringRequest(com.android.volley.Request.Method.POST, Helper.PATH_TO_SERVER_RATIONING_TAMBUA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(response.equalsIgnoreCase("null")){
                    Intent intent = new Intent(TambuaActivity.this, PostVerifyActivity.class);
                    intent.putExtra("VERIFICATION", "FALSE");
                    startActivity(intent);
                    finish();
                }
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();



                try {
                    JSONObject jsonObj = new JSONObject(response);
                    JSONObject json = jsonObj.getJSONObject("response");
                    String firstName = json.getString("firstName");
                    String lastName = json.getString("firstName");
                    String position = json.getString("position");
                    String name = firstName + " "  + lastName;


                    Intent intent = new Intent(TambuaActivity.this, PostVerifyActivity.class);
                    intent.putExtra("VERIFICATION", "TRUE");
                    intent.putExtra("NAME", name);
                    intent.putExtra("POSITION", position);

                    startActivity(intent);
                    finish();


                } catch (JSONException e) {

                }

                if (null == "") {
                    Toast.makeText(TambuaActivity.this, "REQUEST NOT been recieved by the server", Toast.LENGTH_LONG).show();
                    // mySharedPreference.saveNotificationSubscription(false);

                } else {
                    //       Toast.makeText(ChatActivity.this, "REQUEST  successfully send to server", Toast.LENGTH_LONG).show();
                    //   mySharedPreference.saveNotificationSubscription(true);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //      Toast.makeText(ChatActivity.this, volleyError.toString(), Toast.LENGTH_LONG).show();


                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("pfNumber", pfNumber);
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
                        ClientSSLSocketFactory.getDefault
                                (10000 , new SSLSessionCache
                                        (TambuaActivity.this));
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
