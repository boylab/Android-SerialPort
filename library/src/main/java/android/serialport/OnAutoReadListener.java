package android.serialport;

public interface OnAutoReadListener {
    /**
     * 串口数据回调
     */
    void onAutoRead(byte[] data, int length);
}
