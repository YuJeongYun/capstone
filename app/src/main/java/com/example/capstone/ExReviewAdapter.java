package com.example.capstone;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class  ExReviewAdapter extends RecyclerView.Adapter<com.example.capstone.ExReviewAdapter.GalleryViewHolder> {

    private ArrayList<ReviewInfo> mDataset;
    private ArrayList<ReviewInfo> arrayList;
    private Activity activity;

    public static class GalleryViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;

        GalleryViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    public ExReviewAdapter(Activity activity, ArrayList<ReviewInfo> myDataset) {
        this.activity = activity;
        mDataset = myDataset;
        arrayList = new ArrayList<ReviewInfo>();
        arrayList.addAll(myDataset);
    }

    @NonNull
    @Override
    public com.example.capstone.ExReviewAdapter.GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView)LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ex_review, parent, false);

        final GalleryViewHolder galleryViewHolder = new GalleryViewHolder(cardView);

        return galleryViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder viewHolder, final int position) {
        CardView cardView = viewHolder.cardView;

        TextView locationTextView = cardView.findViewById(R.id.locationTextView5);
        locationTextView.setText(mDataset.get(position).getAddress_gu());

        TextView titleTextView = cardView.findViewById(R.id.titleTextView);
        titleTextView.setText(mDataset.get(position).getTitle());

        TextView contentTextView = cardView.findViewById(R.id.contentTextView);
        contentTextView.setText(mDataset.get(position).getContents());

        //TextView createdAtTextView = cardView.findViewById(R.id.createdAtTextView);
        //createdAtTextView.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(mDataset.get(position).getCreatedAt()));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}