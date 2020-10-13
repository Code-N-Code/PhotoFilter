package com.codencode.photofilter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {

    List<CircleImage> mList;
    Context context;
    ImageView mPlaceHolderImage;
    Bitmap image;
    int selected = 0;

    FilterAdapter(List<CircleImage> mList , Context context , ImageView mPlaceHolderImage , Bitmap image)
    {
        this.mList = mList;
        this.context = context;
        this.mPlaceHolderImage = mPlaceHolderImage;
        this.image = image;
    }

    void setImage(Bitmap image)
    {
        this.image = image;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.filter_item_layout , parent , false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.imageView.setImageBitmap(mList.get(position).getFilter().processFilter(mList.get(position).getImage()));

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected = position;
                mPlaceHolderImage.setImageBitmap(mList.get(position).getFilter().processFilter(
                        Bitmap.createScaledBitmap(image, 450, 600, false)
                ));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class  ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.filter_item_circleimage);
        }
    }
}
