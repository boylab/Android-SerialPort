package com.boylab.example.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.boylab.example.Config;
import com.boylab.example.R;
import com.boylab.example.utils.ViewFresher;

public class SendView extends RelativeLayout implements View.OnClickListener {
    private Button btn_SendData;
    private TextView text_Tx;
    private ImageView iv_Reset;
    private SpinnerBox box_AutoSend;
    private EditText input_Send;

    private boolean needParse = false;
    private byte[] sendData = null;
    private OnSendListener onSendListener = null;
    private int txNum = 0;

    public SendView(Context context) {
        this(context, null, 0);
    }

    public SendView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SendView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_send_view, this);
        btn_SendData = findViewById(R.id.btn_SendData);
        text_Tx = findViewById(R.id.text_Tx);
        iv_Reset = findViewById(R.id.iv_Reset);
        box_AutoSend = findViewById(R.id.box_AutoSend);
        input_Send = findViewById(R.id.input_Send);

        iv_Reset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                txNum = 0;
                text_Tx.setText(String.format("Tx: %d", txNum));
            }
        });

        box_AutoSend.setLabel("发送间隔", 6);
        box_AutoSend.setSpinner(Config.Delay, new SpinnerBox.OnItemSpinnerListener() {
            @Override
            public void onItemSelect(View view, int position, boolean isChecked) {
                btn_SendData.setClickable(!isChecked);
                btn_SendData.setEnabled(!isChecked);
                if (isChecked){
                    String ms = Config.Delay.get(position).replace("ms", "");
                    int mills = Integer.parseInt(ms);
                    /**
                     * 开启循环发送
                     */
                    parseSend();
                    ViewFresher.newInstance().addFresh(0x01, mills, new ViewFresher.OnViewFreshListener() {
                        @Override
                        public void onViewFresh() {
                            sendOnce();
                        }
                    });
                }else {
                    //关闭循环发送
                    ViewFresher.newInstance().remove(0x01);
                }
            }
        });
        box_AutoSend.setSelection(1);

        input_Send.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                needParse = true;
                sendData = null;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn_SendData.setOnClickListener(this);
    }

    public void setOnSendListener(OnSendListener onSendListener) {
        this.onSendListener = onSendListener;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_SendData){
            /**
             * 解析发送数据
             */
            parseSend();

            /***
             * 发送回调
             */
            sendOnce();
        }
    }

    /**
     * 解析发送的数据
     */
    private void parseSend(){
        if (needParse){
            needParse = false;
            String text = input_Send.getText().toString().trim().replaceAll(" ", "");
            int length = text.length();
            if (length % 2 != 0){
                Toast.makeText(getContext(), "请输入偶数个字符", Toast.LENGTH_SHORT).show();
                return;
            }
            int size = length / 2;
            sendData = new byte[size];
            for (int i = 0; i < size; i++) {
                sendData[i] = (byte) (Integer.parseInt(text.substring(i*2, i*2+2), 0x10) & 0xFF);
            }
        }
    }

    private void sendOnce(){
        if (onSendListener != null){
            txNum = txNum + sendData.length;
            text_Tx.setText(String.format("Tx: %d", txNum));
            onSendListener.onSend(sendData);
        }
    }

    public void setInputSend(String sendTxt){
        input_Send.setText(sendTxt);
    }

    public String getInputSend(){
        if (sendData == null){
            return "";
        }
        String sendTxt = "";
        for (byte b: sendData) {
            sendTxt += String.format("%02x ", b);
        }
        return sendTxt;
    }

    public interface OnSendListener{
        void onSend(byte[] data);
    }
}
