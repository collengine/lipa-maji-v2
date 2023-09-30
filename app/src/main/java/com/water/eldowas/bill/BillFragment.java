package com.water.eldowas.bill;

import android.net.SSLCertificateSocketFactory;
import android.net.SSLSessionCache;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

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
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class BillFragment extends Fragment {



    private String month;
    private String billNumber;
    private String currentReading;
    private String previousReading;
    private String consumptionUnits;
    private String waterBill;
    private String sewerageBill;
    private String meterRent;
    private String billAmount;
    private String monthlyBill;
    private String dateRead;
    private String dueDate;
    private String dateUpdated;


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
    private TextView accNumber;
    private TextView accName;
    private Button proceedBtn;
    private ShimmerFrameLayout mShimmerViewContainer;


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


    private static final String ARG_PARAM1 = "USER_ID";
    private static final String ARG_PARAM2 = "ACCOUNT_NUMBER";

    // TODO: Rename and change types of parameters
    private String userUid;
    private String accountNumber;

    public BillFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BillFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment newInstance(String param1, String param2) {
        BillFragment fragment = new BillFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_bill, container, false);
        if (getArguments() != null) {

            userUid = this.getArguments().getString(ARG_PARAM1);
            account_id = this.getArguments().getString(ARG_PARAM2);
        }

        uid =userUid;






        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        monthTv = (TextView)view.findViewById(R.id.home_bill_month);
        billNumberTv = (TextView)view.findViewById(R.id.home_bill_number);
        currentReadingTv = (TextView)view.findViewById(R.id.home_bill_cur_reading);
        previousReadingTv = (TextView)view.findViewById(R.id.home_bill_prev_reading);
        consumptionUnitsTv = (TextView)view.findViewById(R.id.home_bill_monthly_units);
        waterBillTv = (TextView)view.findViewById(R.id.home_bill_water_bill);
        sewerageBillTv = (TextView)view.findViewById(R.id.home_bill_sewerage_bill);
        meterRentTv = (TextView)view.findViewById(R.id.home_bill_standing_charges);
        billAmountTv = (TextView)view.findViewById(R.id.home_bill_amount);
        dateReadTv = (TextView)view.findViewById(R.id.home_bill_date_read);
        dueDateTv = (TextView)view.findViewById(R.id.home_due_date);
        monthlyBillTv = (TextView)view.findViewById(R.id.home_bill_final);
        dateUpdatedTv  = (TextView)view.findViewById(R.id.home_bill_date_updated);
        summaryAmount = (TextView)view.findViewById(R.id.summary_month_bill);
        summaryPeriod = (TextView)view.findViewById(R.id.summary_month);
        summaryOutstanding = (TextView)view.findViewById(R.id.fragment_bill_outstanding);
        accNumber = (TextView)view.findViewById(R.id.summary_account);
        accName = (TextView)view.findViewById(R.id.summary_name);



        mShimmerViewContainer = (ShimmerFrameLayout) view.findViewById(R.id.shimmer_view_container);

        mShimmerViewContainer.startShimmerAnimation();
        getstations(account_id);

    }



    private void getstations(final String accountId) {
        {

            ClientSSLSocketFactory cssl = new ClientSSLSocketFactory(30000);
//TODO verify the socket
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                queue = Volley.newRequestQueue(getContext(), new HurlStack(null,
                        cssl.getSocketFactory()));
            }


            StringRequest stringPostRequest = new StringRequest(Request.Method.POST, Helper.PATH_TO_BILL_FETCH, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //            responseProgBar.setVisibility(ProgressBar.GONE);
                    mShimmerViewContainer.stopShimmerAnimation();


                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("response");
                        JSONObject json = jsonArray.getJSONObject(0);
                        String reciept = json.getString("details");
                        String bill = json.getString("bill");
                        DecimalFormat twoPlaces = new DecimalFormat("###,###,###.00");








                        Gson gson = new Gson();
                        BillReciept billReciept = gson.fromJson(reciept, BillReciept.class);
                        BillUpdate billUpdate = gson.fromJson(bill, BillUpdate.class);


                        Double outsBalance=Double.valueOf(billUpdate.getAccBalance());

                        String newBal=twoPlaces.format(outsBalance);


                        monthTv.setText(billReciept.getBillingPeriod());
                        billNumberTv.setText(billReciept.getBillNo());
                        currentReadingTv.setText(billReciept.getCurrentReading());
                        previousReadingTv.setText(billReciept.getPreviousReading());
                        consumptionUnitsTv.setText(billReciept.getConsumption());
                        waterBillTv.setText(billReciept.getWaterAmount());
                        sewerageBillTv.setText(billReciept.getSewerAmount());
                        meterRentTv.setText(billReciept.getRentAmount());
                        billAmountTv.setText(billReciept.getCurrentBillAmount());
                        monthlyBillTv.setText(newBal);
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

                        summaryOutstanding.setText(newBal);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }








                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            //                             responseProgBar.setVisibility(ProgressBar.GONE);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                Toast.makeText(getContext(), volleyError.toString(), Toast.LENGTH_LONG).show();
                            }


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
                SSLSocketFactory ssf = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    ssf = ClientSSLSocketFactory.getDefault
                            (10000 , new SSLSessionCache
                                    (getContext()));
                }
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
