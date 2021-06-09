package com.example.capstone;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.Date;

public class DetailedReviewActivity extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private String name, title, contents, address_gu, uid, commentUid, commentName, comment;
    private ArrayList<String> post = new ArrayList<>();

    private LinearLayout contentLayout;
    private TextView titleTextView, userNameTextView, contentsTextView, locationTextView;
    private EditText commentEditText;
    private ImageButton commentButton, refreshButton;
    private RecyclerView recyclerView;
    private ImageView userImageView2;
    private VideoView videoView;

    InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_review);

        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        titleTextView = findViewById(R.id.titleTextView2);
        userNameTextView = findViewById(R.id.userNameTextView2);
        contentsTextView = findViewById(R.id.contentsTextView2);
        locationTextView = findViewById(R.id.locationTextView4);

        contentLayout = findViewById(R.id.contentsLayout3);
        userImageView2 = findViewById(R.id.userImageView2);
        commentButton = findViewById(R.id.commentButton);
        commentEditText = findViewById(R.id.commentEditText);
        refreshButton = findViewById(R.id.refreshButton);

        commentButton.setOnClickListener(onClickListener);
        refreshButton.setOnClickListener(onClickListener);

        recyclerView = findViewById(R.id.recyclerView2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(DetailedReviewActivity.this));

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        name = intent.getStringExtra("name");
        contents = intent.getStringExtra("contents");
        address_gu = intent.getStringExtra("address_gu");
        post = (ArrayList<String>) intent.getSerializableExtra("post");
        uid = intent.getStringExtra("uid");

        titleTextView.setText(title);
        userNameTextView.setText(name);
        contentsTextView.setText(contents);
        locationTextView.setText(address_gu);

        //DB에 저장된 사용자 정보 가져옴
        db.collection("users").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    commentName = documentSnapshot.getString("name");
                }
                else {
                    Log.d("error", "error");
                }
            }
        });

        //Storage에 저장된 사용자 사진 가져옴
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://capstonedesign-d1ced.appspot.com/");
        StorageReference storageReference = storage.getReference();
        storageReference.child("users/" + uid + "/profileImage.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext())
                        .load(uri)
                        .centerCrop()
                        .override(500)
                        .into(userImageView2);
                Log.e("profileImage", getApplicationContext().toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                userImageView2.setImageResource(R.drawable.ic_baseline_person_24);
            }
        });

        commentLoad();

        //Storage에 저장된 게시글의 동영상 및 사진 가져옴
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for(int i = 0; i < post.size(); i ++) {
            String content = post.get(i);

            if(post.get(i).contains(".mp4")) {
                Log.e("PostType : " , post.get(i)) ;

                Uri videoUri = Uri.parse(post.get(i));

                videoView = findViewById(R.id.videoView);
                videoView.setVisibility(View.VISIBLE);
                videoView.setMediaController(new MediaController(this));
                videoView.setVideoURI(videoUri);

                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        videoView.start();
                    }
                });
            } else {
                ImageView imageView = new ImageView(this);
                TextView textView = new TextView(this);
                imageView.setLayoutParams(layoutParams);
                contentLayout.addView(imageView);
                contentLayout.addView(textView);
                Glide.with(this).load(content).into(imageView);
            }
        }
    }

    //비디오 일시 정지
    @Override
    protected void onPause() {
        super.onPause();

        if(videoView!=null && videoView.isPlaying()) videoView.pause();
    }

    //액티비티가 메모리에서 사라질 때 호출
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(videoView!=null) videoView.stopPlayback();
    }

    //onClickListener
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.commentButton :
                    commentUpload(); //DB에 댓글 추가
                    commentLoad();
                    imm.hideSoftInputFromWindow(commentEditText.getWindowToken(), 0);
                    break;
                case R.id.refreshButton:
                    commentLoad(); //DB에서 댓글 가져옴
                    break;
            }
        }
    };

    //DB에 작성된 댓글 추가
    public void commentUpload() {
        commentUid = user.getUid();
        comment = commentEditText.getText().toString();

        if (comment.length() > 0) {
            CommentInfo commentInfo = new CommentInfo(commentUid, commentName, comment, new Date());
            Log.e("commentName", commentName);

            if (user != null) {
                db.collection("reviews").document(title).collection("comments").document().set(commentInfo) //db.collection("comments").document().set(commentInfo) 5/5 변경 //최종수정
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                startToast("댓글 등록을 성공하였습니다.");
                                commentEditText.setText("");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                startToast("댓글 등록에 실패하였습니다.");
                            }
                        });
            }
        } else {
            startToast("댓글 내용을 입력해주세요");
        }
    }

    //DB에서 해당 게시글의 댓글 가져옴
    public void commentLoad(){
        if(user != null) {
            CollectionReference colRef = db.collection("reviews").document(title).collection("comments");
            colRef.orderBy("createdAt", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        ArrayList<CommentInfo> commentList = new ArrayList<>();

                        for(QueryDocumentSnapshot document : task.getResult()) {
                            commentList.add(new CommentInfo(
                                    document.getData().get("uid").toString(),
                                    document.getData().get("user_name").toString(),
                                    document.getData().get("comment").toString(),
                                    new Date(document.getDate("createdAt").getTime()))
                            );
                        }
                        RecyclerView.Adapter mAdapter = new CommentAdapter(commentList);
                        recyclerView.setAdapter(mAdapter);
                    } else {
                        Log.e("Error", "error");
                    }
                }
            });
        }
    }

    //토스트 메시지
    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    //다른 액티비티 실행
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