package com.example.capstone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class  ResultActivity extends AppCompatActivity {
    private static final String TAG = "Warn";
    private TextView textView4, textView5, textView6;
    private Button button, button2;
    private Chip chip_1, chip_2, chip_3, chip_4, chip_5, chip_6, chip_7, chip_8, chip_9, chip_10, chip_11, chip_12, chip_13, chip_14
            , chip_15, chip_16, chip_17, chip_18;
    private float gp1=0 , gp2=0, gp3=0,gp4=0,gp5=0;
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Intent chartintent = new Intent(this, ChartActivity.class);
        Intent intent = getIntent();
        chip_1 = findViewById(R.id.chip1);
        chip_2 = findViewById(R.id.chip2);
        chip_3 = findViewById(R.id.chip3);
        chip_4 = findViewById(R.id.chip4);
        chip_5 = findViewById(R.id.chip5);
        chip_6 = findViewById(R.id.chip6);
        chip_7 = findViewById(R.id.chip7);
        chip_8 = findViewById(R.id.chip8);
        chip_9 = findViewById(R.id.chip9);
        chip_10 = findViewById(R.id.chip10);
        chip_11 = findViewById(R.id.chip11);
        chip_12 = findViewById(R.id.chip12);
        chip_13 = findViewById(R.id.chip13);
        chip_14 = findViewById(R.id.chip14);
        chip_15 = findViewById(R.id.chip15);
        chip_16 = findViewById(R.id.chip16);
        chip_17 = findViewById(R.id.chip17);
        chip_18 = findViewById(R.id.chip18);
        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> sel = (ArrayList<String>)  intent.getSerializableExtra("list");
        ArrayList<String> gu = (ArrayList<String>) intent.getSerializableExtra("gu");
        ArrayList<Float> l1 = new ArrayList<>();
        ArrayList<Float> avg = new ArrayList<>();
        ArrayList<Float> group1 = new ArrayList<>();
        ArrayList<Float> group2 = new ArrayList<>();
        ArrayList<Float> group3 = new ArrayList<>();
        ArrayList<Float> group4 = new ArrayList<>();
        ArrayList<Float> group5 = new ArrayList<>();
        ArrayList<Float> chip_1 = new ArrayList<>();
        ArrayList<Float> chip_2 = new ArrayList<>();
        ArrayList<Float> chip_3 = new ArrayList<>();
        ArrayList<Float> chip_4 = new ArrayList<>();
        ArrayList<Float> chip_5 = new ArrayList<>();
        ArrayList<Float> chip_6 = new ArrayList<>();
        ArrayList<Float> chip_7 = new ArrayList<>();
        ArrayList<Float> chip_8 = new ArrayList<>();
        ArrayList<Float> chip_9 = new ArrayList<>();
        ArrayList<Float> chip_10 = new ArrayList<>();
        ArrayList<Float> chip_11 = new ArrayList<>();
        ArrayList<Float> chip_12 = new ArrayList<>();
        ArrayList<Float> chip_13 = new ArrayList<>();
        ArrayList<Float> chip_14 = new ArrayList<>();
        ArrayList<Float> chip_15 = new ArrayList<>();
        ArrayList<Float> chip_16 = new ArrayList<>();
        ArrayList<Float> chip_17 = new ArrayList<>();
        ArrayList<Float> chip_18 = new ArrayList<>();
        String m1 = intent.getStringExtra("m1");
        String m2 = intent.getStringExtra("m2");
        String m3 = intent.getStringExtra("m3");
        list.add(m1);
        list.add(m2);
        list.add(m3);
        textView4 = findViewById(R.id.textView4);
        textView4.setText(m1);
        textView5 = findViewById(R.id.textView5);
        textView5.setText(m2);
        textView6 = findViewById(R.id.textView6);
        textView6.setText(m3);
        button = findViewById(R.id.button);
        db.collection("nomal")
                .orderBy("지역구")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int j=0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getMetadata() + " => " + document.getData());
                                //생활·편의·교통
                                chip_1.add(Float.parseFloat(document.get("편의시설 수").toString())); //편의시설 수
                                chip_2.add(Float.parseFloat(document.get("쇼핑시설 수").toString())); //쇼핑시설 수
                                chip_3.add(Float.parseFloat(document.get("외식시설 수").toString())); //외식시설 수
                                //교육
                                chip_4.add(Float.parseFloat(document.get("고등교육기관수").toString())); //고등교육 기관 수
                                chip_5.add(Float.parseFloat(document.get("학원 수").toString()));  //학원 수
                                //복지 문화
                                chip_6.add(Float.parseFloat(document.get("유치원 및 보육시설").toString())); //유치원 및 보육시설
                                chip_7.add(Float.parseFloat(document.get("병의원 및 약국").toString())); //병의원 및 약국
                                chip_8.add(Float.parseFloat(document.get("노인복지시설").toString())); //노인복지시설
                                chip_9.add(Float.parseFloat(document.get("사회복지시설").toString())); //사회복지시설
                                chip_10.add(Float.parseFloat(document.get("문화시설 수").toString())); //문화시설 수
                                chip_11.add(Float.parseFloat(document.get("체육시설 수").toString())); //체육시설 수
                                //자연
                                chip_12.add(Float.parseFloat(document.get("대기오염").toString())); //대기오염\
                                //안전
                                chip_13.add(Float.parseFloat(document.get("교통사고").toString())); //교통사고
                                chip_14.add(Float.parseFloat(document.get("화재").toString())); //화재
                                chip_15.add(Float.parseFloat(document.get("범죄").toString())); //범죄
                                chip_16.add(Float.parseFloat(document.get("생활안전").toString())); //생활안전
                                chip_17.add(Float.parseFloat(document.get("자살").toString())); //자살
                                chip_18.add(Float.parseFloat(document.get("감염병").toString())); //감염병

                            }


                            for (int i = 0; i < 25; i++) {
                                group1.add(chip_1.get(i) + chip_2.get(i) + chip_3.get(i));//생활·편의·교통 합
                                group2.add(chip_4.get(i) + chip_5.get(i));//교육 합
                                group3.add(chip_6.get(i) + chip_7.get(i) + chip_8.get(i) + chip_9.get(i) + chip_10.get(i) + chip_11.get(i));//복지 문화 합
                                group4.add(chip_12.get(i));//자연 합
                                group5.add(1-chip_13.get(i) + 1-chip_14.get(i) + 1-chip_15.get(i) + 1-chip_16.get(i) + 1-chip_17.get(i) + 1-chip_18.get(i));//안전 합
                            }

                            for(int i =0; i<25; i++){
                                gp1 = gp1 + group1.get(i);
                                gp2 = gp2 + group2.get(i);
                                gp3 = gp3 + group3.get(i);
                                gp4 = gp4 + group4.get(i);
                                gp5 = gp5 + group5.get(i);

                            }

                            avg.add(gp1/25);
                            avg.add(gp2/25);
                            avg.add(gp3/25);
                            avg.add(gp4/25);
                            avg.add(gp5/25);



                            int m1idx = gu.indexOf(m1);
                            l1.add(group1.get(m1idx));
                            l1.add(group2.get(m1idx));
                            l1.add(group3.get(m1idx));
                            l1.add(group4.get(m1idx));
                            l1.add(group5.get(m1idx));
                            int m2idx = gu.indexOf(m2);
                            l1.add(group1.get(m2idx));
                            l1.add(group2.get(m2idx));
                            l1.add(group3.get(m2idx));
                            l1.add(group4.get(m2idx));
                            l1.add(group5.get(m2idx));
                            int m3idx = gu.indexOf(m3);
                            l1.add(group1.get(m3idx));
                            l1.add(group2.get(m3idx));
                            l1.add(group3.get(m3idx));
                            l1.add(group4.get(m3idx));
                            l1.add(group5.get(m3idx));
                            for(int i=0; i<5;i++){
                                System.out.println(avg.get(i));
                                System.out.println(l1.get(i)+m1);
                            }


                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chartintent.putExtra("m1",m1);
                chartintent.putExtra("m2",m2);
                chartintent.putExtra("m3",m3);
                chartintent.putExtra("l1",l1);
                chartintent.putExtra("avg",avg);
                startActivity(chartintent);
            }
        });














    }


}
