package com.discoveritech.sensorsetupprogram.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.discoveritech.sensorsetupprogram.GeneralClasses.Constants;
import com.discoveritech.sensorsetupprogram.GeneralClasses.Global;
import com.discoveritech.sensorsetupprogram.GeneralClasses.PreferencesHandler;
import com.discoveritech.sensorsetupprogram.R;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener {
    EditText txt_sending, txt_receiving;
    PreferencesHandler preferencesHandler;
    Button proceed;
    String rec = "";
    String send = "";
    Global global;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
    }

    private void init() {
        global = new Global();
        preferencesHandler = new PreferencesHandler(SplashActivity.this);
        proceed = findViewById(R.id.btn_proceed);
        proceed.setOnClickListener(this);
        txt_sending = findViewById(R.id.txt_sending_port);
        txt_receiving = findViewById(R.id.txt_receving_port);
        Log.d("MyPorts",""+preferencesHandler.getReceivingPort()+"--"+preferencesHandler.getSendingPort());
        txt_receiving.setText(preferencesHandler.getReceivingPort());
        txt_sending.setText(preferencesHandler.getSendingPort());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_proceed:
                if (validate()) {
                    preferencesHandler.setReceivingPort(rec);
                    preferencesHandler.setSendingPort(send);
                 //   Constants.RECEVING_PORT = Integer.parseInt(rec);
                   // Constants.SENDING_PORT = Integer.parseInt(send);
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
}