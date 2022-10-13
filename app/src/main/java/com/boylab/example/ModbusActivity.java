package com.boylab.example;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.boylab.example.modbus.SerialMaster;
import com.serotonin.modbus4j.msg.ReadInputRegistersResponse;

public class ModbusActivity extends AppCompatActivity {

    private SerialMaster serialMaster = SerialMaster.newInstance();

    private TextView text_Receive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modbus);
        text_Receive  = findViewById(R.id.text_Receive);

        findViewById(R.id.btn_Init).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                serialMaster.setDebug(true);
                serialMaster.startSerial();
            }
        });

        findViewById(R.id.btn_ReadWeigh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReadInputRegistersResponse registersResponse = serialMaster.readInputRegisters(1, 0, 4);
                if (registersResponse == null){
                    Log.i("___boylab>>>___", "run: 收到空");
                    return;
                }
                byte[] data = registersResponse.getData();
                if (data != null)
                    for (int i = 0; i < data.length; i++) {
                        text_Receive.append(" ");
                        text_Receive.append(String.format("%2x", data[i]));
                    }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        serialMaster.stopSerial();
    }

}