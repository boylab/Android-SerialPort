package com.boylab.example.view;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boylab.example.R;

import java.nio.ByteBuffer;

public class ReceiveView extends RelativeLayout {
    private Button btn_Clear;
    private TextView text_Rx, text_Receive;
    private CheckBox box_ShowHex;

    private boolean isHex = false;
    private ByteBuffer mBuffer = ByteBuffer.allocate(2048);
    private int rxNum = 0;

    public ReceiveView(Context context) {
        this(context, null, 0);
    }

    public ReceiveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReceiveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_receiver_view, this);

        btn_Clear = findViewById(R.id.btn_Clear);
        text_Rx = findViewById(R.id.text_Rx);
        box_ShowHex = findViewById(R.id.box_ShowHex);
        text_Receive = findViewById(R.id.text_Receive);
        text_Receive.setMovementMethod(ScrollingMovementMethod.getInstance());

        box_ShowHex.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isHex = isChecked;
            }
        });
        btn_Clear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btn_Clear){
                    clearReceive();
                }
            }
        });
    }

    public void addShow(byte[] data){
        int length = data.length;
        if (mBuffer.remaining() < length){
            mBuffer.clear();
            text_Receive.setText("");
        }

        mBuffer.put(data);
        String str = "";
        for (byte b: data) {
            str += (isHex ? String.format("%02x ", b) : String.format("%02d ", b));
        }
        text_Receive.append(str);

        rxNum = rxNum + length;
        text_Rx.setText(String.format("Rx: %d", rxNum));
    }

    /**
     * 清空数据
     */
    public void clearReceive(){
        mBuffer.clear();
        text_Receive.setText("");
        rxNum = 0;
        text_Rx.setText(String.format("Rx: %d", rxNum));
    }

    public void setHexShow(boolean isHexShow){
        box_ShowHex.setChecked(isHexShow);
    }

    public boolean isHexShow(){
        return box_ShowHex.isChecked();
    }

}
