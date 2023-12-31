package com.water.eldowas.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import java.util.concurrent.TimeUnit;
import com.water.eldowas.R;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

public class PhoneVerificationActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "PhoneAuthActivity";
    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";
    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_CODE_SENT = 2;
    private static final int STATE_VERIFY_FAILED = 3;
    private static final int STATE_VERIFY_SUCCESS = 4;
    private static final int STATE_SIGNIN_FAILED = 5;
    private static final int STATE_SIGNIN_SUCCESS = 6;
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]
    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private ViewGroup mPhoneNumberViews;
    private ViewGroup mSignedInViews;
    private TextView mStatusText;
    private TextView mDetailText;
    private EditText mPhoneNumberField;
    private EditText mVerificationField;
    private Button mStartButton;
    private Button mVerifyButton;
    private Button mResendButton;
    private Button mSignOutButton;
    private String phoneNumber;
    private SweetAlertDialog sweetAlertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);
        Intent myIntent2 = getIntent();
        phoneNumber = myIntent2.getStringExtra("PHONE_NUMBER");


        if (savedInstanceState != null) {

            onRestoreInstanceState(savedInstanceState);

        }
         new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("Verifying Phone")
                .setContentText("Please wait as your phone number is verified")
                .show();

        mPhoneNumberViews = findViewById(R.id.phone_auth_fields);

        mSignedInViews = findViewById(R.id.signed_in_buttons);


        mStatusText = findViewById(R.id.status);

        mDetailText = findViewById(R.id.detail);


        mPhoneNumberField = findViewById(R.id.field_phone_number);

        mVerificationField = findViewById(R.id.field_verification_code);


        mStartButton = findViewById(R.id.button_start_verification);

        mVerifyButton = findViewById(R.id.button_verify_phone);

        mResendButton = findViewById(R.id.button_resend);

        mSignOutButton = findViewById(R.id.sign_out_button);


        mStartButton.setOnClickListener(this);

        mVerifyButton.setOnClickListener(this);

        mResendButton.setOnClickListener(this);

        mSignOutButton.setOnClickListener(this);


        // [START initialize_auth]

        mAuth = FirebaseAuth.getInstance();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                mVerificationInProgress = false;
                Intent datum = new Intent();

                datum.putExtra("PHONE_NUMBER", phoneNumber);
                datum.putExtra("VERIFY", "verified");

                setResult(RESULT_OK, datum);
                finish();

//                updateUI(STATE_VERIFY_SUCCESS, phoneAuthCredential);
//                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                mVerificationInProgress = false;

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    mPhoneNumberField.setError("Invalid phone number.");

                }else if (e instanceof FirebaseTooManyRequestsException) {
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.", Snackbar.LENGTH_SHORT).show();

                }
                updateUI(STATE_VERIFY_FAILED);
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
              mVerificationId = verificationId;
                mResendToken = token;
                updateUI(STATE_CODE_SENT);
            }

        };


        startPhoneNumberVerification(phoneNumber);

    }

    @Override
    public void onStart() {
        super.onStart();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);

        if (mVerificationInProgress && validatePhoneNumber()) {
            //startPhoneNumberVerification(mPhoneNumberField.getText().toString());
            startPhoneNumberVerification(phoneNumber);
        }
    }

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
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 60, TimeUnit.SECONDS, this, mCallbacks );
        mVerificationInProgress = true;
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void resendVerificationCode(String phoneNumber,PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 60, TimeUnit.SECONDS, this, mCallbacks, token );
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                   Intent datum = new Intent();

                    datum.putExtra("PHONE_NUMBER", phoneNumber);
                    datum.putExtra("VERIFY", "verified");

                    setResult(RESULT_OK, datum);
                    finish();
