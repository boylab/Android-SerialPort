package android.serialport;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 端口参数
 */
public class PortBean implements Parcelable {

    public static final char PARITY_NONE = 'n', PARITY_ODD = 'o', PARITY_EVEN = 'e';

    private String path = "/dev/ttyMT0";
    private int speed = 9600;
    private int dataBits = 8;
    private int stopBits = 1;
    private char parity = 'n';   //'n':无校验;  'o':奇校验;  'e':偶校验;

    public PortBean() {
    }

    public PortBean(String path, int speed, char parity) {
        this(path, speed, 8, 1, parity);
    }

    public PortBean(String path, int speed, int dataBits, int stopBits, char parity) {
        this.path = path;
        this.speed = speed;
        this.dataBits = dataBits;
        this.stopBits = stopBits;
        this.parity = parity;
    }

    public PortBean(String path) {
        this.path = path;
    }

    protected PortBean(Parcel in) {
        path = in.readString();
        speed = in.readInt();
        dataBits = in.readInt();
        stopBits = in.readInt();
        parity = (char) in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path);
        dest.writeInt(speed);
        dest.writeInt(dataBits);
        dest.writeInt(stopBits);
        dest.writeInt((int) parity);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PortBean> CREATOR = new Creator<PortBean>() {
        @Override
        public PortBean createFromParcel(Parcel in) {
            return new PortBean(in);
        }

        @Override
        public PortBean[] newArray(int size) {
            return new PortBean[size];
        }
    };

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
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

    public char getParity() {
        return parity;
    }

    /***
     * 'n':无校验;  'o':奇校验;  'e':偶校验;
     * @return
     */
    public int getParityInt() {
        if (this.parity == 'n'){
            return 0;
        }else if (this.parity == 'o'){
            return 1;
        }else if (this.parity == 'e'){
            return 2;
        }
        return 0;
    }

    public void setParity(char parity) {
        this.parity = parity;
    }

    @Override
    public String toString() {
        return "PortBean{" +
                "path='" + path + '\'' +
                ", speed=" + speed +
                ", dataBits=" + dataBits +
                ", stopBits=" + stopBits +
                ", parity=" + parity +
                '}';
    }
}
