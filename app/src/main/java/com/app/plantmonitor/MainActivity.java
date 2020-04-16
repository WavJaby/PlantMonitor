package com.app.plantmonitor;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.database.*;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private boolean status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button button = findViewById(R.id.watering_button);
        final TextView textView = findViewById(R.id.textView);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status = !status;
                mDatabase.child("switch").setValue(status);
                button.setText(String.valueOf(status));
                System.out.println(status);
//                System.out.println(mDatabase.child("status").toString());
            }
        });

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("status");

        // Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (String.valueOf(dataSnapshot.getValue()).equals("true")) {
                    textView.setText("ON");
                } else {
                    textView.setText("OFF");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }
}
