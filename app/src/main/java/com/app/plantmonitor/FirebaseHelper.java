package com.app.plantmonitor;

import androidx.annotation.NonNull;
import com.github.mikephil.charting.data.Entry;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.SimpleFormatter;

public class FirebaseHelper {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceData;

    public interface DataStatus {
        void DataIsLoaded(Integer mode, List<List<Entry>> data);

        void WateringState(String state);
    }

    public FirebaseHelper(String path) {
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceData = mDatabase.getReference(path);
    }

    public void dataChangeListener(final DataStatus dataStatus) {
        mReferenceData.child("sensor").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Entry> soilMoistureValue = new ArrayList<>();
                List<Entry> humidityValue = new ArrayList<>();
                List<Entry> lightSensorValue = new ArrayList<>();
                List<Entry> temperatureValue = new ArrayList<>();
                SimpleDateFormat dateFmt = new SimpleDateFormat("ddHHmmss");
                for (DataSnapshot keyDay : dataSnapshot.getChildren()) {//天
                    for (DataSnapshot keyTime : keyDay.getChildren()) {//時間
                        long dataValue = 0;
                        try {
                            Date key = dateFmt.parse(keyDay.getKey().substring(6) + keyTime.getKey());//把時間轉成Date(String)
                            dataValue = key.getTime();//把Date轉成Sec
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        soilMoistureValue.add(new Entry(dataValue, keyTime.getValue(DataObject.class).getSoilMoisture()));
                        humidityValue.add(new Entry(dataValue, keyTime.getValue(DataObject.class).getHumidity()));
                        lightSensorValue.add(new Entry(dataValue, keyTime.getValue(DataObject.class).getLightSensor()));
                        temperatureValue.add(new Entry(dataValue, keyTime.getValue(DataObject.class).getTemperature()));
                    }
                }
                List<List<Entry>> data = new ArrayList<>();
                data.add(soilMoistureValue);
                data.add(lightSensorValue);
                data.add(temperatureValue);
                data.add(humidityValue);
                dataStatus.DataIsLoaded(0, data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void WateringStateChangeListener(final DataStatus dataStatus) {
        mReferenceData.child("wateringState").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataStatus.WateringState(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void watering(Boolean state) {
        mReferenceData.child("watering").setValue(state);
    }

//    public void addData(Map<String, Integer> data, final DataStatus dataStatus) {
//        String key = mReferenceData.push().getKey();
//        mReferenceData.child(key).setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                dataStatus.DataIsInserted();
//            }
//        });
//    }
//
//    public void updateData(String key, Map<String, Integer> data, final DataStatus dataStatus) {
//        mReferenceData.child(key).setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                dataStatus.DataIsUpdateed();
//            }
//        });
//    }
//
//    public void deleteData(String key, final DataStatus dataStatus) {
//        mReferenceData.child(key).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                dataStatus.DataIsDeleted();
//            }
//        });
//    }
}
