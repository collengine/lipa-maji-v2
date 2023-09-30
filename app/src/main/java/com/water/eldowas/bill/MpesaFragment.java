package com.water.eldowas.bill;

import android.net.SSLCertificateSocketFactory;
import android.net.SSLSessionCache;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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


public class MpesaFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RequestQueue queue;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private TextView accountTv;
    private TextView accountBlc;
    private TextView accountDueDate;
    private Button payBillBtn;
    private String account_id;
    private String userUid;
    private  String ammount;
    private EditText amntEdit;
    private ShimmerFrameLayout mShimmerViewContainer;



    public MpesaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MpesaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MpesaFragment newInstance(String param1, String param2) {
        MpesaFragment fragment = new MpesaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {


            userUid = this.getArguments().getString(ARG_PARAM1);
            account_id = this.getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mpesa, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        //amntEdit

        amntEdit = (EditText) view.findViewById(R.id.pesa_account_name);
        accountTv = (TextView)view.findViewById(R.id.account_text);
        accountBlc = (TextView)view.findViewById(R.id.account_balance);
        accountDueDate = (TextView)view.findViewById(R.id.mpesa_due_date);
        payBillBtn= (Button) view.findViewById(R.id.pay_bill_btn);

        mShimmerViewContainer = (ShimmerFrameLayout) view.findViewById(R.id.shimmer_view_container);

        mShimmerViewContainer.startShimmerAnimation();
        payBillBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext() , "Payment NOT YET Activated", Toast.LENGTH_LONG).show();
               /* String phoneNumber = amntEdit.getText().toString();
                if (TextUtils.isEmpty(phoneNumber)) {
                    amntEdit.setError("Cannot be blank");
                } else{


                    ammount=amntEdit.getText().toString();
                    Double amountF= Double.valueOf(ammount);
                    int a = (int) (amountF + 0.5);
                    ammount=String.valueOf(a);
                    Intent intent = new Intent(getContext(), MpesaActivity.class);
                    intent.putExtra("USER_ID", userUid);
                    intent.putExtra("MONTHBILL", ammount);

                    startActivity(intent);

                }*/}
        });


        getstations(account_id);

    }

    private void getstations(final String accountId) {
        {

            MpesaFragment.ClientSSLSocketFactory cssl = new MpesaFragment.ClientSSLSocketFactory(30000);
//TODO verify the socket
            queue = Volley.newRequestQueue(getContext(), new HurlStack(null,
                    cssl.getSocketFactory()));



            StringRequest stringPostRequest = new StringRequest(Request.Method.POST, Helper.PATH_TO_BILL_FETCH, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //            responseProgBar.setVisibility(ProgressBar.GONE);

                    mShimmerViewContainer.stopShimmerAnimation();
                    payBillBtn.setVisibility(View.VISIBLE);


                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("response");
                        JSONObject json = jsonArray.getJSONObject(0);
                        String reciept = json.getString("details");
                        String bill = json.getString("bill");

                        Gson gson = new Gson();
                        BillReciept billReciept = gson.fromJson(reciept, BillReciept.class);
                        BillUpdate billUpdate = gson.fromJson(bill, BillUpdate.class);


                        accountTv.setText("Acc No: "  + billReciept.getAccountNo());
                        amntEdit.setText(billUpdate.getAccBalance());
                        accountDueDate.setText(billReciept.getDueDate());



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                    float referenceDistance = 10000;




                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            //                             responseProgBar.setVisibility(ProgressBar.GONE);
                            Toast.makeText(getContext(), volleyError.toString(), Toast.LENGTH_LONG).show();



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
