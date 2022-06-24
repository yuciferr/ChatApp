package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.chatapp.Adapter.ChatAdapter;
import com.example.chatapp.Models.Messages;
import com.example.chatapp.databinding.ActivityChatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ChatDetailActivity extends AppCompatActivity {

    ActivityChatBinding binding;
    FirebaseAuth mAuth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        final String writerId = mAuth.getUid();
        final String readerId = getIntent().getStringExtra("userId");
        String userName = getIntent().getStringExtra("userName");
        String profilePic = getIntent().getStringExtra("profilePic");


        binding.userName.setText(userName);
        Picasso.get().load(profilePic).into(binding.profilePic);


        binding.backArrow.setOnClickListener(v -> {
            finish();
        });

        final ArrayList<Messages> cList = new ArrayList<>();
        final String sRoom = writerId + readerId;
        final String rRoom = readerId + writerId;

        final ChatAdapter adapter = new ChatAdapter(cList, this, readerId);

        binding.messageRecyclerView.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.messageRecyclerView.setLayoutManager(linearLayoutManager);
        binding.messageRecyclerView.scrollToPosition(cList.size()-1);

        linearLayoutManager.setStackFromEnd(true);

        database.getReference().child("Chats").child(sRoom).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cList.clear();


                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Messages messages = snapshot.getValue(Messages.class);
                    messages.setMessageId(snapshot.getKey());

                    cList.add(messages);
                    binding.messageRecyclerView.scrollToPosition(cList.size()-1);

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        binding.send.setOnClickListener(v -> {
            String message = binding.message.getText().toString();
            final Messages model = new Messages(message, writerId);
            model.setTimeStamp(new Date().getTime());
            binding.message.setText("");

            database.getReference("Chats").child(sRoom).push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    database.getReference("Chats").child(rRoom).push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });
                }
            });
        });


    }
}