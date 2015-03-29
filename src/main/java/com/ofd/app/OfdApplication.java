package com.ofd.app;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

/**
 * Created by npc on 28/03/15.
 */
public class OfdApplication extends Application {


    private static Context mContext;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        mContext = getApplicationContext();
        Parse.initialize(this, "BBlcerZiharLqJDPjIXyaQ8ygGUcy5jyh5AUk3BB", "bmiqxJydxNyzejsp5cmgYIUcEMzTJW32MHgU1ecm");
        // Save the current Installation to Parse.
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public static Context getAppContext(){
        return mContext;
    }


}