package com.water.eldowas.login;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.water.eldowas.R;
import com.water.eldowas.model.NewUserObject;
import com.water.eldowas.model.UserInformation;
import com.water.eldowas.util.Helper;
import com.water.eldowas.util.MySharedPreference;
import com.water.eldowas.util.TokenObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.water.eldowas.login.LoginActivity.USERS_CHILD;

public class PhoneAuthActivity extends AppCompatActivity implements
        View.OnClickListener {

    private static final String TAG = "PhoneAuthActivity";

    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_CODE_SENT = 2;
    private static final int STATE_VERIFY_FAILED = 3;
    private static final int STATE_VERIFY_SUCCESS = 4;
    private static final int STATE_SIGNIN_FAILED = 5;
    private static final int STATE_SIGNIN_SUCCESS = 6;
    private TokenObject tokenObject;
    private String phonNumber ;
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private MySharedPreference mySharedPreference;


    private EditText mPhoneNumberField;
    private TextView timer;

    private ImageButton mStartButton;
    private ProgressBar progressBar;
    private Button mVerifyButton;
    private String uid;
    private EditText verifyCode;
    private String userUID;
    private FirebaseAuth mFirebaseAuth;
    private String verified;
    private DatabaseReference mFirebaseDatabaseReference;
    private RequestQueue queue;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);




      /*  if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
              final int REQUEST_LOCATION = 2;

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Display UI and wait for user interaction
            } else {
                ActivityCompat.requestPermissions(
                        this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_SOURCE);
            }
        }*/

        /*if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){

            }
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE_FOR_SOURCE);

        }*/

        // Restore instance state
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

        // Assign views


        mFirebaseAuth = FirebaseAuth.getInstance();
        mPhoneNumberField = findViewById(R.id.login_user_link);
        mPhoneNumberField.setText("+2547");
        mVerifyButton = findViewById(R.id.login_submit_btn);

        verifyCode = findViewById(R.id.login_code);
        timer = findViewById(R.id.textView1);

        progressBar = (ProgressBar)findViewById(R.id.progressBarFra);

        mStartButton =(ImageButton) findViewById(R.id.next_button);


        // Assign click listeners
        mStartButton.setOnClickListener(this);
        mVerifyButton.setOnClickListener(this);


        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.

                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]

                // [START_EXCLUDE silent]
                // Update the UI and attempt sign in with the phone credential
                // updateUI(STATE_VERIFY_SUCCESS, credential);
                // [END_EXCLUDE]
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
                    mPhoneNumberField.setError("Invalid phone number.");
                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // [START_EXCLUDE]
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                    // [END_EXCLUDE]
                }

                // Show a message and update the UI
                // [START_EXCLUDE]
                //   updateUI(STATE_VERIFY_FAILED);
                // [END_EXCLUDE]
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                // [START_EXCLUDE]
                // Update UI
                //  updateUI(STATE_CODE_SENT);
                // [END_EXCLUDE]
            }
        };
        // [END phone_auth_callbacks]
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //  updateUI(currentUser);

        // [START_EXCLUDE]
        if (mVerificationInProgress && validatePhoneNumber()) {
            startPhoneNumberVerification(mPhoneNumberField.getText().toString());
        }
        // [END_EXCLUDE]
    }
    // [END on_start_check_user]

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }


    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

        mVerificationInProgress = true;
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }

    // [START resend_verification]
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }
    // [END resend_verification]

    // [START sign_in_with_phone]
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            progressBar.setVisibility(ProgressBar.GONE);

                            FirebaseUser user = task.getResult().getUser();
                            String phoneNumber = user.getPhoneNumber();

                            uid = user.getUid();
                            phonNumber = user.getPhoneNumber();
                            String gmail = user.getEmail();
                            String username = user.getDisplayName();

                            checkIfEmailVerified(phonNumber, gmail, username );




                            // [START_EXCLUDE]
                            // updateUI(STATE_SIGNIN_SUCCESS, user);
                            // [END_EXCLUDE]
                        } else {
                            // Sign in failed, display a message and update the UI

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                // [START_EXCLUDE silent]
                                // [END_EXCLUDE]
                            }
                            // [START_EXCLUDE silent]
                            // Update UI
