package com.app.plantmonitor;

import android.graphics.Color;
import android.os.Bundle;
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

        FirebaseHelper light = new FirebaseHelper("sensor");
        light.readData("20200412", this);
    }

    @Override
    public void DataIsLoaded(Integer mode, List<List<Entry>> data) {
        showChart("土壤濕度", 0,Color.BLACK, data);
        showChart("濕度", 1,Color.BLUE, data);
        showChart("亮度", 2,Color.CYAN, data);
        showChart("溫度", 3,Color.GREEN, data);
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

        new List<String>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(@Nullable Object o) {
                return false;
            }

            @NonNull
            @Override
            public Iterator<String> iterator() {
                return null;
            }

            @NonNull
            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @NonNull
            @Override
            public <T> T[] toArray(@NonNull T[] a) {
                return null;
            }

            @Override
            public boolean add(String s) {
                return false;
            }

            @Override
            public boolean remove(@Nullable Object o) {
                return false;
            }

            @Override
            public boolean containsAll(@NonNull Collection<?> c) {
                return false;
            }

            @Override
            public boolean addAll(@NonNull Collection<? extends String> c) {
                return false;
            }

            @Override
            public boolean addAll(int index, @NonNull Collection<? extends String> c) {
                return false;
            }

            @Override
            public boolean removeAll(@NonNull Collection<?> c) {
                return false;
            }

            @Override
            public boolean retainAll(@NonNull Collection<?> c) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public boolean equals(@Nullable Object o) {
                return false;
            }

            @Override
            public int hashCode() {
                return 0;
            }

            @Override
            public String get(int index) {
                return null;
            }

            @Override
            public String set(int index, String element) {
                return null;
            }

            @Override
            public void add(int index, String element) {

            }

            @Override
            public String remove(int index) {
                return null;
            }

            @Override
            public int indexOf(@Nullable Object o) {
                return 0;
            }

            @Override
            public int lastIndexOf(@Nullable Object o) {
                return 0;
            }

            @NonNull
            @Override
            public ListIterator<String> listIterator() {
                return null;
            }

            @NonNull
            @Override
            public ListIterator<String> listIterator(int index) {
                return null;
            }

            @NonNull
            @Override
            public List<String> subList(int fromIndex, int toIndex) {
                return null;
            }
        };

        LineDataSet lineDataSet = new LineDataSet(dataValues.get(index), mane);//設定線段的 數值,名字
        lineDataSet.setColor(color);
        lineDataSet.setCircleColor(color);

        try{
            ilineDataSets.set(index,lineDataSet);//設定線段 (這是處存所有線段的地方)
        } catch (Exception e) {
            ilineDataSets.add(index, lineDataSet);//增加線段 (這是處存所有線段的地方)
        }

        LineData lineData = new LineData(ilineDataSets);
        mpLineChart.setData(lineData);
        mpLineChart.invalidate();

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
