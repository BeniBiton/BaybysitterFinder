package com.example.babysitterfinder.services;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseStorageService {

    private static final int IMAGE_PICKER_REQUEST = 1000;
    private static final StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    public static void pickImage(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        activity.startActivityForResult(intent, IMAGE_PICKER_REQUEST);
    }

    public static void uploadProfilePicture(Activity activity, Intent data, OnSuccessListener<Uri> callback) {
        if (data == null || data.getData() == null) {
            Toast.makeText(activity, "No image selected", Toast.LENGTH_SHORT).show();
            return;
        }

        Uri fileUri = data.getData();
        String fileName = "profile_pictures/" + System.currentTimeMillis() + ".jpg";
        StorageReference fileRef = storageReference.child(fileName);

        fileRef.putFile(fileUri).addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(callback).addOnFailureListener(e -> {
            Log.e("FirebaseStorageService", "Failed to get download URL", e);
            Toast.makeText(activity, "Image upload failed", Toast.LENGTH_SHORT).show();
        }));
    }

    public static Uri handleActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == IMAGE_PICKER_REQUEST && resultCode == Activity.RESULT_OK && data != null){
            return data.getData();
        }
        return null;
    }
}