//                            updateUI(STATE_SIGNIN_FAILED);
                            // [END_EXCLUDE]
                        }
                    }
                });
    }
    // [END sign_in_with_phone]





    private void checkIfEmailVerified(String phoneNumber, String gmail, String username){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user.getPhoneNumber() !=null){
            Toast.makeText(PhoneAuthActivity.this, "Succesfully Logged in", Toast.LENGTH_LONG).show();

            userUID = mFirebaseAuth .getCurrentUser().getUid();

            mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();


            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            sendTheRegisteredTokenToWebServer( refreshedToken, userUID);

//            SharedPreferences.Editor editor = getSharedPreferences(idPreference, MODE_PRIVATE).edit();
//            editor.putString(UIDCONSTANT, userUID);
//            editor.commit();



//                            HashMap<String, Object> userGroup= new HashMap<>();
//                            userGroup.put("uid", userUID);



            NewUserObject newUserObject = new NewUserObject(phoneNumber , username, gmail, "on" );
            HashMap<String, Object> userInformation = new HashMap<>();
            userInformation.put("state", "on");
            userInformation.put("uid", userUID);
            userInformation.put("phoneNumber", phoneNumber);
            userInformation.put("gmail", gmail);
            userInformation.put("userName", username);
            FirebaseDatabase.getInstance().getReference().child(USERS_CHILD).child(userUID).updateChildren(userInformation);


            FirebaseDatabase.getInstance().getReference().child(USERS_CHILD).child(userUID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    UserInformation userInformation = dataSnapshot.getValue(UserInformation.class);
                    verified = userInformation.getFirstName();
                    ///  confirm(verified);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });







        }
        else{

            //  inputEmail.setError("Please verifiy your email account");
        }
    }
    private void sendTheRegisteredTokenToWebServer(final String token, final String userUID){
        queue = Volley.newRequestQueue(getApplicationContext());
        mySharedPreference = new MySharedPreference(getApplicationContext());

        StringRequest stringPostRequest = new StringRequest(Request.Method.POST, Helper.PATH_TO_SERVER_IMAGE_UPLOAD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                tokenObject = gson.fromJson(response, TokenObject.class);

                if (null == tokenObject) {
                    Toast.makeText(getApplicationContext(), "Can't send a token to the server", Toast.LENGTH_LONG).show();
                    mySharedPreference.saveNotificationSubscription(false);


                } else {
                    Toast.makeText(getApplicationContext(), "Token successfully send to server", Toast.LENGTH_LONG).show();
                    mySharedPreference.saveNotificationSubscription(true);

                }
                Intent intent = new Intent(PhoneAuthActivity.this, UsernameActivity.class);
                // intent.putExtra("PHONE", "+254710180942"); user.getPhoneNumber()
                intent.putExtra("PHONE", phonNumber);
                intent.putExtra("USER_ID",uid );
                startActivity(intent);
                finish();

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Helper.TOKEN_TO_SERVER, token);
                params.put(Helper.UID_TO_SERVER, userUID);
                return params;
            }
        };
        stringPostRequest.setRetryPolicy(new DefaultRetryPolicy(3* DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 0));
        queue.add(stringPostRequest);
    }

    private void confirm(String verified) {//0783093069

//        if(verified!=null ){
//
//
//
//            Intent myIntent = new Intent(PhoneAuthActivity.this, LoginActivity.class);
//            myIntent.putExtra("USER_ID", userUID);
//            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);//Revisit this
//            startActivity(myIntent);
//
//            finish();
//
//        }else
        {
            Intent myIntent = new Intent(PhoneAuthActivity.this, UsernameActivity.class);
            myIntent.putExtra("USER_ID", userUID);
            myIntent.putExtra("PHONE", phonNumber);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);//Revisit this

            startActivity(myIntent);

            finish();
        }
    }

    private boolean validatePhoneNumber() {
        String phoneNumber = mPhoneNumberField.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            mPhoneNumberField.setError("Invalid phone number.");
            return false;
        }

        return true;
    }

    private static final String FORMAT = "%02d:%02d";
    int seconds , minutes;
    public void reverseTimer(int Seconds,final TextView tv){

        new CountDownTimer(Seconds* 1000+1000, 1000) {

            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                tv.setText("Please Wait..... : " + String.format("%02d", minutes)
                        + ":" + String.format("%02d", seconds));
            }

            public void onFinish() {
                tv.setText("Delay Sending Code");
                verifyCode.setVisibility(View.GONE);
                mVerifyButton.setVisibility(View.GONE);
                mPhoneNumberField.setEnabled(true);
            }
        }.start();

   /*     verifyCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View arg0, boolean hasfocus) {
                if (hasfocus) {
                    tv.setVisibility(View.GONE);



                }
            }
        });*/
    }

    private void enableViews(View... views) {
        for (View v : views) {
            v.setEnabled(true);
        }
    }

    private void disableViews(View... views) {
        for (View v : views) {
            v.setEnabled(false);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next_button:

                ConnectivityManager ConnectionManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo=ConnectionManager.getActiveNetworkInfo();
                if(networkInfo != null && networkInfo.isConnected()==true )
                {




                    reverseTimer(300,timer);
                    progressBar.setVisibility(ProgressBar.GONE);
                    mStartButton.setEnabled(false);
                    timer.setVisibility(View.VISIBLE);
                    verifyCode.setVisibility(View.VISIBLE);
                    mVerifyButton.setVisibility(View.VISIBLE);
                    mPhoneNumberField.setEnabled(false);



                    if (!validatePhoneNumber()) {
                        return;
                    }

//                Intent intent = new Intent(PhoneAuthActivity.this, FetchAccountsActivity.class);
//                intent.putExtra("PHONE", mPhoneNumberField.getText().toString());
//                intent.putExtra("USER_ID",uid );
//                startActivity(intent);
//                finish();


                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mPhoneNumberField.getWindowToken(), 0);
                    startPhoneNumberVerification(mPhoneNumberField.getText().toString());
                    // progressBar.setVisibility(ProgressBar.VISIBLE);

                    Toast.makeText(PhoneAuthActivity.this, "Please wait while phone is verified", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(PhoneAuthActivity.this, "No Internet connection", Toast.LENGTH_LONG).show();

                }



                break;
            case R.id.login_submit_btn:
                String code = verifyCode.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    verifyCode.setError("Cannot be empty.");
                    return;
                }

                verifyPhoneNumberWithCode(mVerificationId, code);
                break;


        }
    }
}