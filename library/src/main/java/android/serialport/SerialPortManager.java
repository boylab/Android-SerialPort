package android.serialport;

import android.serialport.loop.AutoReadThread;
import android.util.Log;

import androidx.annotation.NonNull;

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
     * @param portBean
     */
    public SerialPortManager(PortBean portBean) {
        this(portBean, false);
    }

    public SerialPortManager(PortBean portBean, boolean isAutoRead) {
        this.open(portBean, isAutoRead);
    }

    /**
     * 开启串口
     * @param portBean
     * @return
     */
    private SerialPort open(PortBean portBean, boolean isAutoRead) {
        try {
            File deviceFile = new File(portBean.getPath());
            if (!deviceFile.exists()) {
                Loge("device path is not exists");
                return null;
            }

            this.mSerialPort = new SerialPort(deviceFile, portBean);
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
