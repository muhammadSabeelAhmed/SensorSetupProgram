package com.discoveritech.sensorsetupprogram.UDP;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;

import com.discoveritech.sensorsetupprogram.Activities.DashboardActivity;
import com.discoveritech.sensorsetupprogram.GeneralClasses.Constants;
import com.discoveritech.sensorsetupprogram.GeneralClasses.Global;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import static com.discoveritech.sensorsetupprogram.Activities.DashboardActivity.handler;

public class UDPBroadcast {
    public String[] udp_received = new String[4];

    public void sendBroadcast(byte[] sendData, Context mContext) {
        // Hack Prevent crash (sending should be done using an async task)
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            //Open a random port to send the package
            DatagramSocket socket = new DatagramSocket();
            socket.setBroadcast(true);
            //  byte[] sendData = (messageStr.getBytes(StandardCharsets.US_ASCII));
            //  Base64.getEncoder().encode(token.serialise().getBytes(StandardCharsets.US_ASCII)
            Log.d("SendingBoradcast", "True: " + sendData.length);

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, getBroadcastAddress(mContext), Constants.SENDING_PORT);
            socket.send(sendPacket);

        } catch (IOException e) {
            Log.d("SendingBoradcast", "IOException: " + e.getMessage());
        }
    }

    public void receiveBroadcst() {
        try {
            //Keep a socket open to listen to all the UDP trafic that is destined for this port
            DatagramSocket socket = new DatagramSocket(Constants.RECEVING_PORT, InetAddress.getByName("0.0.0.0"));
            socket.setBroadcast(true);

            while (true) {
                Log.d("ReceivingBoradcast", "Ready to receive broadcast packets!");

                //Receive a packet
                byte[] recvBuf = new byte[15000];
                DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
                socket.receive(packet);

                String data = new String(packet.getData()).trim();
                //  {"serial": "res-sm-000158", "measurement": [1595865179, 1, 76, "Luftballon", 0, ""]}

                try {
                    JSONObject jsonObject = new JSONObject(data);

                    JSONArray jsonArray = jsonObject.getJSONArray("measurement");
                    Log.d("ReceivingBoradcast", "measurement" + jsonArray.toString());

                    Message sensor = Message.obtain();
                    sensor.obj = "start_updating";

                    udp_received[0] = jsonObject.get("serial").toString();
                    if (jsonArray.length() > 0) {
                        udp_received[1] = jsonArray.get(1).toString();
                        udp_received[2] = jsonArray.get(2).toString() + " " + jsonArray.get(3).toString() + "-" + jsonArray.get(5).toString();
                        udp_received[3] = packet.getAddress().getHostAddress();
                        DashboardActivity.myarr = udp_received;
                        //   DashboardActivity.addProduct(jsonObject.get("serial").toString(), jsonArray.get(1).toString(), jsonArray.get(2).toString() + " " + jsonArray.get(3).toString() + "-" + jsonArray.get(5).toString());
                        handler.sendMessage(sensor);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException ex) {
            Log.i("ReceivingBoradcast", "Oops" + ex.getMessage());
        }
    }

    InetAddress getBroadcastAddress(Context mContext) throws IOException {
        WifiManager wifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcp = wifi.getDhcpInfo();
        // handle null somehow
        int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
        byte[] quads = new byte[4];
        for (int k = 0; k < 4; k++)
            quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
        return InetAddress.getByAddress(quads);
    }
}
