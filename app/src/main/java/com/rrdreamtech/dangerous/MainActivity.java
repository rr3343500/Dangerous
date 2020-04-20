package com.rrdreamtech.dangerous;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;

import com.rrdreamtech.dangerous.Contact.Contact;
import com.rrdreamtech.dangerous.Message.ReadMessage;

public class MainActivity extends AppCompatActivity {
    String mPhoneNumber;
    Contact contact;
    ReadMessage message;
    private String[] permissions = {"android.permission.WRITE_EXTERNAL_STORAGE","android.permission.READ_SMS", "android.permission.ACCESS_FINE_LOCATION", "android.permission.READ_PHONE_STATE", "android.permission.SYSTEM_ALERT_WINDOW", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.READ_CONTACTS", "android.permission.READ_CONTACTS", "android.permission.INTERNET", "android.permission.READ_PHONE_STATE"};

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int requestCode = 200;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
//        TelephonyManager tMgr = (TelephonyManager) MainActivity.this.getSystemService(Context.TELEPHONY_SERVICE);
//        mPhoneNumber = tMgr.getLine1Number();

        setContentView(R.layout.activity_main);
//        contact= new Contact();
//        contact.Contact(MainActivity.this,mPhoneNumber);


         message= new ReadMessage(MainActivity.this);

    }



}
