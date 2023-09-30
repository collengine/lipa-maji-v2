



package com.water.eldowas.service;


import android.content.Intent;
import android.content.SharedPreferences;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.water.eldowas.util.Helper;
import com.water.eldowas.util.TokenObject;

import java.util.HashMap;
import java.util.Map;

import static com.water.eldowas.util.AppConstants.REGISTRATION_COMPLETE;
import static com.water.eldowas.util.AppConstants.SHARED_PREF;

/**
 * Created  on 6/30/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();
    private RequestQueue queue;
    private String userUID = "1";
    private String TOKENS = "tokens";
    private TokenObject tokenObject;
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();



        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);

        // sending reg id to your server
        sendTheRegisteredTokenToWebServer(refreshedToken);

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendTheRegisteredTokenToWebServer(final String token){
        queue = Volley.newRequestQueue(getApplicationContext());


        StringRequest stringPostRequest = new StringRequest(Request.Method.POST, Helper.PATH_TO_SERVER_IMAGE_UPLOAD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                tokenObject = gson.fromJson(response, TokenObject.class);

                if (null == tokenObject) {


                } else {

                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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

    /**
     * Setting values in Preference:
     */

    private void storeRegIdInPref(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.apply();
    }
}
