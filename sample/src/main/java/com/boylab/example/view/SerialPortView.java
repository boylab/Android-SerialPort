package com.boylab.example.view;

import android.content.Context;
import android.serialport.SerialBean;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import com.boylab.example.Config;
import com.boylab.example.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SerialPortView extends RelativeLayout implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private SpinnerList serialSpinner, baudSpinner, dataBitSpinner, paritySpinner, stopBitSpinner;
    private Button btn_Open;
    private CheckBox box_State;

    private List<String> serialport = new ArrayList<>();
    private OnSerialOpenListener onSerialOpenListener = null;
    private SerialBean serialBean = new SerialBean();

    public SerialPortView(Context context) {
        this(context, null, 0);
    }

    public SerialPortView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SerialPortView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_serial_port, this);

        serialSpinner = findViewById(R.id.serialSpinner);
        baudSpinner = findViewById(R.id.baudSpinner);
        dataBitSpinner = findViewById(R.id.dataBitSpinner);
        paritySpinner = findViewById(R.id.paritySpinner);
        stopBitSpinner = findViewById(R.id.stopBitSpinner);
        btn_Open = findViewById(R.id.btn_Open);
        box_State = findViewById(R.id.box_State);

        serialSpinner.setLabel("端口", 5);
        baudSpinner.setLabel("波特率", 5);
        dataBitSpinner.setLabel("数据位", 5);
        paritySpinner.setLabel("校验位", 5);
        stopBitSpinner.setLabel("停止位", 5);

        baudSpinner.setSpinner(Config.BAUD);
        baudSpinner.setSelection(4);    //初始化9600
        dataBitSpinner.setSpinner(Config.DataBit);
        paritySpinner.setSpinner(Config.PARITY);
        stopBitSpinner.setSpinner(Config.StopBit);
        box_State.setOnCheckedChangeListener(this);
        btn_Open.setOnClickListener(this);
    }

    public void setSerialPort(List<String> serialport, OnSerialOpenListener onSerialOpenListener){
        this.serialport.clear();
        this.serialport.addAll(serialport);
        this.onSerialOpenListener = onSerialOpenListener;
        serialSpinner.setSpinner(serialport);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.box_State){
            btn_Open.setText(isChecked ? "关闭串口" : "打开串口");
            serialSpinner.setEnabled(!isChecked);
            baudSpinner.setEnabled(!isChecked);
            dataBitSpinner.setEnabled(!isChecked);
            paritySpinner.setEnabled(!isChecked);
            stopBitSpinner.setEnabled(!isChecked);
            //btn_Open.setTextColor(isChecked ? "关闭串口" : "打开串口");
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_Open){
            boolean checked = box_State.isChecked();
            if (!checked){
                /**
                 * 开串口操作
                 */
                int serial = serialSpinner.getSelection();
                int baud = baudSpinner.getSelection();
                int dataBit = dataBitSpinner.getSelection();
                int parity = paritySpinner.getSelection();
                int stopBit = stopBitSpinner.getSelection();

                serialBean.setDevicePath(serialport.get(serial));
                serialBean.setBaudrate(Integer.parseInt(Config.BAUD.get(baud)));
                serialBean.setDataBits(Integer.parseInt(Config.DataBit.get(dataBit)));
                String key = Config.PARITY.get(parity);
                serialBean.setParity(Config.PARITY_KEY.get(key));
                serialBean.setStopBits(Integer.parseInt(Config.StopBit.get(stopBit)));
            }

            box_State.setChecked(!checked);
            if (onSerialOpenListener != null){
                onSerialOpenListener.onSerialOpen(serialBean, !checked);
            }
        }
    }

    public void setSerial(HashMap<String, Integer> serial) {
        serialSpinner.setSelection(serial.get("port"));
        baudSpinner.setSelection(serial.get("baud"));
        dataBitSpinner.setSelection(serial.get("databit"));
        paritySpinner.setSelection(serial.get("parity"));
        stopBitSpinner.setSelection(serial.get("stopbit"));
    }

    public HashMap<String, Integer> getSerial() {
        HashMap<String, Integer> serial = new HashMap<>();
        serial.put("port", serialSpinner.getSelection());
        serial.put("baud", baudSpinner.getSelection());
        serial.put("databit", dataBitSpinner.getSelection());
        serial.put("parity", paritySpinner.getSelection());
        serial.put("stopbit", stopBitSpinner.getSelection());
        return serial;
    }

    public interface OnSerialOpenListener{
        void onSerialOpen(SerialBean serialBean, boolean openSerial);
    }
}
