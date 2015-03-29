package com.ofd.app.gcm;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.ofd.app.ui.ConfirmActivity;
import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by npc on 29/03/15.
 */
public class PushReceiver extends ParsePushBroadcastReceiver {

//    @Override
//    protected void onPushReceive(Context context, Intent intent) {
//    }
//
//    @Override
//    protected void onPushDismiss(Context context, Intent intent) {
//    }

    @Override
    protected void onPushOpen(Context context, Intent intent) {

        Intent i = new Intent(context, ConfirmActivity.class);
        i.putExtras(intent.getExtras());
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
