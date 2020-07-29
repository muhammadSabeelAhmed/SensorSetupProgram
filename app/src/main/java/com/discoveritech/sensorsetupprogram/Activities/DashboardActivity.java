package com.discoveritech.sensorsetupprogram.Activities;

import android.app.admin.DevicePolicyManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.discoveritech.sensorsetupprogram.GeneralClasses.Global;
import com.discoveritech.sensorsetupprogram.R;
import com.discoveritech.sensorsetupprogram.TreeSystem.DetailInfo;
import com.discoveritech.sensorsetupprogram.TreeSystem.HeaderInfo;
import com.discoveritech.sensorsetupprogram.TreeSystem.MyListAdapter;
import com.discoveritech.sensorsetupprogram.UDP.UDPBroadcast;
import com.macasaet.fernet.Key;
import com.macasaet.fernet.StringValidator;
import com.macasaet.fernet.Token;
import com.macasaet.fernet.Validator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_all, btn_resume, btn_pause, btn_upadte, btn_shutdown, btn_clear, btn_send;
    EditText ssid, pass;
    String txt_ssid = "";
    String txt_pass = "";
    RelativeLayout v;
    UDPBroadcast udpBroadcast;
    public static String[] myarr = new String[4];
    public static Handler handler;
    Handler myhandler;
    Runnable myRunnable;
    Global global;
    private static LinkedHashMap<String, HeaderInfo> myDepartments = new LinkedHashMap<String, HeaderInfo>();
    private static ArrayList<HeaderInfo> deptList = new ArrayList<HeaderInfo>();
    private MyListAdapter listAdapter;

    private ExpandableListView myList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        init();

        Spinner spinner = (Spinner) findViewById(R.id.department);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.dept_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        //Just add some data to start with
        // loadData();

        //get reference to the ExpandableListView
        myList = (ExpandableListView) findViewById(R.id.myList);
        //create the adapter by passing your ArrayList data
        listAdapter = new MyListAdapter(DashboardActivity.this, deptList);
        //attach the adapter to the list
        myList.setAdapter(listAdapter);

        //expand all Groups
        expandAll();

        //add new item to the List
        Button add = (Button) findViewById(R.id.add);
        add.setOnClickListener(this);

        //listener for child row click
        myList.setOnChildClickListener(myListItemClicked);
        //listener for group heading click
        myList.setOnGroupClickListener(myListGroupClicked);

    }

    public void init() {
        global = new Global();
        v = findViewById(R.id.view);
        btn_all = (Button) findViewById(R.id.btn_allReports);
        btn_all.setOnClickListener(this);
        btn_resume = (Button) findViewById(R.id.btn_resume);
        btn_resume.setOnClickListener(this);
        btn_pause = (Button) findViewById(R.id.btn_pause);
        btn_pause.setOnClickListener(this);
        btn_upadte = (Button) findViewById(R.id.btn_update);
        btn_upadte.setOnClickListener(this);
        btn_shutdown = (Button) findViewById(R.id.btn_shutdown);
        btn_shutdown.setOnClickListener(this);
        btn_clear = (Button) findViewById(R.id.btn_clear);
        btn_clear.setOnClickListener(this);
        btn_send = (Button) findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this);
        ssid = (EditText) findViewById(R.id.network_ssd);
        pass = (EditText) findViewById(R.id.network_pass);
        udpBroadcast = new UDPBroadcast();


        new Thread() {
            @Override
            public void run() {
                udpBroadcast.receiveBroadcst();
            }
        }.start();

        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                //Log.d("MyReceivedData", "" + udpBroadcast.udp_received.length);
          /*      if (udpBroadcast.udp_received.length == 3) {
                    Toast.makeText(DashboardActivity.this.getApplicationContext(), udpBroadcast.udp_received[0] + "---" + udpBroadcast.udp_received[1] + "---" + udpBroadcast.udp_received[2], Toast.LENGTH_SHORT).show();
                    addProduct(udpBroadcast.udp_received[0], udpBroadcast.udp_received[1], udpBroadcast.udp_received[2]);
                }*/

                // Toast.makeText(DashboardActivity.this.getApplicationContext(), myarr[0] + "---" + myarr[1] + "---" + myarr[2], Toast.LENGTH_SHORT).show();
                addProduct(myarr[0], myarr[1], myarr[2], myarr[3]);

                listAdapter.notifyDataSetChanged();
                //collapse all groups
                // collapseAll();
            }
        };
