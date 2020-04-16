package com.app.plantmonitor;

import android.annotation.SuppressLint;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyYAxisValueFormatter extends ValueFormatter {

    @Override
    @SuppressLint("SimpleDateFormat")
    public String getFormattedValue(float value) {
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd HH:mm:ss");
        Date date = new Date((long) value);

        return "4/" + outputFormat.format(date);
    }
}
