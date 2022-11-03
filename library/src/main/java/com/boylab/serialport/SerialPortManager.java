package com.boylab.serialport;

import android.serialport.SerialPort;
import android.util.Log;

import androidx.annotation.NonNull;
import com.boylab.serialport.loop.AutoReadThread;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicBoolean;

public class SerialPortManager {
    public static boolean DEBUG = false;

    /**
     * 串口和输入输出流
     */
    private AtomicBoolean isPortOpen = new AtomicBoolean(false);
    private SerialPort mSerialPort = null;
    private InputStream mInputStream = null;
    private OutputStream mOutputStream = null;

    /**
     * 线程
     */
    private AutoReadThread mAutoReadThread;
    private AtomicBoolean isThreadOpen = new AtomicBoolean(false);

    /**
     * 默认开启串口读取
     * @param serialBean
     */
    public SerialPortManager(SerialBean serialBean) {
        this(serialBean, false);
    }

    public SerialPortManager(SerialBean serialBean, boolean isAutoRead) {
        this.open(serialBean, isAutoRead);
    }

    /**
     * 开启串口
     * @param serialBean
     * @return
     */
    private SerialPort open(SerialBean serialBean, boolean isAutoRead) {
        try {
            File deviceFile = new File(serialBean.getDevicePath());
            if (!deviceFile.exists()) {
                Loge("device path is not exists");
                return null;
            }
            int baudrate = serialBean.getBaudrate();
            int dataBits = serialBean.getDataBits();
            int parity = serialBean.getParity();
            int stopBits = serialBean.getStopBits();

            this.mSerialPort = new SerialPort(deviceFile, baudrate, dataBits, parity, stopBits);
            this.mInputStream = mSerialPort.getInputStream();
            this.mOutputStream = mSerialPort.getOutputStream();
            this.isPortOpen.set(true);

            if (isAutoRead){
                this.mAutoReadThread = new AutoReadThread(mInputStream, isThreadOpen);
                this.mAutoReadThread.start();
            }
            Logi("port open success");
        } catch (IOException e) {
            Loge("port open with IOException " + e.toString());
            return mSerialPort;
        }
        return mSerialPort;
    }

    /**
     * 通过串口写数据
     * @param sendData
     */
    public void write(byte[] sendData) {
        try {
            if (sendData.length > 0 && this.isPortOpen.get()) {
                this.mOutputStream.write(sendData);
                this.mOutputStream.flush();
            }
        } catch (IOException e) {
            Loge("port write failed " + e.toString());
        }
    }

    /**关闭串口
     *
     */
    public void close() {
        try {
            if (isThreadOpen.get()){
                this.mAutoReadThread.shutdown();
                this.isThreadOpen.set(false);
            }

            if (this.isPortOpen.get()) {
                this.isPortOpen.set(false);

                this.mInputStream.close();
                this.mOutputStream.close();
                this.mSerialPort.close();
                Logi("port close success");
            }
        } catch (IOException e) {
            Loge("port close with IOException" + e.toString());
        }
    }

    public void setOnAutoReadListener(OnAutoReadListener onAutoReadListener) {
        if (mAutoReadThread != null){
            mAutoReadThread.setOnAutoReadListener(onAutoReadListener);
        }
    }

    public InputStream getInputStream() {
        return mInputStream;
    }

    public OutputStream getOutputStream() {
        return mOutputStream;
    }

    /**
     * 串口数据回调
     */
    public interface OnAutoReadListener {
        void onAutoRead(byte[] data, int length);
    }

    /**
     * 日志调试入口
     * @param msg
     */
    public static void Logi(@NonNull String msg) {
        if (DEBUG){
            Log.i(">>>serialport>>>", msg);
        }
    }

    public static void Loge(@NonNull String msg) {
        if (DEBUG){
            Log.i(">>>serialport>>>", msg);
        }
    }

}
