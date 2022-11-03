/*
 * Copyright 2009 Cedric Priscal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.serialport;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class SerialPort {

    private static final String TAG = "SerialPort";

    public static final String SU_PATH = "/system/bin/su";
    private static String mSuPath = SU_PATH;

    private File device;
    private int baudrate;
    private int dataBits;
    private int parity;
    private int stopBits;
    //private int flags;

    /**
     * Set the su binary path, the default su binary path is {@link #SU_PATH}
     *
     * @param suPath su binary path
     */
    public static void setSuPath(@Nullable String suPath) {
        mSuPath = (suPath == null ? SU_PATH : suPath);
    }

    /*
     * Do not remove or rename the field mFd: it is used by native method close();
     */
    private FileDescriptor mFd;
    private FileInputStream mFileInputStream;
    private FileOutputStream mFileOutputStream;

    /**
     * 串口
     *
     * @param device   串口设备文件
     * @param baudrate 波特率
     * @param dataBits 数据位；默认8,可选值为5~8
     * @param parity   奇偶校验；0:无校验位(NONE，默认)；1:奇校验位(ODD);2:偶校验位(EVEN)
     * @param stopBits 停止位；默认1；1:1位停止位；2:2位停止位
     * @throws SecurityException
     * @throws IOException
     */
    public SerialPort(@NonNull File device, int baudrate, int dataBits, int parity, int stopBits) throws SecurityException, IOException {
        this.device = device;
        this.baudrate = baudrate;
        this.dataBits = dataBits;
        this.parity = parity;
        this.stopBits = stopBits;

        /* Check access permission */
        if (!device.canRead() || !device.canWrite()) {
            try {
                /* Missing read/write permission, trying to chmod the file */
                Process su;
                su = Runtime.getRuntime().exec(mSuPath);
                String cmd = "chmod 666 " + device.getAbsolutePath() + "\n" + "exit\n";
                su.getOutputStream().write(cmd.getBytes());
                if ((su.waitFor() != 0) || !device.canRead() || !device.canWrite()) {
                    throw new SecurityException();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new SecurityException();
            }
        }

        mFd = open(device.getAbsolutePath(), baudrate, dataBits, parity, stopBits);
        if (mFd == null) {
            Log.e(TAG, "native open returns null");
            throw new IOException();
        }
        mFileInputStream = new FileInputStream(mFd);
        mFileOutputStream = new FileOutputStream(mFd);
    }

    /**
     * 串口(默认8位数据位，1位停止位)
     *
     * @param device   串口设备文件
     * @param baudrate 波特率
     * @param parity   奇偶校验；0:无校验位(NONE，默认)；1:奇校验位(ODD);2:偶校验位(EVEN)
     * @throws SecurityException
     * @throws IOException
     */
    public SerialPort(@NonNull File device, int baudrate, int parity) throws SecurityException, IOException {
        this(device, baudrate, 8, parity, 1);
    }

    // Getters and setters
    @NonNull
    public InputStream getInputStream() {
        return mFileInputStream;
    }

    @NonNull
    public OutputStream getOutputStream() {
        return mFileOutputStream;
    }

    /**
     * 串口设备文件
     */
    @NonNull
    public File getDevice() {
        return device;
    }

    /**
     * 波特率
     */
    public int getBaudrate() {
        return baudrate;
    }

    /**
     * 数据位；默认8,可选值为5~8
     */
    public int getDataBits() {
        return dataBits;
    }

    /**
     * 奇偶校验；0:无校验位(NONE，默认)；1:奇校验位(ODD);2:偶校验位(EVEN)
     */
    public int getParity() {
        return parity;
    }

    /**
     * 停止位；默认1；1:1位停止位；2:2位停止位
     */
    public int getStopBits() {
        return stopBits;
    }

    // JNI
    private native FileDescriptor open(String absolutePath, int baudrate, int dataBits, int parity, int stopBits);

    public native void close();

    /**
     * 关闭流和串口，已经try-catch
     */
    public void tryClose() {
        try {
            mFileInputStream.close();
        } catch (IOException e) {
            //e.printStackTrace();
        }

        try {
            mFileOutputStream.close();
        } catch (IOException e) {
            //e.printStackTrace();
        }

        try {
            close();
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    static {
        System.loadLibrary("serial_port");
    }

}
