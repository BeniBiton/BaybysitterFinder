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

    public BabysitterAdapter(List<Babysitter> babysitterList, Context context){
        this.babysitterList = babysitterList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.item_babysitter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Babysitter babysitter = babysitterList.get(position);
        holder.babysitterName.setText(babysitter.getName());
        holder.babysitterDescription.setText(babysitter.getBio());

        Glide.with(context)
                .load(babysitter.getProfilePictureUrl())
                .into(holder.babysitterImage);
    }

    @Override
    public int getItemCount() {
        return babysitterList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView babysitterName, babysitterDescription;
        ImageView babysitterImage;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            babysitterName = itemView.findViewById(R.id.babysitterName);
            babysitterDescription = itemView.findViewById(R.id.babysitterDescription);
            babysitterImage = itemView.findViewById(R.id.babysitterImage);
        }
    }
}
