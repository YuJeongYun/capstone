package com.example.capstone;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView recyclerView;
    ArrayList<ReviewInfo> reviewList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        if (user == null) {
            myStartActivity(LoginActivity.class); // 메인 -> 로그인 확인 후 로그인되어 있지 않으면 로그인으로 이동
        } else {
            DocumentReference docRef = db.collection("users").document(user.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null) {
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }
        findViewById(R.id.goToReviewTextView).setOnClickListener(onClickListener);
        findViewById(R.id.recommendButton).setOnClickListener(onClickListener);
    }
    protected void onResume() {
        super.onResume();

        if(user != null) {
            final RelativeLayout loaderLayout = findViewById(R.id.loaderLayout);
            loaderLayout.setVisibility(View.VISIBLE);
            CollectionReference colRef = db.collection("reviews");
            colRef.orderBy("createdAt", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()) {
                        reviewList = new ArrayList<>();
                        loaderLayout.setVisibility(View.GONE);
                        int i = 0;
                        for(QueryDocumentSnapshot document : task.getResult()) {
                            if(i == 3) break;
                            i++;
                            reviewList.add(new ReviewInfo(
                                    (String) document.getData().get("uid"),
                                    document.getData().get("name").toString(),
                                    document.getData().get("title").toString(),
                                    document.getData().get("contents").toString(),
                                    (ArrayList<String>) document.getData().get("post"),
                                    document.getData().get("address_gu").toString(),
                                    Math.toIntExact((Long) document.getData().get("likes")),
                                    new Date(document.getDate("createdAt").getTime())));
                        }
                        RecyclerView.Adapter nAdapter = new ExReviewAdapter(MainActivity.this, reviewList);
                        recyclerView.setAdapter(nAdapter);
                    } else{
                        Log.d(TAG, "Error getting documents : ", task.getException());
                    }
                }
            });
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.goToReviewTextView:
                    myStartActivity(ReviewActivity.class);
                    break;
                case R.id.floatingActionButton:
                    myStartActivity(WriteReviewActivity.class);
                    break;
                case R.id.recommendButton:
                    myStartActivity(RecommendActivity.class);
                    break;
            }
        }
    };

    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu:
                myStartActivity(InfoActivity.class);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}