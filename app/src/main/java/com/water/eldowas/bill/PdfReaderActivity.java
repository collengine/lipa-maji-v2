package com.water.eldowas.bill;


import android.content.ContextWrapper;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;


import android.app.DownloadManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import android.content.Intent;
import android.net.SSLCertificateSocketFactory;
import android.net.SSLSessionCache;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.*;

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
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

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

import android.widget.Button;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
//http://62.24.127.90/ebill/print.php?period=Jul.%202018&usercode=3068560-001

public class PdfReaderActivity extends AppCompatActivity {
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        Intent intent = getIntent();


        usercode = intent.getStringExtra(ACCOUNT_NUMBER);
        uid = intent.getStringExtra(USER_ID);
        period = intent.getStringExtra(PERIOD);
        getstations(usercode);

        //    downloadPb = (ProgressBar)findViewById(R.id.download_progrez_bar);

        pdfView = (PDFView)findViewById(R.id.pdf_viewer);
        progressBar = (ProgressBar) findViewById(R.id.download_progrez_bar);
//        pdfView.fromAsset("eldowas_bill.pdf").load();

        destinationDir = new File(Environment.getExternalStorageDirectory() ,  getPackageName());
        if(!destinationDir.exists()){
            destinationDir.mkdir();
        }
        webView = (WebView)findViewById(R.id.webView);

        webView.setWebViewClient(new MyClient());
        webView.setWebChromeClient(new GoogleClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.clearCache(true);
        webView.clearHistory();

        webView.getSettings().setJavaScriptEnabled(true);

        //user interface


        ContextWrapper wrapper;
        wrapper = new ContextWrapper(getApplicationContext());

        file = wrapper.getDir( "Bills", MODE_APPEND );
        file = new File(file, usercode +"-"+ period  +".pdf");




        webView.setDownloadListener(new DownloadListener()
        {

            @Override


            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimeType,
                                        long contentLength) {


                Uri source = Uri.parse(period);
                DownloadManager.Request request = new DownloadManager.Request(
                        Uri.parse(url));


                request.setMimeType(mimeType);


                String cookies = CookieManager.getInstance().getCookie(url);


                request.addRequestHeader("cookie", cookies);
                request.addRequestHeader("cookie", cookies);


                request.addRequestHeader("User-Agent", userAgent);


                request.setDescription("Downloading Bill...");



                // request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimeType));
                path =  usercode+"( " + period + "- Bill)";
                request.setTitle(path);


                //  String filePath = getApplicationContext().getDir("Images", MODE_PRIVATE)+ "/" + "UniquexxyyFileName" +".jpg";



                request.allowScanningByMediaScanner();


                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                //           request.setDestinationUri(Uri.fromFile(file));
//                request.setDestinationInExternalFilesDir(PdfReaderActivity.this,
//                Environment.DIRECTORY_DOWNLOADS,usercode+"-"+period + ".pdf");

                request.setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_DOWNLOADS,usercode+"-"+period + ".pdf");
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);

                //  downloadPb.setVisibility(View.GONE);
                webView.setVisibility(WebView.GONE);

                Toast.makeText(getApplicationContext(), "Downloaded",
                        Toast.LENGTH_LONG).show();

                new SweetAlertDialog(PdfReaderActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Download Status")
                        .setContentText( period + " Bill Downloaded Succesfully")
                        .setConfirmClickListener(sDialog -> {
                            sDialog.dismissWithAnimation();

                        })
                        .show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        finish();
                    }
                }, 3000);

//                File files = new File(Environment.getExternalStorageDirectory()+ "/Download/"+ usercode +"-Sep. 2018.pdf");
//                pdfView.fromFile(files).load();
            }



        });


        //    this.finish();

    }

    class MyClient extends WebViewClient
    {

        @Override
        public void onPageStarted(WebView view,String url,Bitmap favicon){
            super.onPageStarted(view,url,favicon);

        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view,String Url)
        {
            view.loadUrl(Url);
            return true;

        }
        @Override
        public void onPageFinished(WebView view,String url)
        {
            super.onPageFinished(view,url);

        }
    }
    class GoogleClient extends WebChromeClient
    {
        @Override
        public void onProgressChanged(WebView view,int newProgress)
        {
            super.onProgressChanged(view,newProgress);

        }
    }
    @Override
    public void onBackPressed() {
        if (webView.canGoBack())
            webView.goBack();
        else
            super.onBackPressed();

    }










    private void getstations(final String accountId) {
        {
            //Log.e("CheckOut", "This respose || i am on kabisa");

            PdfReaderActivity.ClientSSLSocketFactory cssl = new PdfReaderActivity.ClientSSLSocketFactory(30000);
//TODO verify the socket
            queue = Volley.newRequestQueue(PdfReaderActivity.this, new HurlStack(null,
                    cssl.getSocketFactory()));



            StringRequest stringPostRequest = new StringRequest(Request.Method.POST, Helper.PATH_TO_BILL_FETCH, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //            responseProgBar.setVisibility(ProgressBar.GONE);

                    Log.e("CheckOut", "This respose ||" + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("response");
                        JSONObject json = jsonArray.getJSONObject(0);
                        String reciept = json.getString("details");
                        String bill = json.getString("bill");
                        Log.e("MonthlyBill", "the bill " +reciept  );
                        Log.e("MonthlyBill", "the bill " + bill );
                        Gson gson = new Gson();
                        BillReciept billReciept = gson.fromJson(reciept, BillReciept.class);
                        BillUpdate billUpdate = gson.fromJson(bill, BillUpdate.class);
                        Log.e("MonthlyBill", "the bill " + billReciept.toString() );
                        Log.e("MonthlyBill", "the bill " + billUpdate.toString() );
                        Log.e("MonthlyBill", "Statement Period " + billReciept.getBillingPeriod().toString() );
                        Log.e("MonthlyBill", "Account " + usercode );


                      /*  accountNo.setText(billReciept.getAccountNo().toString());
                        outBalance.setText(billUpdate.getAccBalance().toString());
                        billPeriod.setText(billReciept.getBillingPeriod().toString());*/



                        myperiod=billReciept.getBillingPeriod().toString();

                        period = billReciept.getBillingPeriod();
                        //     new RetrievePdfStream().execute(URL + "period="+ "Jul. 2018"+"usercode"+usercode);

                        webView.loadUrl("http://62.24.127.90/ebill/print.php?period="+myperiod+"&usercode="+usercode);




                    } catch (JSONException e) {
                        e.printStackTrace();
                    }








                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            //                             responseProgBar.setVisibility(ProgressBar.GONE);
                            Toast.makeText(PdfReaderActivity.this, volleyError.toString(), Toast.LENGTH_LONG).show();



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
                                (PdfReaderActivity.this));
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


