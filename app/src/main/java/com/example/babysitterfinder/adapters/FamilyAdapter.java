package com.example.babysitterfinder.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.babysitterfinder.R;
import com.example.babysitterfinder.models.Babysitter;
import com.example.babysitterfinder.models.Family;

import java.util.List;

public class FamilyAdapter extends RecyclerView.Adapter<FamilyAdapter.FamilyViewHolder> {

    private final List<Family> familyList;
    private final Context context;

    private OnFamilyClickListener clickListener;


    public interface OnFamilyClickListener {
        void onClick(String familyId);
    }
    public FamilyAdapter(List<Family> familyList, Context context, OnFamilyClickListener familyClickListener) {
        this.familyList = familyList;
        this.context = context;
        this.clickListener = familyClickListener;
    }

    @NonNull
    @Override
    public FamilyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_family, parent,false);
        return new FamilyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FamilyViewHolder familyViewHolder, int position) {
        Family family = familyList.get(position);
        familyViewHolder.familyName.setText(family.getFamilyName() != null ? family.getFamilyName() : "N/A");
        familyViewHolder.familyLocation.setText(family.getLocation() != null ? family.getLocation() : "N/A");
        familyViewHolder.familyNumOfChildren.setText(String.valueOf(family.getNumOfChildren()));

        familyViewHolder.itemView.setOnClickListener(view -> {
            String familyId = family.getFirestoreDocumentId();
            clickListener.onClick(familyId);
        });

    }

    @Override
    public int getItemCount() {
        return familyList.size();
    }



    public static class FamilyViewHolder extends RecyclerView.ViewHolder {
        TextView familyName, familyLocation, familyNumOfChildren;

        public FamilyViewHolder(@NonNull View itemView) {
            super(itemView);
            familyName = itemView.findViewById(R.id.textViewFamilyName);
            familyLocation = itemView.findViewById(R.id.textViewFamilyLocation);
            familyNumOfChildren = itemView.findViewById(R.id.textViewFamilyNumOfChildren);
        }
    }
}
