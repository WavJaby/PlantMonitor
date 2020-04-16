package com.app.plantmonitor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
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

    List<List<Entry>> sensorData = new ArrayList<>();

    private boolean status;
    private Button button;
    private Spinner showMode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mp_android_chart);
        mpLineChart = findViewById(R.id.chart);//圖表

        lineChartSetup();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String deviceID = prefs.getString("pref_device_id", "");
        System.out.println(deviceID);

        final FirebaseHelper firebase = new FirebaseHelper(deviceID);
        firebase.dataChangeListener(this);
        firebase.WateringStateChangeListener(this);

        button = findViewById(R.id.watering_button);//澆水按鈕
        button.setText(String.valueOf(status));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status = !status;
                button.setText(getText(R.string.str_sending));
                firebase.watering(status);
            }
        });

        showMode = findViewById(R.id.show_mode);
        showMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!sensorData.isEmpty()) {
                    eraseChart();
                    switch (position) {
                        case 1:
                            showChart(getString(R.string.str_soil_moisture), 0, Color.BLACK);
                            return;
                        case 2:
                            showChart(getString(R.string.str_light_level), 1, Color.CYAN);
                            return;
                        case 3:
                            showChart(getString(R.string.str_temperature), 2, Color.GREEN);
                            return;
                        case 4:
                            showChart(getString(R.string.str_humidity), 3, Color.BLUE);
                            return;
                        default:
                            showChart(getString(R.string.str_soil_moisture), 0, Color.BLACK);
                            showChart(getString(R.string.str_light_level), 1, Color.CYAN);
                            showChart(getString(R.string.str_temperature), 2, Color.GREEN);
                            showChart(getString(R.string.str_humidity), 3, Color.BLUE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void DataIsLoaded(Integer mode, List<List<Entry>> data) {
        sensorData = data;
        switch (showMode.getSelectedItemPosition()) {
            case 1:
                showChart(getString(R.string.str_soil_moisture), 0, Color.BLACK);
                return;
            case 2:
                showChart(getString(R.string.str_light_level), 1, Color.CYAN);
                return;
            case 3:
                showChart(getString(R.string.str_temperature), 2, Color.GREEN);
                return;
            case 4:
                showChart(getString(R.string.str_humidity), 3, Color.BLUE);
                return;
            default:
                showChart(getString(R.string.str_soil_moisture), 0, Color.BLACK);
                showChart(getString(R.string.str_light_level), 1, Color.CYAN);
                showChart(getString(R.string.str_temperature), 2, Color.GREEN);
                showChart(getString(R.string.str_humidity), 3, Color.BLUE);
        }
    }

    @Override
    public void WateringState(String status) {
        button.setText(String.valueOf(status));
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

    private void eraseChart() {
        ilineDataSets.clear();
    }

    private void showChart(String mane, int index, int color) {
        LineDataSet lineDataSet = new LineDataSet(sensorData.get(index), mane);//設定線段的 數值,名字
        lineDataSet.setColor(color);
        lineDataSet.setCircleColor(color);

        try {
            ilineDataSets.set(index, lineDataSet);//設定線段 (這是處存所有線段的地方)
        } catch (Exception e) {
            ilineDataSets.add(lineDataSet);//增加線段 (這是處存所有線段的地方)
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
}