//                    FirebaseUser user = task.getResult().getUser();
                    //updateUI(STATE_SIGNIN_SUCCESS, user);
                }else {
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        mVerificationField.setError("Invalid code.");
                        Intent datum = new Intent();

                        datum.putExtra("PHONE_NUMBER", phoneNumber);
                        datum.putExtra("VERIFY", "Invalid code.");

                        setResult(RESULT_OK, datum);
                        finish();
                    }
                    updateUI(STATE_SIGNIN_FAILED);
                }
            }
        });
    }

    public void signOut() {
        mAuth.signOut();
        updateUI(STATE_INITIALIZED);
    }
    private void updateUI(int uiState) {
        updateUI(uiState, mAuth.getCurrentUser(), null);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            updateUI(STATE_SIGNIN_SUCCESS, user);
        }else {
            updateUI(STATE_INITIALIZED);
        }
    }
    private void updateUI(int uiState, FirebaseUser user) {
        updateUI(uiState, user, null);
    }
    private void updateUI(int uiState, PhoneAuthCredential cred) {
        updateUI(uiState, null, cred);
    }

    private void updateUI(int uiState, FirebaseUser user, PhoneAuthCredential cred) {
        switch (uiState) {
            case STATE_INITIALIZED:
                enableViews(mStartButton, mPhoneNumberField);
                disableViews(mVerifyButton, mResendButton, mVerificationField);
                mDetailText.setText(null);
                break;
            case STATE_CODE_SENT:
                enableViews(mVerifyButton, mResendButton, mPhoneNumberField, mVerificationField);
                disableViews(mStartButton);
                mDetailText.setText("status_code_sent");
                break;
            case STATE_VERIFY_FAILED:
                enableViews(mStartButton, mVerifyButton, mResendButton, mPhoneNumberField,mVerificationField);
                mDetailText.setText("status_verification_failed");
                break;
            case STATE_VERIFY_SUCCESS:
                disableViews(mStartButton, mVerifyButton, mResendButton, mPhoneNumberField,mVerificationField);
                mDetailText.setText("status_verification_succeeded");
                if (cred != null) {
                    if (cred.getSmsCode() != null) {
                        mVerificationField.setText(cred.getSmsCode());
                    }else {
                        mVerificationField.setText("instant_validation");
                    }
                }

                break;
            case STATE_SIGNIN_FAILED:
                mDetailText.setText("status_sign_in_failed");
                break;
            case STATE_SIGNIN_SUCCESS:
                break;

        }

        if (user == null) {
            mPhoneNumberViews.setVisibility(View.VISIBLE);
            mSignedInViews.setVisibility(View.GONE);
            mStatusText.setText("signed_out");
        }else {
            mPhoneNumberViews.setVisibility(View.GONE);
            mSignedInViews.setVisibility(View.VISIBLE);
            enableViews(mPhoneNumberField, mVerificationField);
            mPhoneNumberField.setText(null);
            mVerificationField.setText(null);
            mStatusText.setText("signed_in");
            mDetailText.setText(getString(R.string.firebase_status_fmt, user.getUid()));
        }

    }

    private boolean validatePhoneNumber() {
        //String phoneNumber = mPhoneNumberField.getText().toString();
        String mPhoneNumber = phoneNumber;
//        if (TextUtils.isEmpty(mPhoneNumber)) {
//            mPhoneNumberField.setError("Invalid phone number.");
//            return false;
//        }
        return true;
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
            case R.id.button_start_verification:
                if (!validatePhoneNumber()) {
                    return;
                }
                startPhoneNumberVerification(mPhoneNumberField.getText().toString());
                break;
            case R.id.button_verify_phone:
                String code = mVerificationField.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    mVerificationField.setError("Cannot be empty.");
                    return;
                }
                verifyPhoneNumberWithCode(mVerificationId, code);
                break;
            case R.id.button_resend:
              //  resendVerificationCode(mPhoneNumberField.getText().toString(), mResendToken);
                resendVerificationCode(phoneNumber, mResendToken);
                break;
            case R.id.sign_out_button:
                signOut();
                break;

        }
    }

}
