package com.boylab.serialport;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 端口参数
 * device 串口设备文件
 * baudrate 波特率
 * dataBits 数据位；默认8,可选值为5~8
 * parity 奇偶校验；0:无校验位(NONE，默认)；1:奇校验位(ODD);2:偶校验位(EVEN)
 * stopBits 停止位；默认1；1:1位停止位；2:2位停止位
 * flags 默认0
 */
public class SerialBean implements Parcelable {

    private String devicePath = "/dev/ttyMT0";
    private int baudrate = 9600;
    private int dataBits = 8;
    private int stopBits = 1;
    private int parity = 0;   //0:无校验位(NONE，默认)；1:奇校验位(ODD);2:偶校验位(EVEN)

    public SerialBean() {
    }

    public SerialBean(String devicePath, int speed, int parity) {
        this(devicePath, speed, 8, 1, parity);
    }

    public SerialBean(String devicePath, int baudrate, int dataBits, int stopBits, int parity) {
        this.devicePath = devicePath;
        this.baudrate = baudrate;
        this.dataBits = dataBits;
        this.stopBits = stopBits;
        this.parity = parity;
    }

    protected SerialBean(Parcel in) {
        devicePath = in.readString();
        baudrate = in.readInt();
        dataBits = in.readInt();
        stopBits = in.readInt();
        parity = (char) in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(devicePath);
        dest.writeInt(baudrate);
        dest.writeInt(dataBits);
        dest.writeInt(stopBits);
        dest.writeInt((int) parity);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SerialBean> CREATOR = new Creator<SerialBean>() {
        @Override
        public SerialBean createFromParcel(Parcel in) {
            return new SerialBean(in);
        }

        @Override
        public SerialBean[] newArray(int size) {
            return new SerialBean[size];
        }
    };

    public String getDevicePath() {
        return devicePath;
    }

    public void setDevicePath(String devicePath) {
        this.devicePath = devicePath;
    }

    public int getBaudrate() {
        return baudrate;
    }

    public void setBaudrate(int baudrate) {
        this.baudrate = baudrate;
    }

    public int getDataBits() {
        return dataBits;
    }

    public void setDataBits(int dataBits) {
        this.dataBits = dataBits;
    }

    public int getStopBits() {
        return stopBits;
    }

    public void setStopBits(int stopBits) {
        this.stopBits = stopBits;
    }

    public int getParity() {
        return parity;
    }

    public void setParity(int parity) {
        this.parity = parity;
    }

    public void setParity(char parity) {
        this.parity = parity;
    }

    @Override
    public String toString() {
        return "SerialBean{" +
                "devicePath='" + devicePath + '\'' +
                ", baudrate=" + baudrate +
                ", dataBits=" + dataBits +
                ", stopBits=" + stopBits +
                ", parity=" + parity +
                '}';
    }
}
