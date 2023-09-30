package com.water.eldowas.ui.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.water.eldowas.R;
import com.water.eldowas.api.ApiClient;
import com.water.eldowas.api.model.AccessToken;
import com.water.eldowas.api.model.STKPush;
import com.water.eldowas.util.NotificationUtils;
import com.water.eldowas.util.SharedPrefsUtil;
import com.water.eldowas.util.Utils;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.ArrayList;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.water.eldowas.util.AppConstants.BUSINESS_SHORT_CODE;
import static com.water.eldowas.util.AppConstants.CALLBACKURL;
import static com.water.eldowas.util.AppConstants.PARTYB;
import static com.water.eldowas.util.AppConstants.PASSKEY;
import static com.water.eldowas.util.AppConstants.PUSH_NOTIFICATION;
import static com.water.eldowas.util.AppConstants.REGISTRATION_COMPLETE;
import static com.water.eldowas.util.AppConstants.TOPIC_GLOBAL;
import static com.water.eldowas.util.AppConstants.TRANSACTION_TYPE;

public class MpesaActivity extends AppCompatActivity {





    private String mFireBaseRegId;
    private String uid;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private ProgressDialog mProgressDialog;
    private SharedPrefsUtil mSharedPrefsUtil;
    private ApiClient mApiClient;
    private ArrayList<Integer> mPriceArrayList = new ArrayList<>();
    private String monthBill;
    private boolean doubleBackToExitPressedOnce = false;
    private static final int VERIFY_NUMBER = 50;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpesa);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        uid = intent.getStringExtra("USER_ID");
        monthBill = intent.getStringExtra("MONTHBILL");

        mProgressDialog = new ProgressDialog(this);
        mSharedPrefsUtil = new SharedPrefsUtil(this);
        mApiClient = new ApiClient();
        mApiClient.setIsDebug(true); //Set True to enable logging, false to disable.

        getAccessToken();



        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_GLOBAL);
                    getFirebaseRegId();

                } else if (intent.getAction().equals(PUSH_NOTIFICATION)) {
                    String message = intent.getStringExtra("message");

                    NotificationUtils.createNotification(getApplicationContext(), message);
                    showResultDialog(message);
                }
            }
        };

        getFirebaseRegId();
       // showCheckoutDialog();
    }








    @Override
    protected void onDestroy() {
        super.onDestroy();

        // unregisterReceiver(mRegistrationBroadcastReceiver);
    }


    public void getAccessToken() {
        mApiClient.setGetAccessToken(true);
        mApiClient.mpesaService().getAccessToken().enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(@NonNull Call<AccessToken> call, @NonNull Response<AccessToken> response) {

                if (response.isSuccessful()) {
                   mApiClient.setAuthToken(response.body().accessToken);
                    showCheckoutDialog();
                }
            }

            @Override
            public void onFailure(@NonNull Call<AccessToken> call, @NonNull Throwable t) {
                Toast.makeText(MpesaActivity.this, "the token is denied: " + t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void showCheckoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.checkout_dialog_title, Integer.parseInt(monthBill)));

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_PHONE);
        input.setHint(getString(R.string.hint_phone_number));
        builder.setView(input);

        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
            String phone_number = input.getText().toString();
            String phonenumber = phone_number;
            if (phone_number.length() < 11 & phone_number.startsWith("0")) {
                phonenumber = phone_number.replaceFirst("^0", "+254");

            }
            Intent intent = new Intent(MpesaActivity.this, PhoneVerificationActivity.class);
            intent.putExtra("PHONE_NUMBER", phonenumber);
            startActivityForResult(intent,VERIFY_NUMBER );

           // performSTKPush(phone_number);
        });
        builder.setNegativeButton(getString(R.string.clear_cart), (dialog, which) -> {

            dialog.cancel();
        });

        builder.show();
    }

    public void performSTKPush(String phone_number) {
        mProgressDialog.setMessage(getString(R.string.dialog_message_processing));
        mProgressDialog.setTitle(getString(R.string.title_wait));
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
        String timestamp = Utils.getTimestamp();
        STKPush stkPush = new STKPush(
                BUSINESS_SHORT_CODE,
                Utils.getPassword(BUSINESS_SHORT_CODE, PASSKEY, timestamp),
                timestamp,
                TRANSACTION_TYPE,
                "1",
                Utils.sanitizePhoneNumber(phone_number),
                PARTYB,
                Utils.sanitizePhoneNumber(phone_number),
                CALLBACKURL+uid,
                "test", //The account reference
                "test"  //The transaction description
        );
//mFireBaseRegId
        mApiClient.setGetAccessToken(false);

        mApiClient.mpesaService().sendPush(stkPush).enqueue(new Callback<STKPush>() {
            @Override
            public void onResponse(@NonNull Call<STKPush> call, @NonNull Response<STKPush> response) {
                mProgressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {

                        Timber.d("post submitted to API. %s", response.body());

                    } else {

                        Timber.e("Response %s", response.errorBody().string());
                        Intent intent = new Intent(MpesaActivity.this, MainActivity.class);
                        intent.putExtra("USER_ID", uid);
                        startActivity(intent);

                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(@NonNull Call<STKPush> call, @NonNull Throwable t) {
                mProgressDialog.dismiss();

                Timber.e(t);
            }
        });
    }

    private void getFirebaseRegId() {
        mFireBaseRegId = mSharedPrefsUtil.getFirebaseRegistrationID();
       if (!TextUtils.isEmpty(mFireBaseRegId)) {
            mSharedPrefsUtil.saveFirebaseRegistrationID(mFireBaseRegId);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }



    public int getTotal(ArrayList<Integer> prices) {
        int sum = 0;
        for (int i = 0; i < prices.size(); i++) {
            sum = sum + prices.get(i);
        }

        if (prices.size() == 0) {
            Toast.makeText(MpesaActivity.this, String.valueOf("Total: " + sum), Toast.LENGTH_SHORT).show();
            return 0;
        } else
            return sum;
    }


    public void showResultDialog(String result) {
        Timber.d(result);
        if (!mSharedPrefsUtil.getIsFirstTime()) {
            // run your one time code
            mSharedPrefsUtil.saveIsFirstTime(true);

            new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText(getString(R.string.title_success))
                    .setContentText(getString(R.string.dialog_message_success) + "\n" + result)
                    .setConfirmClickListener(sDialog -> {
                        sDialog.dismissWithAnimation();
                        mSharedPrefsUtil.saveIsFirstTime(false);
                        Intent intent = new Intent(MpesaActivity.this, MainActivity.class);
                        intent.putExtra("USER_ID", uid);
                        startActivity(intent);
                        finish();
                    })
                    .show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("MpesaActivity", "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode + "Expected" + RESULT_OK);

        if (requestCode == VERIFY_NUMBER) {
            //  // //Log.d(TAG, "onActivityResult: requestCode=");
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    String verify = data.getStringExtra("VERIFY");
                    String phoneNumber = data.getStringExtra("PHONE_NUMBER");

                    if(verify.equalsIgnoreCase("verified")){
                      performSTKPush(Utils.sanitizePhoneNumber(phoneNumber).substring(1));
                    }
                    else{
                        Snackbar.make(findViewById(android.R.id.content), verify, Snackbar.LENGTH_SHORT).show();
                        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Phone Number Verification")
                                .setContentText( "Phone Number Not Verified" )
                                .setConfirmClickListener(sDialog -> {
                                    sDialog.dismissWithAnimation();
                                    mSharedPrefsUtil.saveIsFirstTime(false);
                                    Intent intent = new Intent(MpesaActivity.this, MainActivity.class);
                                    intent.putExtra("USER_ID", uid);
                                    startActivity(intent);
                                    finish();
                                })
                                .show();
                    }
                }
            }
        }
    }
    @Override
    public void onBackPressed(){
//        if(getSupportFragmentManager().getBackStackEntryCount()>0)getSupportFragmentManager().popBackStack();
//        else finish();

        if(doubleBackToExitPressedOnce){
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click back again to Exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
        /*
        if

         */
    }
}
