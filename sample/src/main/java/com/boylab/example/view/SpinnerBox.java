package com.boylab.example.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.boylab.example.R;

import java.util.ArrayList;
import java.util.List;

public class SpinnerBox extends RelativeLayout {

    private CheckBox box_Label;
    private Spinner spinner;
    private OnItemSpinnerListener onItemSpinnerListener = null;

    public SpinnerBox(Context context) {
        this(context, null, 0);
    }

    public SpinnerBox(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpinnerBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_spinner_box, this);

        box_Label = findViewById(R.id.box_Label);
        spinner = findViewById(R.id.spinner);
        box_Label.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                spinner.setEnabled(!isChecked);
                if (buttonView.getId() == R.id.box_Label){
                    int position = spinner.getSelectedItemPosition();
                    if (onItemSpinnerListener != null){
                        onItemSpinnerListener.onItemSelect(SpinnerBox.this, position, isChecked);
                    }
                }
            }
        });
    }

    /**
     * 设置label
     * @param label
     * @param ems
     */
    public void setLabel(String label, int ems){
        box_Label.setText(label);
        box_Label.setEms(ems);
    }

    /**
     * 弹窗数据
     */
    public void setSpinner(List<String> data, OnItemSpinnerListener onItemSpinnerListener){
        this.onItemSpinnerListener = onItemSpinnerListener;
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

    public interface OnItemSpinnerListener{
        void onItemSelect(View view, int position, boolean isChecked);
    }

}
