package com.example.userpc.form_sutdio;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyAndroidFirebaseInstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String refresh_tok= FirebaseInstanceId.getInstance().getToken();
        Log.e("Token",refresh_tok);
        updateToken();

    }

    private void updateToken() {
    }
}
