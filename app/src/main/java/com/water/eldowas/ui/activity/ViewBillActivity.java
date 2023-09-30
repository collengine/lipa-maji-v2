package com.water.eldowas.ui.activity;

import android.content.Intent;
import android.net.SSLCertificateSocketFactory;
import android.net.SSLSessionCache;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
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
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.water.eldowas.R;
import com.water.eldowas.model.BillReciept;
import com.water.eldowas.model.BillUpdate;
import com.water.eldowas.util.Helper;
import com.water.eldowas.util.TokenObject;

import org.json.JSONArray;
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

import static com.androidquery.util.AQUtility.getContext;

public class ViewBillActivity extends AppCompatActivity {

    private TextView monthTv;
    private TextView billNumberTv;
    private TextView currentReadingTv;
    private TextView previousReadingTv;
    private TextView consumptionUnitsTv;
    private TextView waterBillTv;
    private TextView sewerageBillTv;
    private TextView meterRentTv;
    private TextView billAmountTv;
    private TextView monthlyBillTv;
    private TextView dateReadTv;
    private TextView dueDateTv;//
    private TextView dateUpdatedTv;
    private Button proceedBtn;


    private String uid;
    private RequestQueue queue;
    private String mpesacoded;
    private String TOKENS = "tokens";
    private String account_id;

    private TokenObject tokenObject;
    private  String monthlyBillx;

    private TextView summaryPeriod;
    private TextView summaryAmount;
    private TextView summaryOutstanding;

    private TextView accNumber;
    private TextView accName;

    // TODO: Rename and change types of parameters
    private String userUid;
    private String accountNumber;
    private ShimmerFrameLayout mShimmerViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bill);

        Intent myIntent = getIntent();//mIntent.putExtra("USER_ID", uid);
        userUid = myIntent.getStringExtra("USER_ID");
        accountNumber = myIntent.getStringExtra("ACCOUNT_NUMBER");

        monthTv = (TextView)findViewById(R.id.home_bill_month);
        billNumberTv = (TextView)findViewById(R.id.home_bill_number);
        currentReadingTv = (TextView)findViewById(R.id.home_bill_cur_reading);
        previousReadingTv = (TextView)findViewById(R.id.home_bill_prev_reading);
        consumptionUnitsTv = (TextView)findViewById(R.id.home_bill_monthly_units);
        waterBillTv = (TextView)findViewById(R.id.home_bill_water_bill);
        sewerageBillTv = (TextView)findViewById(R.id.home_bill_sewerage_bill);
        meterRentTv = (TextView)findViewById(R.id.home_bill_standing_charges);
        billAmountTv = (TextView)findViewById(R.id.home_bill_amount);
        dateReadTv = (TextView)findViewById(R.id.home_bill_date_read);
        dueDateTv = (TextView)findViewById(R.id.home_due_date);
        monthlyBillTv = (TextView)findViewById(R.id.home_bill_final);
        dateUpdatedTv  = (TextView)findViewById(R.id.home_bill_date_updated);
        summaryAmount = (TextView)findViewById(R.id.summary_month_bill);
        summaryPeriod = (TextView)findViewById(R.id.summary_month);
        summaryOutstanding = (TextView)findViewById(R.id.fragment_bill_outstanding);

        accNumber = (TextView)findViewById(R.id.summary_account);
        accName = (TextView)findViewById(R.id.summary_name);

        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);

        mShimmerViewContainer.startShimmerAnimation();

        getstations(accountNumber);
    }




    private void getstations(final String accountId) {
        {

            ViewBillActivity.ClientSSLSocketFactory cssl = new ViewBillActivity.ClientSSLSocketFactory(30000);
//TODO verify the socket
            queue = Volley.newRequestQueue(this, new HurlStack(null,
                    cssl.getSocketFactory()));



            StringRequest stringPostRequest = new StringRequest(Request.Method.POST, Helper.PATH_TO_BILL_FETCH, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //            responseProgBar.setVisibility(ProgressBar.GONE);

                    mShimmerViewContainer.stopShimmerAnimation();
                    //mShimmerViewContainer.setVisibility(View.GONE);




                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("response");
                        JSONObject json = jsonArray.getJSONObject(0);
                        String reciept = json.getString("details");
                        String bill = json.getString("bill");
                        Gson gson = new Gson();
                        BillReciept billReciept = gson.fromJson(reciept, BillReciept.class);
                        BillUpdate billUpdate = gson.fromJson(bill, BillUpdate.class);

                        monthTv.setText(billReciept.getBillingPeriod());
                        billNumberTv.setText(billReciept.getBillNo());
                        currentReadingTv.setText(billReciept.getCurrentReading());
                        previousReadingTv.setText(billReciept.getPreviousReading());
                        consumptionUnitsTv.setText(billReciept.getConsumption());
                        waterBillTv.setText(billReciept.getWaterAmount());
                        sewerageBillTv.setText(billReciept.getSewerAmount());
                        meterRentTv.setText(billReciept.getRentAmount());
                        billAmountTv.setText(billReciept.getCurrentBillAmount());
                        monthlyBillTv.setText(billUpdate.getAccBalance());
                        monthlyBillTv.setVisibility(TextView.VISIBLE);
                        dateReadTv.setText(billReciept.getDateOfReading());
                        dueDateTv.setText(billReciept.getDueDate());//
                        accName.setText(billReciept.getCustomerName());
                        accNumber.setText(billReciept.getAccountNo());
                        dateUpdatedTv.setText("Last Updated " + billUpdate.getLastUpdated());
                        String Billx = billUpdate.getAccBalance();
                        double billy = Double.parseDouble(Billx);
                        int billz = (int)(billy);
                        monthlyBillx = String.valueOf(billz);

                        summaryAmount.setText(billReciept.getCurrentBillAmount());
                        summaryPeriod.setText(billReciept.getBillingPeriod());
                        summaryOutstanding.setText(billUpdate.getAccBalance());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            //                             responseProgBar.setVisibility(ProgressBar.GONE);
                            Toast.makeText(getContext(), volleyError.toString(), Toast.LENGTH_LONG).show();



                        }
                    }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("account", accountId);



                    return params;
                }
            };

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
                SSLSocketFactory ssf = ViewBillActivity.ClientSSLSocketFactory.getDefault
                        (10000 , new SSLSessionCache
                                (getContext()));
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
