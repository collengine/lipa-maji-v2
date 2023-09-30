package com.water.eldowas.bill;

import android.content.Intent;
import android.net.SSLCertificateSocketFactory;
import android.net.SSLSessionCache;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
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
import com.github.barteksc.pdfviewer.PDFView;
import com.google.gson.Gson;
import com.water.eldowas.R;
import com.water.eldowas.model.BillReciept;
import com.water.eldowas.model.BillUpdate;
import com.water.eldowas.util.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
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

public class Main2Activity extends AppCompatActivity {

    private static final String USER_ID = "USER_ID";
    private static final String ACCOUNT_NUMBER = "ACCOUNT_NUMBER";
    private static final String PERIOD= "PERIOD";
    private PDFView pdfView;
    private String period;
    private String usercode;
    private String URL = "http://62.24.127.90/ebill/print.php?";
    private String uid;
    private String accountNumber;
    private String myperiod;
    private RequestQueue queue;
    private WebView webView;
    private Button btnDownload;

    public TextView maiTxt;
    public TextView accountNo;
    public TextView outBalance;
    public TextView billPeriod;
    private ProgressBar downloadPb;
    private  File destinationDir;
    private  ProgressBar progressBar;

    private  File file;
    private String path;

    WebView webview;
    ProgressBar progressbar;
    private static final String HTML_FORMAT = "<html><body style=\"text-align: center; background-color: black; vertical-align: center;\"><img src = \"%s\" /></body></html>";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent intent = getIntent();


        usercode = intent.getStringExtra(ACCOUNT_NUMBER);
        uid = intent.getStringExtra(USER_ID);
        period = intent.getStringExtra(PERIOD);
        getstations(usercode);




    }




    private void getstations(final String accountId) {
        {


            Main2Activity.ClientSSLSocketFactory cssl = new Main2Activity.ClientSSLSocketFactory(30000);
//TODO verify the socket
            queue = Volley.newRequestQueue(Main2Activity.this, new HurlStack(null,
                    cssl.getSocketFactory()));



            StringRequest stringPostRequest = new StringRequest(Request.Method.POST, Helper.PATH_TO_BILL_FETCH, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //            responseProgBar.setVisibility(ProgressBar.GONE);


                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("response");
                        JSONObject json = jsonArray.getJSONObject(0);
                        String reciept = json.getString("details");
                        String bill = json.getString("bill");

                        Gson gson = new Gson();
                        BillReciept billReciept = gson.fromJson(reciept, BillReciept.class);
                        BillUpdate billUpdate = gson.fromJson(bill, BillUpdate.class);


                      /*  accountNo.setText(billReciept.getAccountNo().toString());
                        outBalance.setText(billUpdate.getAccBalance().toString());
                        billPeriod.setText(billReciept.getBillingPeriod().toString());*/



                        myperiod=billReciept.getBillingPeriod().toString();

                        period = billReciept.getBillingPeriod();
                        //     new RetrievePdfStream().execute(URL + "period="+ "Jul. 2018"+"usercode"+usercode);

                        //webView.loadUrl("http://62.24.127.90/ebill/print.php?period="+myperiod+"&usercode="+usercode);

                        webview = (WebView)findViewById(R.id.webview);
                        progressbar = (ProgressBar) findViewById(R.id.progressbar);
                        webview.getSettings().setJavaScriptEnabled(true);
                        String filename ="http://62.24.127.90/ebill/print.php?period=Apr.%202019&usercode=3068554-001";
                        webview.loadUrl("http://docs.google.com/gview?embedded=true&url=" + filename);
                       // webview.loadDataWithBaseURL("", html, "text/html", "UTF-8", "");

                        Log.e("Main2Activity", "The Link is: " + filename);

                        webview.setWebViewClient(new WebViewClient() {

                            public void onPageFinished(WebView view, String url) {
                                // do your stuff here
                                progressbar.setVisibility(View.GONE);
                            }
                        });




                    } catch (JSONException e) {
                        e.printStackTrace();
                    }








                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            //                             responseProgBar.setVisibility(ProgressBar.GONE);
                            Toast.makeText(Main2Activity.this, volleyError.toString(), Toast.LENGTH_LONG).show();



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
                SSLSocketFactory ssf = BillFragment.ClientSSLSocketFactory.getDefault
                        (10000 , new SSLSessionCache
                                (Main2Activity.this));
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
