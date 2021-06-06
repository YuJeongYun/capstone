package com.example.capstone;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Date;

public class ReviewActivity extends AppCompatActivity {

    private static final String TAG = "ReviewActivity";

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView recyclerView;
    EditText searchEditText;
    ArrayList<ReviewInfo> reviewList;
    RelativeLayout loaderLayout;
    CardView cardView;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        findViewById(R.id.floatingActionButton).setOnClickListener(onClickListener);
        findViewById(R.id.searchButton).setOnClickListener(onClickListener);
        findViewById(R.id.searchFloatingButton).setOnClickListener(onClickListener);
        findViewById(R.id.menuFloatingButton).setOnClickListener(onClickListener);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ReviewActivity.this));

        searchEditText = findViewById(R.id.searchEditText);
        cardView = findViewById(R.id.searchCardView);

        loaderLayout = findViewById(R.id.loaderLayout);
        loaderLayout.setVisibility(View.VISIBLE);

        linearLayout = findViewById(R.id.menuFloatingLayout);
    }

    protected void onResume() {
        super.onResume();

        String searchText = searchEditText.getText().toString();

        if(user != null) {
            //DB에 저장된 사용자 및 리뷰 정보 가져옴(리뷰 게시판 글)
            CollectionReference colRef = db.collection("reviews");
            colRef.orderBy("createdAt", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()) {
                        reviewList = new ArrayList<>();

                        for(QueryDocumentSnapshot document : task.getResult()) {
                            reviewList.add(new ReviewInfo(
                                    (String) document.getData().get("uid"),
                                    document.getData().get("name").toString(),
                                    document.getData().get("title").toString(),
                                    document.getData().get("contents").toString(),
                                    (ArrayList<String>) document.getData().get("post"),
                                    document.getData().get("address_gu").toString(),
                                    new Date(document.getDate("createdAt").getTime())));
                        }

                        //검색한 지역에 해당하는 리뷰 게시글 보여줌
                        if(searchText.length() > 0) {
                            RecyclerView.Adapter mAdapter = new ReviewAdapter(ReviewActivity.this, reviewList, user.getUid());
                            recyclerView.setAdapter(mAdapter);
                            ((ReviewAdapter) mAdapter).filter(searchText);
                        }
                        //전체 리뷰 게시글 보여줌
                        else {
                            RecyclerView.Adapter mAdapter = new ReviewAdapter(ReviewActivity.this, reviewList, user.getUid());
                            recyclerView.setAdapter(mAdapter);
                        }

                    } else{
                        Log.d(TAG, "Error getting documents : ", task.getException());
                    }
                }
            });
            loaderLayout.setVisibility(View.GONE);
        }
    }

    //onClickListener
    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.floatingActionButton :
                    myStartActivity(WriteReviewActivity.class); //리뷰 작성 게시판으로 이동
                    break;

                case R.id.searchButton :
                    onResume();
                    break;

                case R.id.searchFloatingButton :
                    if(cardView.getVisibility() == View.GONE) {
                        cardView.setVisibility(View.VISIBLE);
                    } else {
                        cardView.setVisibility(View.GONE);
                    }

                    break;

                case R.id.menuFloatingButton :
                    if(linearLayout.getVisibility() == View.GONE) {
                        linearLayout.setVisibility(View.VISIBLE);
                    } else {
                        linearLayout.setVisibility(View.GONE);
                    }
                    break;

            }
        }
    };

    //토스트 메시지
    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    //다른 액티비티로 이동
    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

    //옵션메뉴
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
                myStartActivity(InfoActivity.class); //마이페이지로 이동
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}