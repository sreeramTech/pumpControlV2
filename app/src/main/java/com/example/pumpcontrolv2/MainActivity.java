package com.example.pumpcontrolv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.suke.widget.SwitchButton;

public class MainActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference statRef = database.getReference("munnar/121/pump1/status");
    DatabaseReference permitRef = database.getReference("munnar/121/pump1/permit");
    DatabaseReference commandRef = database.getReference("munnar/121/pump1/cmnd");
    TextView status;
    com.suke.widget.SwitchButton toggle;
    String stat;

    private String permission = "yes";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        status = findViewById(R.id.status);
        toggle = findViewById(R.id.onoffSwitch);

        permitRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                permission = dataSnapshot.getValue().toString();

                if(permission.equals("yes")){
                    isValid();
                }
                else {
                    Toast.makeText(getApplicationContext(),"PERMISSION DENIED",Toast.LENGTH_SHORT).show();
                    toggle.setVisibility(View.INVISIBLE);
                    status.setText("NA");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void isValid(){
        statRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                stat = dataSnapshot.getValue().toString();
                status.setText(stat);
                if (stat.equals("ON")){toggle.setChecked(true);}
                else{toggle.setChecked(false);}


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
       toggle.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(SwitchButton view, boolean isChecked) {
               if(isChecked){commandRef.setValue("ON");}
               else{commandRef.setValue("OFF");}
           }
       });

    }


}
