package com.discoveritech.sensorsetupprogram.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.discoveritech.sensorsetupprogram.GeneralClasses.Constants;
import com.discoveritech.sensorsetupprogram.GeneralClasses.Global;
import com.discoveritech.sensorsetupprogram.R;
import com.discoveritech.sensorsetupprogram.GeneralClasses.myDbAdapter;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener {
    EditText txt_sending, txt_receiving;
    Button proceed;
    String rec = "";
    String send = "";
    Global global;
    myDbAdapter helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
    }

    private void init() {
        helper = new myDbAdapter(SplashActivity.this);
        global = new Global();
        txt_sending = findViewById(R.id.txt_sending_port);
        txt_receiving = findViewById(R.id.txt_receving_port);
        proceed = findViewById(R.id.btn_proceed);
        proceed.setOnClickListener(this);
        ArrayList<String> tempArr = new ArrayList<>();
        tempArr = viewdata();
        if (tempArr.size() > 0) {
            txt_receiving.setText(tempArr.get(1));
            txt_sending.setText(tempArr.get(0));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_proceed:
                if (validate()) {
                    deleteTable();
                    addPortSettings(send, rec);
                    Constants.RECEVING_PORT = Integer.parseInt(rec);
                    Constants.SENDING_PORT = Integer.parseInt(send);
                    global.changeActivity(SplashActivity.this, new DashboardActivity());
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    finish();
                }
                break;
        }
    }

    public boolean validate() {
        boolean result = true;
        rec = txt_receiving.getText().toString();
        send = txt_sending.getText().toString();
        if (rec.isEmpty() || send.isEmpty()) {
            result = false;
            Toast.makeText(SplashActivity.this, "Both Fields required", Toast.LENGTH_SHORT).show();
        }
        return result;
    }


    public void addPortSettings(String sending, String receving) {
        long id = helper.insertData(sending, receving);
        if (id <= 0) {
            Toast.makeText(getApplicationContext(), "Port Settings Unsuccessful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Port Settings Successful", Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList<String> viewdata() {
        ArrayList<String> data = helper.getData();
        return data;
    }

    public void deleteTable() {
        int a = helper.deletePortTable();
        if (a <= 0) {
           // Toast.makeText(getApplicationContext(), "Not Deleted", Toast.LENGTH_SHORT).show();
        } else {
            {
              //  Toast.makeText(getApplicationContext(), "Token Deleted Successfull", Toast.LENGTH_SHORT).show();
            }
        }
    }
}