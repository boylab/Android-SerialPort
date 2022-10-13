package android.serialport.loop;

import android.serialport.SerialPortManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;

public class AutoReadThread extends LoopThread {

    private AtomicBoolean isThreadOpen;
    private InputStream mInputStream;
    private byte[] buffer = new byte[1024];

    private SerialPortManager.OnAutoReadListener onAutoReadListener = null;

    public AutoReadThread(InputStream mInputStream, AtomicBoolean isThreadOpen) {
        super();
        this.mInputStream = mInputStream;
        this.isThreadOpen = isThreadOpen;
    }

    public void setOnAutoReadListener(SerialPortManager.OnAutoReadListener onAutoReadListener) {
        this.onAutoReadListener = onAutoReadListener;
    }

    @Override
    protected void beforeLoop() throws Exception {
        super.beforeLoop();
        isThreadOpen.set(true);
        SerialPortManager.Logi("Auto Read is Open");
    }

    @Override
    protected void runInLoopThread() throws Exception {
        if (isThreadOpen.get()) {
            try {
                if (mInputStream == null) {
                    return;
                }
                int size = mInputStream.read(buffer);
                if (size > 0) {
                    byte[] readBytes = new byte[size];
                    System.arraycopy(buffer, 0, readBytes, 0, size);
                    if (onAutoReadListener != null) {
                        onAutoReadListener.onAutoRead(readBytes, size);
                    }
                }
                Thread.sleep(100);
            } catch (IOException e) {
                SerialPortManager.Loge("Auto Read Failed " + e.toString());
            }
        }
    }

    @Override
    protected void loopFinish(Exception e) {
        SerialPortManager.Logi("Auto Read is Close");
    }
}
