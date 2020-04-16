package com.app.plantmonitor;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.*;

public class MPAndroidChart extends AppCompatActivity implements FirebaseHelper.DataStatus {
    LineChart mpLineChart;

    ArrayList<ILineDataSet> ilineDataSets = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mp_android_chart);
        mpLineChart = findViewById(R.id.chart);//圖表

        lineChartSetup();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String deviceID = prefs.getString("pref_device_id", "");
        System.out.println(deviceID);

        FirebaseHelper light = new FirebaseHelper(deviceID + "/sensor");
        light.readData(this);
    }

    @Override
    public void DataIsLoaded(Integer mode, List<List<Entry>> data) {
        showChart("土壤濕度", 0, Color.BLACK, data);
        showChart("濕度", 1, Color.BLUE, data);
        showChart("亮度", 2, Color.CYAN, data);
        showChart("溫度", 3, Color.GREEN, data);
    }

    private void lineChartSetup() {
        Description description = new Description();
        description.setText("監測盒數據");//描述文字
        description.setTextSize(12);
        mpLineChart.setDescription(description);//設定圖表描述

        XAxis xAxis = mpLineChart.getXAxis();//取得X軸
        YAxis yAxis = mpLineChart.getAxisLeft();//取得Y軸
        yAxis.setAxisMinimum(0);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//把資料名稱位置改到下面
        mpLineChart.getAxisRight().setEnabled(false);//隱藏右邊的Y軸

        xAxis.setValueFormatter(new MyYAxisValueFormatter());//資料format
    }

    private void showChart(String mane, int index, int color, List<List<Entry>> dataValues) {

        LineDataSet lineDataSet = new LineDataSet(dataValues.get(index), mane);//設定線段的 數值,名字
        lineDataSet.setColor(color);
        lineDataSet.setCircleColor(color);

        try {
            ilineDataSets.set(index, lineDataSet);//設定線段 (這是處存所有線段的地方)
        } catch (Exception e) {
            ilineDataSets.add(index, lineDataSet);//增加線段 (這是處存所有線段的地方)
        }

        LineData lineData = new LineData(ilineDataSets);
        mpLineChart.setData(lineData);
        mpLineChart.invalidate();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tools, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting_button:
                startActivity(new Intent(this, SettingsActivity.class));//開啟設定
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void DataIsInserted() {

    }

    @Override
    public void DataIsUpdateed() {

    }

    @Override
    public void DataIsDeleted() {

    }
}
