package com.discoveritech.sensorsetupprogram.UDP;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;

import com.discoveritech.sensorsetupprogram.GeneralClasses.Constants;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class UDPBroadcast {
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
            Log.d("SendingBoradcast", "True: " +sendData.length);

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, getBroadcastAddress(mContext), Constants.PORT);
            socket.send(sendPacket);

        } catch (IOException e) {
            Log.d("SendingBoradcast", "IOException: " + e.getMessage());
        }
    }

    public void receiveBroadcst() {
        try {
            //Keep a socket open to listen to all the UDP trafic that is destined for this port
            DatagramSocket socket = new DatagramSocket(Constants.PORT, InetAddress.getByName("0.0.0.0"));
            socket.setBroadcast(true);

            while (true) {
                Log.d("ReceivingBoradcast", "Ready to receive broadcast packets!");

                //Receive a packet
                byte[] recvBuf = new byte[15000];
                DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
                socket.receive(packet);

                //Packet received
                Log.d("ReceivingBoradcast", "Packet received from: " + packet.getAddress().getHostAddress() + "--Name:" + packet.getAddress().getCanonicalHostName());
                String data = new String(packet.getData()).trim();
                Log.d("ReceivingBoradcast", "Packet received; data: " + data);
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
