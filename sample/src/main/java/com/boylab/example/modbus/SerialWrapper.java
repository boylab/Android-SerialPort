package com.boylab.example.modbus;

import android.serialport.SerialPortManager;
import android.serialport.PortBean;

import com.serotonin.modbus4j.serial.SerialPortWrapper;

import java.io.InputStream;
import java.io.OutputStream;

public class SerialWrapper implements SerialPortWrapper {

    private PortBean portBean;
    private SerialPortManager serialPortManager;

    /**
     * 不需要主动调用，modbus系统调用
     */
    @Override
    public void open(){
        if(serialPortManager!= null) {
            serialPortManager.close();
        }
        portBean = new PortBean("/dev/ttyMT2", 38400, PortBean.PARITY_EVEN);
        serialPortManager = new SerialPortManager(portBean, false);   //不开启读线程，交给modbus
    }

    @Override
    public void close() {
        if (serialPortManager != null){
            serialPortManager.close();
            serialPortManager = null;
        }
    }

    @Override
    public InputStream getInputStream() {
        return serialPortManager.getInputStream();
    }

    @Override
    public OutputStream getOutputStream() {
        return serialPortManager.getOutputStream();
    }

    @Override
    public int getBaudRate() {
        return portBean.getSpeed();
    }

    @Override
    public int getDataBits() {
        return portBean.getDataBits();
    }

    @Override
    public int getStopBits() {
        return portBean.getStopBits();
    }

    @Override
    public int getParity() {
        return portBean.getParityInt();
    }
}
