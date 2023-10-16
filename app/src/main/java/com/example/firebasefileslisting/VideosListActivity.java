package com.example.firebasefileslisting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Objects;

public class VideosListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos_list);

        String directory = getIntent().getStringExtra("directory");

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(directory);
        RecyclerView recyclerView = findViewById(R.id.recycler);

        StorageReference reference = FirebaseStorage.getInstance().getReference().child(Objects.requireNonNull(directory));

        reference.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                ArrayList<Video> arrayList = new ArrayList<>();
                VideosAdapter adapter = new VideosAdapter(VideosListActivity.this, arrayList);
                for (StorageReference storageReference : listResult.getItems()) {
                    Video video = new Video();
                    video.setName(storageReference.getName());
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            video.setUri(uri);
                            arrayList.add(video);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
                recyclerView.setAdapter(adapter);

                adapter.setOnItemClickListener(new VideosAdapter.OnItemClickListener() {
                    @Override
                    public void onClick(Uri uri) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(uri, "video/mp4");
                        startActivity(intent);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(VideosListActivity.this, "There was an error while listing videos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}