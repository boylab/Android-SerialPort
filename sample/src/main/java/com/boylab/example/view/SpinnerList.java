package com.boylab.example.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.boylab.example.R;

import java.util.List;

public class SpinnerList extends RelativeLayout {

    private TextView text_Label;
    private Spinner spinner;

    public SpinnerList(Context context) {
        this(context, null, 0);
    }

    public SpinnerList(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpinnerList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_spinner_list, this);

        text_Label = findViewById(R.id.text_Label);
        spinner = findViewById(R.id.spinner);
    }

    /**
     * 设置label
     * @param label
     * @param ems
     */
    public void setLabel(String label, int ems){
        text_Label.setText(label);
        text_Label.setEms(ems);
    }

    /**
     * 弹窗数据
     */
    public void setSpinner(List<String> data){
        ArrayAdapter adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void setSelection(int position){
        spinner.setSelection(position);
    }

    public int getSelection(){
        return spinner.getSelectedItemPosition();
    }

}
