package com.water.eldowas.bill;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.SSLCertificateSocketFactory;
import android.net.SSLSessionCache;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;
import com.water.eldowas.R;
import com.water.eldowas.model.BillReciept;
import com.water.eldowas.model.BillSummary;
import com.water.eldowas.model.BillUpdate;
import com.water.eldowas.ui.activity.SummaryActivity;
import com.water.eldowas.ui.adapter.BillSummaryAdapter;
import com.water.eldowas.util.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.DecimalFormat;
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

public class StatementFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "USER_ID";
    private static final String ARG_PARAM2 = "ACCOUNT_NUMBER";



    // TODO: Rename and change types of parameters
    private String userUid;
    private String accountNumber;
    private String meterNo;
    private String customerName;




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
    private Calendar cal1 ;
    private Calendar cal2 ;
    private DatePickerDialog datePickerDialog1;
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
    private TextView accBalz,accNumber,accDue, accName, accMeterNumber,payment;
    private ShimmerFrameLayout mShimmerViewContainer;
    private TextView lastpayment,genBalance;
    private  Button btnNext;



    public StatementFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatementFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatementFragment newInstance(String param1, String param2) {
        StatementFragment fragment = new StatementFragment();
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
            userUid = getArguments().getString(ARG_PARAM1);
            accountNumber = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_statement, container, false);


        if (getArguments() != null) {
            //fragment_bill_payment

            userUid = this.getArguments().getString(ARG_PARAM1);
            accountNumber = this.getArguments().getString(ARG_PARAM2);

           // tabletv711 payments
          //  tabletv81 balance

            accNumber = (TextView)view.findViewById(R.id.summary_account_statement);
            accName = (TextView)view.findViewById(R.id.summary_month_bill);
            accMeterNumber = (TextView)view.findViewById(R.id.fragment_bill_outstanding);
            accBalz = (TextView)view.findViewById(R.id.fragment_outstanding);
            accDue = (TextView)view.findViewById(R.id.fragment_last_updated);
            lastpayment = (TextView)view.findViewById(R.id.tabletv711);
            genBalance = (TextView)view.findViewById(R.id.tabletv81);
            btnNext = (Button)view.findViewById(R.id.btnPrint);



           // payment = (TextView)view.findViewById(R.id.fragment_bill_payment);

            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(),PdfPrintStatementActivity.class);
                    intent.putExtra(ARG_PARAM1, userUid);
                    intent.putExtra(ARG_PARAM2, accountNumber);
                    intent.putExtra("customerName", accName.getText());
                    intent.putExtra("meterNo", accMeterNumber.getText());
                    startActivity(intent);
                }
            });



        }
        mShimmerViewContainer = (ShimmerFrameLayout) view.findViewById(R.id.shimmer_view_container);
        mShimmerViewContainer.startShimmerAnimation();






        return view;
    }





    private Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);

        return b;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        table = (TableLayout) view.findViewById(R.id.tablelayout1);

        getstationsNEW(accountNumber);
        getstations(accountNumber);


    }

    private void getstationsNEW(final String accountId) {
        {

            StatementFragment.ClientSSLSocketFactory cssl = new StatementFragment.ClientSSLSocketFactory(30000);
//TODO verify the socket
            queue = Volley.newRequestQueue(getContext(), new HurlStack(null,
                    cssl.getSocketFactory()));



            StringRequest stringPostRequest = new StringRequest(Request.Method.POST, Helper.PATH_TO_BILL_FETCH, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //            responseProgBar.setVisibility(ProgressBar.GONE);

                    mShimmerViewContainer.stopShimmerAnimation();
                    btnNext.setVisibility(View.VISIBLE);


                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("response");
                        JSONObject json = jsonArray.getJSONObject(0);
                        String reciept = json.getString("details");
                        String bill = json.getString("bill");

                        Gson gson = new Gson();
                        BillReciept billReciept = gson.fromJson(reciept, BillReciept.class);
                        BillUpdate billUpdate = gson.fromJson(bill, BillUpdate.class);

                        Double bigBalance=Double.valueOf(billReciept.getAccBalance());
                        Double billBalance=Double.valueOf(billUpdate.getAccBalance());
                        Double newbalance=billBalance - bigBalance;

                        DecimalFormat twoPlaces = new DecimalFormat("###,###,###.00");


                        String newBal=twoPlaces.format(newbalance);;


                        //String moneyString = formatter.format(money);


                        accNumber.setText(billReciept.getAccountNo());

                        //+"\n Name  :"+billReciept.getCustomerName()+"\n Meter No :"+billReciept.getMeterNo());//
                        accName.setText(billReciept.getCustomerName());
                       accMeterNumber.setText(billReciept.getMeterNo());

                        accDue.setText( billUpdate.getLastUpdated());
                        lastpayment.setText(newBal);
                        genBalance.setText( twoPlaces.format(billBalance));

                        accBalz.setText(twoPlaces.format(billBalance));





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


    private void getstations(final String accountId) {
        {

            ClientSSLSocketFactory cssl = new ClientSSLSocketFactory(30000);

            queue = Volley.newRequestQueue(getContext(), new HurlStack(null,
                    cssl.getSocketFactory()));



            StringRequest stringPostRequest = new StringRequest(Request.Method.POST, Helper.PATH_TO_SERVER_METRO, new Response.Listener<String>() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onResponse(String response) {
                    //            responseProgBar.setVisibility(ProgressBar.GONE);


                    try {
                        Gson gson = new Gson();
                        BillSummary[] weather = gson.fromJson(response, BillSummary[].class);
                        BillSummaryList = Arrays.asList(weather);
                    }catch (Exception e){
                        Toast.makeText(getContext(), "Check your Internet Connection", Toast.LENGTH_LONG).show();
                    }


                    //    billSummaryAdapter.notifyDataSetChanged();


                    for(BillSummary bs: BillSummaryList){



                        TableRow row = new TableRow(getContext());
                        TableLayout.LayoutParams tableRowParams= new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
                        tableRowParams.setMargins(0, 0, 0, 2);
                        row.setGravity(Gravity.CENTER_HORIZONTAL);
                        row.setLayoutParams(tableRowParams);

                        TextView t1 = new TextView(getContext());
                        TextView t2 = new TextView(getContext());
                        TextView t3 = new TextView(getContext());
                        TextView t4 = new TextView(getContext());
                        TextView t5 = new TextView(getContext());
                        TextView t6 = new TextView(getContext());
                        TextView t7 = new TextView(getContext());
                        TextView t71 = new TextView(getContext());
                        TextView t8 = new TextView(getContext());



                        t1.setText(bs.getBillNo());
                        t2.setText(bs.getBillingPeriod());
                        t3.setText(bs.getPreviousReading());
                        t4.setText(bs.getCurrentReading());
                        t5.setText(bs.getConsumption());
                        t6.setText(bs.getBillType());
                        t7.setText(bs.getCurrentBillAmount());
                        t71.setText(bs.getPayment());
                        t8.setText(bs.getAccBalance());


                        t1.setGravity(Gravity.CENTER_HORIZONTAL);
                        t2.setGravity(Gravity.CENTER_HORIZONTAL);
                        t3.setGravity(Gravity.CENTER_HORIZONTAL);
                        t4.setGravity(Gravity.CENTER_HORIZONTAL);
                        t5.setGravity(Gravity.CENTER_HORIZONTAL);
                        t6.setGravity(Gravity.CENTER_HORIZONTAL);
                        t7.setGravity(Gravity.CENTER_HORIZONTAL);
                        t71.setGravity(Gravity.CENTER_HORIZONTAL);
                        t8.setGravity(Gravity.CENTER_HORIZONTAL);


                        t1.setBackground(getActivity().getDrawable(R.drawable.table_border));
                        t2.setBackground(getActivity().getDrawable(R.drawable.table_border));
                        t3.setBackground(getActivity().getDrawable(R.drawable.table_border));
                        t4.setBackground(getActivity().getDrawable(R.drawable.table_border));
                        t5.setBackground(getActivity().getDrawable(R.drawable.table_border));
                        t6.setBackground(getActivity().getDrawable(R.drawable.table_border));
                        t7.setBackground(getActivity().getDrawable(R.drawable.table_border));
                        t71.setBackground(getActivity().getDrawable(R.drawable.table_border));
                        t8.setBackground(getActivity().getDrawable(R.drawable.table_border));



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
                SSLSocketFactory ssf =
                        SummaryActivity.ClientSSLSocketFactory.getDefault
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




    private <T> Iterable<T> iterate(final Iterator<T> i){
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return i;
            }
        };
    }



}
