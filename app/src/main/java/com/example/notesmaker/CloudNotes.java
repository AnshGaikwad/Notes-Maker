package com.example.notesmaker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.Frame;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

public class CloudNotes extends AppCompatActivity {

    RecyclerView cloudNotes;
    LinearLayoutManager linearLayoutManager;
    CloudFileAdapter cloudFileAdapter;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseStorage storage;
    StorageReference pdfStorage;
    SwipeRefreshLayout swipeRefreshLayout;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_notes);

        swipeRefreshLayout = findViewById(R.id.cloudListRefresh);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        if (mUser!=null){
            storage = FirebaseStorage.getInstance();
            pdfStorage = storage.getReference();
            pdfStorage = pdfStorage.child(mUser.getUid());
            pdfStorage = pdfStorage.child("PDFs");
            getFiles();
            swipeRefreshLayout.setEnabled(true);


            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getFiles();
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
        } else {
            swipeRefreshLayout.setEnabled(false);
        }

        cloudNotes = findViewById(R.id.cloudNotes);
        linearLayoutManager = new LinearLayoutManager(this);


    }

    public void getFiles(){
        pdfStorage.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                cloudFileAdapter = new CloudFileAdapter(getApplicationContext(), listResult.getItems());
                cloudNotes.setLayoutManager(linearLayoutManager);
                cloudNotes.setAdapter(cloudFileAdapter);
            }
        });
    }
}