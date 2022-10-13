package com.boylab.example;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.serialport.SerialPortManager;
import android.serialport.PortBean;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private SerialPortManager serialPortManager = null;
    private TextView text_Receive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text_Receive  = findViewById(R.id.text_Receive);

        findViewById(R.id.btn_Init).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PortBean portBean = new PortBean("/dev/ttyMT2", 38400, PortBean.PARITY_EVEN);
                SerialPortManager.DEBUG = true;
                serialPortManager = new SerialPortManager(portBean, true);  //启动自动读数据

                serialPortManager.setOnAutoReadListener(new SerialPortManager.OnAutoReadListener() {
                    @Override
                    public void onAutoRead(byte[] bytes, int j) {
                        text_Receive.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                text_Receive.append("     数量"+j+"    ");
                                for (int i = 0; i < bytes.length; i++) {
                                    text_Receive.append(" ");
                                    text_Receive.append(String.format("%2x", bytes[i]));
                                }
                            }
                        }, 0);
                    }
                });
            }
        });

        findViewById(R.id.btn_ReadWeigh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                byte[] bytes = new byte[]{0x01, 0x04, 0x00, 0x00, 0x00, 0x02, 0x71, (byte) 0xCB};
                serialPortManager.write(bytes);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        serialPortManager.close();
    }
}