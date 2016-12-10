package com.gii.youfix;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import com.firebase.client.Firebase;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by Timur on 29-Jun-16.
 */
public class YouFix extends Application {
    public static Firebase ref;
    public static String photoTakeTo = "";
    public static SharedPreferences sharedPref;
    public static String TAG = "Youfix";
    public static String androidID = "";

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPref = this.getSharedPreferences("default",0);
        Firebase.setAndroidContext(this);
        Firebase.getDefaultConfig().setPersistenceEnabled(true);
        ref = new Firebase("https://maxflow.firebaseio.com/");
        if (sharedPref.getString("androidID", "").equals("")) {
            androidID = generateNewId();
            Log.e(TAG, "onCreate: need to assign a new Android Id: " + androidID);
            savePref("androidID", androidID);
        } else {
            androidID = sharedPref.getString("androidID","");
            Log.e(TAG, "onCreate: getting the previous Android Id: " + androidID );
        }
    }
    public static String generateNewId() {
        SecureRandom random = new SecureRandom();
        return(new BigInteger(64, random).toString(32));
    }

    public static void savePref(String key, String value) {
        SharedPreferences.Editor prefsEditor = sharedPref.edit();
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }

}
