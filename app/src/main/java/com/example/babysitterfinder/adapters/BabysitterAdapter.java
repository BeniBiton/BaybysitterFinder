package com.example.babysitterfinder.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.babysitterfinder.R;
import com.example.babysitterfinder.models.Babysitter;

import java.util.List;

public class BabysitterAdapter extends RecyclerView.Adapter<BabysitterAdapter.ViewHolder> {

    private List<Babysitter> babysitterList;
    private Context context;

    private OnBabysitterClickListener clickListener;

    public interface OnBabysitterClickListener {
        void onClick(String babysitterId);
    }

    public BabysitterAdapter(List<Babysitter> babysitterList, Context context, OnBabysitterClickListener clickListener) {
        this.babysitterList = babysitterList;
        this.context = context;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_babysitter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Babysitter babysitter = babysitterList.get(position);
        holder.babysitterName.setText(
                babysitter.getName() != null ? babysitter.getName() : "Name not available"
        );
        holder.babysitterDescription.setText(
                babysitter.getBio() != null ? babysitter.getBio() : "Bio not available"
        );

        String imageUrl = babysitter.getProfilePictureUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(context)
                    .load(imageUrl)
                    .centerCrop()
                    .placeholder(R.drawable.ic_profile_placeholder)
                    .error(R.drawable.ic_profile_placeholder)
                    .into(holder.babysitterImage);
        } else {
            Glide.with(context)
                    .load(R.drawable.ic_profile_placeholder)
                    .into(holder.babysitterImage);
        }

        holder.itemView.setOnClickListener(view -> {
            String babysitterId = babysitter.getFirestoreDocumentId();
            clickListener.onClick(babysitterId);
        });


        holder.itemView.setOnClickListener(view -> {
            String babysitterId = babysitter.getFirestoreDocumentId();
            clickListener.onClick(babysitterId);
        });
    }

    @Override
    public int getItemCount() {
        return babysitterList.size();
    }

    public void updateList(List<Babysitter> newList) {
        babysitterList.clear();
        babysitterList.addAll(newList);
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView babysitterName, babysitterDescription;
        ImageView babysitterImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            babysitterName = itemView.findViewById(R.id.babysitterName);
            babysitterDescription = itemView.findViewById(R.id.babysitterDescription);
            babysitterImage = itemView.findViewById(R.id.babysitterImage);
        }
    }
}
