package com.ofd.app.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ofd.app.AppSharedPreferences;
import com.ofd.app.R;
import com.ofd.app.entities.Account;
import com.ofd.app.entities.Contact;
import com.ofd.app.entities.Conversation;
import com.ofd.app.entities.ListItem;
import com.ofd.app.entities.Message;
import com.ofd.app.xmpp.jid.InvalidJidException;
import com.ofd.app.xmpp.jid.Jid;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONException;
import org.json.JSONObject;

public class ConfirmActivity extends XmppActivity {

    private TextView deliveryData;
    private Button btnConfirm;
    private String data,deliveryBy,objId;
    private boolean ifContactPresent= false;
    private AppSharedPreferences appSharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        appSharedPreferences=new AppSharedPreferences(this);
        deliveryData=(TextView)findViewById(R.id.delivery_details);
        btnConfirm=(Button)findViewById(R.id.btn_confirm);

        try {
            JSONObject pushData = new JSONObject(getIntent().getStringExtra("com.parse.Data"));
            data= "Your package:" + pushData.getString("pkgInf")+" will be delivered by "+pushData.getString("delBy")+" to your address "+pushData.getString("delAddress")+" around "+pushData.getString("schTime")+" today";
            data+="\nPlease confirm your availability";
            deliveryBy=pushData.getString("delBy");
            objId=pushData.getString("objId");
        } catch (JSONException e) {
        }

        deliveryData.setText(data);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseQuery<ParseObject> query = ParseQuery.getQuery("Delivery");

                query.getInBackground(objId,new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject parseObject, ParseException e) {
                        if(e==null){
                            parseObject.put("status","ofd");
                            parseObject.saveInBackground();
                            appSharedPreferences.setConversationAsDelivery(parseObject.getString("deliveryBy"),parseObject.getObjectId(),true,"r");
                        }
                    }
                });
                if(xmppConnectionServiceBound){
                  for(Account acc:xmppConnectionService.getAccounts()){
                      Contact c = null;
                      try {
                          c = acc.getRoster().getContact(Jid.fromString(deliveryBy + "@hackathon.hike.in"));
                      } catch (InvalidJidException e) {
                          e.printStackTrace();
                      }
                      if(c.showInRoster()){
                              Conversation conversation = xmppConnectionService
                                      .findOrCreateConversation(c.getAccount(),
                                              c.getJid(), false);
                              xmppConnectionService.sendMessage(new Message(conversation,"I will receive the item as per the schedule",Message.ENCRYPTION_NONE));
                              switchToConversation(conversation);

                      }else {
                          c.addOtrFingerprint(null);
                          xmppConnectionService.createContact(c);
                          Conversation conversation = xmppConnectionService
                                  .findOrCreateConversation(c.getAccount(),
                                          c.getJid(), false);
                          xmppConnectionService.sendMessage(new Message(conversation,"I will receive the item as per the schedule",Message.ENCRYPTION_NONE));
                          switchToConversation(conversation);

                      }

                  }
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_confirm, menu);
        return true;
    }

    @Override
    void onBackendConnected() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
