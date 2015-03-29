package com.ofd.app.ui;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ofd.app.AppSharedPreferences;
import com.ofd.app.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.util.Calendar;
import java.util.Date;

public class CreateDeliveryActivity extends Activity {

    private ImageButton btn_pick_contacts;
    private EditText etxt_phone, etxt_address, etxt_pkg;
    private String phoneNumber, address, pkgInfo, deliveryBy;
    private Button btnTimePicker;

    private int hour;
    private int minute;

    private Date deliveryDate;
    private AppSharedPreferences appSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_delivery);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        deliveryBy=getIntent().getStringExtra("account");
        appSharedPreferences=new AppSharedPreferences(this);
        btn_pick_contacts=(ImageButton)findViewById(R.id.btn_pick_contact);

        etxt_phone=(EditText)findViewById(R.id.etxt_to_number);
        etxt_address=(EditText)findViewById(R.id.etxt_del_addrs);
        etxt_pkg=(EditText)findViewById(R.id.etxt_pkg_info);
        btnTimePicker = (Button) findViewById(R.id.btn_time_picker);
        Calendar c = Calendar.getInstance();
        //deliveryDate=c.getTime();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        btn_pick_contacts.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(intent, 1);

            }
        });

        btnTimePicker.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                hour = c.get(Calendar.HOUR_OF_DAY);
                minute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog tpd = new TimePickerDialog(CreateDeliveryActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minuteN) {
                                // Display Selected time in textbox
                                btnTimePicker.setText("Delivery Time: "+hourOfDay + ":" + minuteN);
                                c.set(Calendar.HOUR_OF_DAY,hourOfDay);
                                c.set(Calendar.MINUTE,minuteN);
                                deliveryDate=c.getTime();

                            }
                        }, hour, minute, false);
                tpd.show();
            }

        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_delivery, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        if(id== R.id.action_done){
            createDelivery();
            //Toast
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void createDelivery() {
        if(!etxt_phone.getText().toString().equals("") && !etxt_address.getText().toString().equals("") && !etxt_pkg.getText().toString().equals("") && deliveryDate!=null){
            address=etxt_address.getText().toString();
            pkgInfo=etxt_pkg.getText().toString();
            phoneNumber=etxt_phone.getText().toString();

            final ParseObject delivery = new ParseObject("Delivery");
            delivery.put("deliveryTo", phoneNumber);
            if(deliveryBy!=null)
                delivery.put("deliveryBy",deliveryBy);
            delivery.put("deliveryAddress", address);
            delivery.put("pkgInfo",pkgInfo);
            delivery.put("status","Initiated");
            if(deliveryDate!=null)
                delivery.put("scheduledTime",deliveryDate);
            delivery.saveInBackground(new SaveCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        appSharedPreferences.setConversationAsDelivery(phoneNumber, delivery.getObjectId(),false,"d");
                    } else {
                    }
                }
            });
            finish();
        }
        else {
            Toast.makeText(this,"Enter all the details and try again",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            Uri uri = data.getData();

            if (uri != null) {
                Cursor c = null;
                try {
                    c = getContentResolver().query(uri, new String[]{
                                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                                    ContactsContract.CommonDataKinds.Phone.TYPE },
                            null, null, null);

                    if (c != null && c.moveToFirst()) {
                        phoneNumber = c.getString(0).replaceAll("\\s+","");
                        //int type = c.getInt(1);

                        etxt_phone.setText(phoneNumber);

                    }
                } finally {
                    if (c != null) {
                        c.close();
                    }
                }
            }
        }
    }



}