//
//        myhandler = new Handler();
//        myRunnable = new Runnable() {
//            @Override
//            public void run() {
//                if (myarr.size() ==3)
//                    myhandler.postDelayed(myRunnable, 250);
//            }
//        };
//        myhandler.post(myRunnable);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_allReports:
                udpBroadcast.sendBroadcast(encryptContent(createCommand("Report")), DashboardActivity.this);

                break;
            case R.id.btn_resume:
                udpBroadcast.sendBroadcast(encryptContent(createCommand("Resume_Posting")), DashboardActivity.this);

                break;
            case R.id.btn_pause:
                udpBroadcast.sendBroadcast(encryptContent(createCommand("Pause_Posting")), DashboardActivity.this);

                break;
            case R.id.btn_update:
                udpBroadcast.sendBroadcast(encryptContent(createCommand("Update_Check")), DashboardActivity.this);

                break;
            case R.id.btn_shutdown:
                udpBroadcast.sendBroadcast(encryptContent(createCommand("Shutdown")), DashboardActivity.this);

                break;
            case R.id.btn_clear:
                //  Toast.makeText(DashboardActivity.this, "Will be handle later", Toast.LENGTH_SHORT).show();
                myDepartments.clear();
                deptList.clear();
                listAdapter.notifyDataSetChanged();

                break;
            case R.id.btn_send:
                if (validate()) {
                    udpBroadcast.sendBroadcast(encryptContent(createCommand("Set_Network", txt_ssid, txt_pass)), DashboardActivity.this);
                }
                break;
            case R.id.add:

                Spinner spinner = (Spinner) findViewById(R.id.department);
                String department = spinner.getSelectedItem().toString();
                EditText editText = (EditText) findViewById(R.id.product);
                String product = editText.getText().toString();
                editText.setText("");

                //add a new item to the list
                int groupPosition = addProduct(department, product, "1235", "");
                //notify the list so that changes can take effect
                listAdapter.notifyDataSetChanged();

                //collapse all groups
                collapseAll();
                //expand the group where item was just added
                myList.expandGroup(groupPosition);
                //set the current group to be selected so that it becomes visible
                myList.setSelectedGroup(groupPosition);

                break;

        }

    }

    public boolean validate() {
        txt_ssid = ssid.getText().toString();
        txt_pass = pass.getText().toString();
        boolean valid = true;
        if (txt_ssid.isEmpty()) {
            Toast.makeText(DashboardActivity.this, R.string.ssid_message, Toast.LENGTH_SHORT).show();
            valid = false;
        } else if (txt_pass.isEmpty()) {
            Toast.makeText(DashboardActivity.this, R.string.pass_message, Toast.LENGTH_SHORT).show();
            valid = false;
        }
        return valid;
    }

    public String createCommand(String Command) {
        String all_value = "[\"All\"]";
        String[] jsonArray = new String[1];
        String value = "";
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("command", Command);
            jsonObject.put("params", new JSONArray());
            jsonArray[0] = "[" + all_value + ", " + jsonObject.toString() + "]";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        value = jsonArray[0].replaceAll("\\\\", "");
        Log.d("MyObject", "" + value);
        return value;
    }


    public String createCommand(String command, String SSID, String Password) {
        String all_value = "[\"All\"]";
        String[] jsonArray = new String[1];
        String value = "";
        try {
            JSONArray myJsonArray = new JSONArray();
            myJsonArray.put(SSID);
            myJsonArray.put(Password);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("command", command);
            jsonObject.put("params", myJsonArray);
            jsonArray[0] = "[" + all_value + ", " + jsonObject.toString() + "]";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        value = jsonArray[0].replaceAll("\\\\", "");
        Log.d("MyObject", "" + value);
        return value;
    }


    public byte[] encryptContent(String Command) {
        final Key key = new Key("3t55GSk5qDRUif_v4MNQGLrkzaWv-TFOSJpqQWj9KKg=");
        final Token token = Token.generate(key, Command);

//        final Validator<String> validator = new StringValidator() {
//            public TemporalAmount getTimeToLive() {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    return Duration.ofHours(1);
//                }
//                return null;
//            }
//        };
     /*   String payload = token.validateAndDecrypt(key, validator);
        byte[] encodedBytes = key.encrypt(token.toString().getBytes(), token.getInitializationVector());*/

        return token.serialise().getBytes();
    }

    //method to expand all groups
    private void expandAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            myList.expandGroup(i);
        }
    }

    //method to collapse all groups
    private void collapseAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            myList.collapseGroup(i);
        }
    }

    //our child listener
    private ExpandableListView.OnChildClickListener myListItemClicked = new ExpandableListView.OnChildClickListener() {

        public boolean onChildClick(ExpandableListView parent, View v,
                                    int groupPosition, int childPosition, long id) {

            //get the group header
            HeaderInfo headerInfo = deptList.get(groupPosition);
            //get the child info
            DetailInfo detailInfo = headerInfo.getProductList().get(childPosition);
            //display it or do something with it
            //  Toast.makeText(getBaseContext(), "" + headerInfo.getName() + "/" + detailInfo.getName(), Toast.LENGTH_LONG).show();
            return false;
        }

    };

    //our group listener
    private ExpandableListView.OnGroupClickListener myListGroupClicked = new ExpandableListView.OnGroupClickListener() {

        public boolean onGroupClick(ExpandableListView parent, View v,
                                    int groupPosition, long id) {

            //get the group header
            HeaderInfo headerInfo = deptList.get(groupPosition);
            //display it or do something with it
            //     Toast.makeText(getBaseContext(), "" + headerInfo.getName(), Toast.LENGTH_LONG).show();

            return false;
        }

    };

    //here we maintain our products in various departments
    public static int addProduct(String department, String sequence, String product, String ip) {

        int groupPosition = 0;

        //check the hash map if the group already exists
        HeaderInfo headerInfo = myDepartments.get(department);
        //add the group if doesn't exists
        if (headerInfo == null) {
            headerInfo = new HeaderInfo();
            headerInfo.setName(department);
            headerInfo.setDate_time(convertToDateTime(new Date()));

            myDepartments.put(department, headerInfo);
            deptList.add(headerInfo);

            //get the children for the group
            ArrayList<DetailInfo> productList = headerInfo.getProductList();
            //size of the children list
            int listSize = productList.size();
            //add to the counter
            listSize++;
            //create a new child and add that to the group

            DetailInfo detailInfo = new DetailInfo();
            detailInfo.setSequence("ip");
            detailInfo.setName(ip);
            productList.add(detailInfo);
        } else {
            myDepartments.get(department).setDate_time(convertToDateTime(new Date()));
            for (int i = 0; i <= myDepartments.get(department).getProductList().size() - 1; i++) {
                if (sequence.equals(myDepartments.get(department).getProductList().get(i).getSequence())) {
                    myDepartments.get(department).getProductList().remove(i);
                }

            }
        }

        //get the children for the group
        ArrayList<DetailInfo> productList = headerInfo.getProductList();
        //size of the children list
        int listSize = productList.size();
        //add to the counter
        listSize++;
        //create a new child and add that to the group

        DetailInfo detailInfo = new DetailInfo();
        detailInfo.setSequence(sequence);
        detailInfo.setName(product);
        productList.add(detailInfo);

        int count = productList.size();
        for (int i = 0; i < count; i++) {
            for (int j = i + 1; j < count; j++) {
                if (productList.get(i).getSequence().equals(productList.get(j).getSequence())) {
                    productList.remove(j--);
                    count--;
                }
            }
        }

        Collections.sort(productList.subList(1, productList.size() - 1), new Comparator<DetailInfo>() {
            @Override
            public int compare(DetailInfo o1, DetailInfo o2) {
                int val1 = Integer.parseInt(o1.getSequence());
                int val2 = Integer.parseInt(o2.getSequence());
                return val1 - val2;
            }
        });

        headerInfo.setProductList(productList);
        //find the group position inside the list
        groupPosition = deptList.indexOf(headerInfo);
        return groupPosition;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_dashboard, menu);
        return true;
    }

    public static void removeDuplicates(List<?> list) {
        int count = list.size();

        for (int i = 0; i < count; i++) {
            for (int j = i + 1; j < count; j++) {
                if (list.get(i).equals(list.get(j))) {
                    list.remove(j--);
                    count--;
                }
            }
        }
    }

    public static String convertToDateTime(Date date) {
        DateFormat simpleFormatter = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
        return simpleFormatter.format(date);
    }

    @Override
    public void onBackPressed() {
        global.changeActivity(DashboardActivity.this, new SplashActivity());
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        finish();
    }
}