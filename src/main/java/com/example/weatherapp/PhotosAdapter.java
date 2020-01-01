package com.example.weatherapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import java.util.List;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.ViewHolder> {

    private List<PhotoItem> photoItemsList;
    private Context context;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View photoView = inflater.inflate(R.layout.photo_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(photoView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        PhotoItem photoItem = photoItemsList.get(i);
        ImageView cityImageView =  viewHolder.photoView;
        //fetch image from Picasso
        Picasso.with(context).load(photoItem.getPhotoUrl()).fit().into(cityImageView);
//        Glide.with(context)
//                .load(photoItem.getPhotoUrl())
//                .into(cityImageView);
    }

    @Override
    public int getItemCount() {
        return photoItemsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView photoView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            photoView = (ImageView) itemView.findViewById(R.id.cityPhotosView);
        }
    }

    public PhotosAdapter(Context context, List<PhotoItem> photoItemsList) {
        this.photoItemsList = photoItemsList;
        this.context = context;
    }

}
