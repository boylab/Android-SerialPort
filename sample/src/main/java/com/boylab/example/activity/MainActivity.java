package com.boylab.example.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.serialport.OnAutoReadListener;
import android.serialport.SerialBean;
import android.serialport.SerialPortManager;

import com.boylab.example.utils.SharePref;

import com.boylab.example.R;
import com.boylab.example.view.ReceiveView;
import com.boylab.example.view.SendView;
import com.boylab.example.view.SerialPortView;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SendView.OnSendListener, SerialPortView.OnSerialOpenListener, OnAutoReadListener {

    private SerialPortManager serialPortManager = null;

    private SerialPortView serialPort;
    private SendView sendView;
    private ReceiveView receiveView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        serialPort = findViewById(R.id.serialPort);
        sendView = findViewById(R.id.sendView);
        receiveView = findViewById(R.id.receiveView);

        sendView.setOnSendListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        String[] devicesPath = SerialPortManager.allDevicesPath();

        List<String> devicesList = Arrays.asList(devicesPath);
        Collections.sort(devicesList);  //排序
        serialPort.setSerialPort(devicesList, this);

        HashMap<String, Integer> serial = SharePref.getSerial(this);
        String sendData = SharePref.getSendData(this);
        boolean hexShow = SharePref.isHexShow(this);
        boolean isNext = SharePref.isNextLine(this);

        serialPort.setSerial(serial);
        sendView.setInputSend(sendData);
        receiveView.setHexShow(hexShow);
        receiveView.setNextLine(isNext);
    }

    @Override
    public void onSerialOpen(SerialBean serialBean, boolean openSerial) {
        if (openSerial){
            serialPortManager = new SerialPortManager(serialBean, true);
            serialPortManager.setOnAutoReadListener(this);
        }else {
            serialPortManager.close();
        }
    }

    @Override
    public void onSend(byte[] data) {
        if (serialPortManager != null){
            serialPortManager.write(data);
        }
    }

    @Override
    public void onAutoRead(byte[] data, int length) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                receiveView.addShow(data);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (serialPortManager != null){
            serialPortManager.close();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharePref.setSerial(this, serialPort.getSerial());
        SharePref.setSendData(this, sendView.getInputSend());
        SharePref.setHexShow(this, receiveView.isHexShow());
        SharePref.setNextLine(this, receiveView.isNextLine());
    }
}